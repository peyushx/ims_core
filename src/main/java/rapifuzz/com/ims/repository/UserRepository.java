package rapifuzz.com.ims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rapifuzz.com.ims.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);
}
