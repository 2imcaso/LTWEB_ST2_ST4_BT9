package security.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String images;
    @Email @NotBlank private String email;
    @NotBlank private String fullName;
    @NotNull private Long roleId;
    private String roleName;
    private boolean enabled;
    private LocalDateTime createdAt;
    private long productCount;
}
