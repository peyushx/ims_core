package rapifuzz.com.ims.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rapifuzz.com.ims.model.request.IncidentRecord;
import rapifuzz.com.ims.model.request.SearchIncidentRecord;
import rapifuzz.com.ims.model.response.ImsResponse;
import rapifuzz.com.ims.service.IncidentService;

@RestController
@RequestMapping("/")
public class IncidentController {

    @GetMapping
    public ResponseEntity<String> welcome(){
        return ImsResponse.success("Welcome to Ims Core", true);
    }
}
