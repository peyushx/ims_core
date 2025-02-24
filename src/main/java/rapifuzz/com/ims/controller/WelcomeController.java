package rapifuzz.com.ims.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rapifuzz.com.ims.model.response.ImsResponse;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/")
public class WelcomeController {

    @GetMapping
    public ResponseEntity<ImsResponse> welcome(){
        return ImsResponse.success("Welcome to Ims Core", true);
    }
}
