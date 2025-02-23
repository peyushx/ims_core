package rapifuzz.com.ims.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rapifuzz.com.ims.model.request.LoginRecord;
import rapifuzz.com.ims.model.request.SignupRecord;
import rapifuzz.com.ims.model.response.ImsResponse;
import rapifuzz.com.ims.service.UserService;

@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ImsResponse> signUp(@RequestBody SignupRecord signupRecord) {
        return userService.signUp(signupRecord);
    }

    @PostMapping("/login")
    public ResponseEntity<ImsResponse> login(@RequestBody LoginRecord loginRecord) {
        return userService.login(loginRecord);
    }
}

