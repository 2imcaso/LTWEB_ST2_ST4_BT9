package security.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data public class VerifyOtpDTO { @NotBlank @Email private String email; @NotBlank @Pattern(regexp="\\d{6}") private String otp; }
