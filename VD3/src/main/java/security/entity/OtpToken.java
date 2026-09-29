package security.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Entity @Table(name="otp_tokens",indexes=@Index(name="idx_otp_email_type",columnList="email,type")) @Getter @Setter
public class OtpToken {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=150) private String email;
 @Column(nullable=false,length=100) private String otpHash;
 @Column(nullable=false,length=30) private String type;
 @Column(nullable=false) private LocalDateTime expiresAt;
 @Column(nullable=false) private int attempts;
 @Column(nullable=false) private boolean used;
 @Column(nullable=false) private LocalDateTime createdAt=LocalDateTime.now();
}
