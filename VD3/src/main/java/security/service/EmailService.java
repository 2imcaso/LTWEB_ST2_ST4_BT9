package security.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service public class EmailService {
 private final JavaMailSender sender; @Value("${app.mail.enabled:false}") private boolean enabled;
 public EmailService(JavaMailSender sender){this.sender=sender;}
 public boolean isEnabled(){return enabled;}
 public void sendOtp(String email,String code,String purpose){if(!enabled)return;SimpleMailMessage m=new SimpleMailMessage();m.setTo(email);m.setSubject("VD3 - Ma OTP "+purpose);m.setText("Ma OTP cua ban: "+code+". Ma co hieu luc trong 10 phut.");sender.send(m);}
}
