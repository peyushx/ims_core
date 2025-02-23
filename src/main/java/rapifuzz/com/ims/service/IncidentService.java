package rapifuzz.com.ims.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import rapifuzz.com.ims.constants.enums.IncidentStatus;
import rapifuzz.com.ims.model.entity.IncidentEntity;
import rapifuzz.com.ims.model.entity.UserEntity;
import rapifuzz.com.ims.model.request.IncidentRecord;
import rapifuzz.com.ims.model.request.SearchIncidentRecord;
import rapifuzz.com.ims.model.response.ImsResponse;
import rapifuzz.com.ims.repository.IncidentRepository;
import rapifuzz.com.ims.util.ServiceUtils;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public ResponseEntity<ImsResponse> getIncidents(SearchIncidentRecord searchIncidentRecord,Pageable pageable) {
        UserEntity user = ServiceUtils.getUserDetailService().getLoggedinUser().getUser();
        return ImsResponse.success("Incidents fetched successfully", incidentRepository.findAll(user.getId(),searchIncidentRecord.searchKey(),pageable));
    }

    public ResponseEntity<ImsResponse> createIncident(IncidentRecord incidentRecord) {
        IncidentEntity incidentEntity = null;
        UserEntity reporter = ServiceUtils.getUserDetailService().getLoggedinUser().getUser();
        if (!ObjectUtils.isEmpty(incidentRecord.id())){
            incidentEntity = incidentRepository.findById(incidentRecord.id()).orElse(null);
            if (incidentEntity == null){
                incidentEntity = new IncidentEntity(incidentRecord, reporter);
            } else {
                if(!incidentEntity.getStatus().equals(IncidentStatus.CLOSED)) {
                    incidentEntity.update(incidentRecord, reporter);
                } else {
                    return ImsResponse.failed(HttpStatus.BAD_REQUEST,"Incident is already closed");
                }
            }
        } else {
            incidentEntity = new IncidentEntity(incidentRecord, reporter);
        }
        incidentRepository.save(incidentEntity);
        return ImsResponse.success("Incident created successfully", incidentEntity);
    }
}
