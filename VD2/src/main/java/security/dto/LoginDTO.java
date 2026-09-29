package security.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class LoginDTO {
    @NotBlank private String login;
    @NotBlank @Size(min = 6, max = 100) private String password;
}
