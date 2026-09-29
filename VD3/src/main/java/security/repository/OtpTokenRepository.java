package security.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import security.entity.OtpToken;
import java.util.Optional;
public interface OtpTokenRepository extends JpaRepository<OtpToken,Long> {
 Optional<OtpToken> findTopByEmailIgnoreCaseAndTypeAndUsedFalseOrderByCreatedAtDesc(String email,String type);
 void deleteByEmailIgnoreCaseAndType(String email,String type);
}
