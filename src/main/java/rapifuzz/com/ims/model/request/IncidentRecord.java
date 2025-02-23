package rapifuzz.com.ims.model.request;

import rapifuzz.com.ims.constants.enums.IncidentStatus;
import rapifuzz.com.ims.constants.enums.Priority;
import rapifuzz.com.ims.constants.enums.UserType;

public record IncidentRecord(Long id,
                             String incidentNumber,
                             UserType incidentType,
                             String details,
                             Priority priority,
                             IncidentStatus status,
                             Long userId
                             ) {
}
