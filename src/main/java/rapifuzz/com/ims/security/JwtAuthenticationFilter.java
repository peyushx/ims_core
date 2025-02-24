package rapifuzz.com.ims.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import rapifuzz.com.ims.exception.CustomException;
import rapifuzz.com.ims.model.response.ImsResponse;
import rapifuzz.com.ims.service.UserDetailService;

import java.io.IOException;
import java.util.*;


@Component
@RequiredArgsConstructor
public final class JwtAuthenticationFilter extends OncePerRequestFilter {


    private static final String[] AUTH_WHITELIST = {
            "/api/v1/user/signup",
            "/api/v1/user/login",
            "/"
    };
    private final UserDetailService userDetailService;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws IOException {
        try {
            String token = request.getHeader(SecurityConstant.AUTHORIZATION_HEADER);
            if (!Objects.isNull(token) && jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getUserEmail(token);
                Authentication auth = userDetailService.getAuthentication(email);
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
            } else {
                logger.error("Unauthorized access for uri :: " + request.getRequestURI());
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } catch(Exception e){
            logger.error("Could not authenticate user for uri :: " + request.getRequestURI(), e);
            setResponse(response, e.getMessage(), HttpStatus.FORBIDDEN.value());
        }
    }
    private void setResponse (HttpServletResponse response, String message, int httpStatusValue)
            throws IOException {
        response.setStatus(httpStatusValue);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonRespString = ow.writeValueAsString(new ImsResponse<>(null, message, httpStatusValue));
        response.setContentType("application/json");
        response.getWriter().write(jsonRespString);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher pathMatcher = new AntPathMatcher(AntPathMatcher.DEFAULT_PATH_SEPARATOR);
        return Arrays.asList(AUTH_WHITELIST).stream().anyMatch(path -> pathMatcher.match(path, request.getRequestURI()));
    }

}
