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
@RequestMapping("/api/v1/incident")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping("/save")
    public ResponseEntity<ImsResponse> createIncident(@RequestBody IncidentRecord incidentRecord){
        return incidentService.createIncident(incidentRecord);
    }

    @PostMapping("/fetch-all")
    public ResponseEntity<ImsResponse> fetchAllIncidents(@RequestBody SearchIncidentRecord searchIncidentRecord, Pageable pageable){
        return incidentService.getIncidents(searchIncidentRecord,pageable);
    }
}
