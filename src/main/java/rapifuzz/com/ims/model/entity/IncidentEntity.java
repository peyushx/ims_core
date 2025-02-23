package rapifuzz.com.ims.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rapifuzz.com.ims.constants.enums.IncidentStatus;
import rapifuzz.com.ims.constants.enums.Priority;
import rapifuzz.com.ims.constants.enums.UserType;
import rapifuzz.com.ims.model.request.IncidentRecord;
import rapifuzz.com.ims.util.DateUtil;

import java.time.LocalDateTime;

@Entity
@Table(name = "incident")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class IncidentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incident_number", unique = true, nullable = false)
    private String incidentNumber;

    @Column(name = "incidentType", nullable = false)
    private UserType incidentType;

    @Column(name = "incident_details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    @ManyToOne
    @JoinColumn(name = "fk_id_user")
    private UserEntity userEntity;

    @CreatedDate
    private LocalDateTime createdDate;

    public IncidentEntity(IncidentRecord incidentRecord, UserEntity reporter) {
        this.userEntity = reporter;
        this.details = incidentRecord.details();
        this.incidentType = incidentRecord.incidentType();
        this.priority = incidentRecord.priority();
        this.status = incidentRecord.status();
        this.addIncidentNumber();
    }
    public void addIncidentNumber() {
        this.incidentNumber = "RMG" + (int) (Math.random() * 100000) + DateUtil.getCurrentYear();
    }

    public void update(IncidentRecord incidentRecord, UserEntity reporter) {
        this.userEntity = reporter;
        this.details = incidentRecord.details();
        this.incidentType = incidentRecord.incidentType();
        this.priority = incidentRecord.priority();
        this.status = incidentRecord.status();
    }
}