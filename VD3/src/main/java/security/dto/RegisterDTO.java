package security.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data public class RegisterDTO {
 @NotBlank @Size(max=50) private String username;
 @NotBlank @Email @Size(max=150) private String email;
 @NotBlank @Size(min=6,max=72) private String password;
 @NotBlank private String confirmPassword;
 @NotBlank @Size(max=120) private String fullName;
}
