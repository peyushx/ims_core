package rapifuzz.com.ims.service;

import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import rapifuzz.com.ims.exception.CustomException;
import rapifuzz.com.ims.model.UserModel;
import rapifuzz.com.ims.model.entity.UserEntity;
import rapifuzz.com.ims.repository.UserRepository;

import java.util.*;

@Slf4j
@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserModel login(String email, String password) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            boolean isPasswordValid = BCrypt.checkpw(password, user.get().getPassword());
            if (isPasswordValid) {
                return this.generateUserModel(user.get());
            } else {
                throw new CustomException("Incorrect Password", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new CustomException("Invalid Email", HttpStatus.BAD_REQUEST);
        }
    }

    private UserModel generateUserModel(UserEntity userEntity) {
        UserModel userModel = new UserModel(userEntity.getEmail(), userEntity.getEmail(), this.getAuthorities(userEntity), true, true, true, true);
        userModel.setUser(userEntity);
        return userModel;
    }

    /**
     * Creates a PreAuthenticatedAuthenticationToken for the logged-in user.
     *
     * @return PreAuthenticatedAuthenticationToken - The authentication token.
     * @throws CustomException - if the user is not authorized.
     */
    public PreAuthenticatedAuthenticationToken getAuthentication(String email) {
        try {
            UserEntity user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                UserModel userModel = new UserModel(user.getEmail(), user.getEmail(), this.getAuthorities(user), true, true, true, true);
                userModel.setUser(user);
                return new PreAuthenticatedAuthenticationToken(userModel, "", this.getAuthorities(user));
            } else {
                throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Retrieves authorities for the given user entity.
     *
     * @param userEntity - The user entity.
     * @return List<SimpleGrantedAuthority> - The list of granted authorities.
     */
    private List<SimpleGrantedAuthority> getAuthorities(UserEntity userEntity) {
        List<String> authority = new ArrayList<>();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String p : authority) {
            authorities.add(new SimpleGrantedAuthority(p));
        }
        return authorities;
    }

    /**
     * Retrieves the currently logged-in user's role from the security context.
     *
     * @return UserModel - The authenticated user's details.
     * @throws CustomException - if the user is not authenticated.
     */
    public UserModel getLoggedinUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserModel) {
            return (UserModel) authentication.getPrincipal();
        } else {
            return null;
        }
    }

    /**
     * Loads user details by username.
     *
     * @param username - The username.
     * @return UserDetails - The user details.
     * @throws UsernameNotFoundException - if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByEmail(username);
        UserEntity user = userEntity.orElse(null);
        if (userEntity.isPresent()) {
            List<SimpleGrantedAuthority> authorities = this.getAuthorities(user);
            UserModel userModel = new UserModel(user.getEmail(), user.getEmail(), authorities, true, true, true, true);
            userModel.setUser(user);
            return userModel;
        } else {
            throw new CustomException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

}
