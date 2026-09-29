package security.service;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.dto.RegisterDTO;
import security.entity.User;
import security.repository.*;
@Service public class AuthService {
 public static final String REGISTER="REGISTER",RESET="RESET";
 private final UserRepository users;private final RoleRepository roles;private final PasswordEncoder encoder;private final OtpService otp;private final EmailService mail;
 public AuthService(UserRepository u,RoleRepository r,PasswordEncoder e,OtpService o,EmailService m){users=u;roles=r;encoder=e;otp=o;mail=m;}
 @Transactional public String register(RegisterDTO d){String email=d.getEmail().trim().toLowerCase(Locale.ROOT);if(users.existsByEmailIgnoreCase(email))throw new IllegalArgumentException("Email đã tồn tại.");if(users.existsByUsernameIgnoreCase(d.getUsername().trim()))throw new IllegalArgumentException("Username đã tồn tại.");if(!d.getPassword().equals(d.getConfirmPassword()))throw new IllegalArgumentException("Mật khẩu xác nhận không khớp.");User u=new User();u.setUsername(d.getUsername().trim());u.setEmail(email);u.setFullName(d.getFullName().trim());u.setPassword(encoder.encode(d.getPassword()));u.setImages("/images/avatar-default.svg");u.setEnabled(false);u.setRole(roles.findByNameIgnoreCase("USER").orElseThrow());users.save(u);return issue(email,REGISTER,"đăng ký");}
 public String resendRegister(String email){if(users.findByEmailIgnoreCase(email).filter(u->!u.isEnabled()).isEmpty())throw new IllegalArgumentException("Không tìm thấy tài khoản chờ xác nhận.");return issue(email,REGISTER,"đăng ký");}
 @Transactional public void verifyRegister(String email,String code){if(!otp.verify(email,REGISTER,code))throw new IllegalArgumentException("OTP không đúng, đã hết hạn hoặc vượt số lần thử.");User u=users.findByEmailIgnoreCase(email).orElseThrow();u.setEnabled(true);}
 public String forgot(String email){users.findByEmailIgnoreCase(email).orElseThrow(()->new IllegalArgumentException("Email chưa được đăng ký."));return issue(email,RESET,"đặt lại mật khẩu");}
 @Transactional public void reset(String email,String code,String password,String confirm){if(!password.equals(confirm))throw new IllegalArgumentException("Mật khẩu xác nhận không khớp.");if(!otp.verify(email,RESET,code))throw new IllegalArgumentException("OTP không đúng, đã hết hạn hoặc vượt số lần thử.");User u=users.findByEmailIgnoreCase(email).orElseThrow();u.setPassword(encoder.encode(password));}
 private String issue(String email,String type,String purpose){String code=otp.create(email,type);mail.sendOtp(email,code,purpose);return code;}
 public boolean mailEnabled(){return mail.isEnabled();}
}
