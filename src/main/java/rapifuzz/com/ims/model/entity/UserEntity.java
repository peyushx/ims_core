package rapifuzz.com.ims.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rapifuzz.com.ims.model.request.SignupRecord;


@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "pincode", length = 6)
    private String pincode;

    @Column(name = "phone_code", length = 5)
    private String phoneCode;

    @Column(name = "mobile_number", length = 10)
    private String mobileNumber;

    @Column(name = "password")
    private String password;


    public UserEntity(SignupRecord record){
        this.firstName = record.firstName();
        this.lastName = record.lastName();
        this.email = record.email();
        this.address = record.address();
        this.country = record.country();
        this.state = record.state();
        this.city = record.city();
        String hashpw = BCrypt.hashpw(record.password(), BCrypt.gensalt());
        this.password = hashpw;
        this.pincode = record.pincode();
        this.phoneCode = record.phoneCode();
        this.mobileNumber = record.mobileNumber().toString();
    }

}
