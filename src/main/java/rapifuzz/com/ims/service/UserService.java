package rapifuzz.com.ims.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rapifuzz.com.ims.model.UserModel;
import rapifuzz.com.ims.model.entity.UserEntity;
import rapifuzz.com.ims.model.request.LoginRecord;
import rapifuzz.com.ims.model.request.SignupRecord;
import rapifuzz.com.ims.model.response.ImsResponse;
import rapifuzz.com.ims.repository.UserRepository;
import rapifuzz.com.ims.security.JwtTokenProvider;
import rapifuzz.com.ims.util.ServiceUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<ImsResponse> signUp(SignupRecord signupRecord) {
        UserEntity user = new UserEntity(signupRecord);
        userRepository.save(user);
        return ImsResponse.success("User created successfully", user);
    }

    public ResponseEntity<ImsResponse> login(LoginRecord loginRecord) {
        UserModel userModel = ServiceUtils.getUserDetailService().login(loginRecord.email(), loginRecord.password());
        userModel.setToken(jwtTokenProvider.createToken(userModel.getUser().getEmail()));
        return ImsResponse.success("User logged in successfully", userModel);
    }
}
