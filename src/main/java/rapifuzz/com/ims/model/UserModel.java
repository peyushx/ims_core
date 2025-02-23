package rapifuzz.com.ims.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import rapifuzz.com.ims.model.entity.UserEntity;

import java.util.Collection;

@Getter
@Setter
public class UserModel extends User {


    private String username;
    private String password;
    private String token;
    private UserEntity user;

    public UserModel(String username, String password,
                     Collection<? extends GrantedAuthority> authorities, boolean enabled, boolean accountNonExpired,
                     boolean credentialsNonExpired, boolean accountNonLocked) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }


}