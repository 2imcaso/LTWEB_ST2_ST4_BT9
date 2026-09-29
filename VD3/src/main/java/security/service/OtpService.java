package security.service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.entity.OtpToken;
import security.repository.OtpTokenRepository;
@Service public class OtpService {
 private final OtpTokenRepository tokens; private final PasswordEncoder encoder; private final SecureRandom random=new SecureRandom();
 @Value("${app.otp.expiration-minutes:10}") private long minutes; @Value("${app.otp.max-attempts:5}") private int maxAttempts;
 public OtpService(OtpTokenRepository tokens,PasswordEncoder encoder){this.tokens=tokens;this.encoder=encoder;}
 @Transactional public String create(String email,String type){tokens.deleteByEmailIgnoreCaseAndType(email,type);String code="%06d".formatted(random.nextInt(1_000_000));OtpToken t=new OtpToken();t.setEmail(email.toLowerCase());t.setType(type);t.setOtpHash(encoder.encode(code));t.setExpiresAt(LocalDateTime.now().plusMinutes(minutes));tokens.save(t);return code;}
 @Transactional public boolean verify(String email,String type,String code){var t=tokens.findTopByEmailIgnoreCaseAndTypeAndUsedFalseOrderByCreatedAtDesc(email,type).orElse(null);if(t==null||t.getExpiresAt().isBefore(LocalDateTime.now())||t.getAttempts()>=maxAttempts)return false;t.setAttempts(t.getAttempts()+1);if(!encoder.matches(code,t.getOtpHash()))return false;t.setUsed(true);return true;}
}
