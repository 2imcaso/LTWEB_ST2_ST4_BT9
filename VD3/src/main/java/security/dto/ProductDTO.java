package security.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
@Data public class ProductDTO { private Long id; @NotBlank private String name; private String description; @NotNull @DecimalMin("0.0") private BigDecimal price; private String imageUrl; private Long userId; private String username; }
