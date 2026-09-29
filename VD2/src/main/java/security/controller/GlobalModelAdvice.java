package security.controller;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import security.dto.UserDTO;
import security.security.CustomUserDetails;
import security.service.UserService;
@ControllerAdvice
public class GlobalModelAdvice {
    private final UserService users;
    public GlobalModelAdvice(UserService users) { this.users = users; }
    @ModelAttribute("currentUser")
    public UserDTO currentUser(Authentication authentication) {
        return authentication != null && authentication.getPrincipal() instanceof CustomUserDetails user
            ? users.findById(user.getId()) : null;
    }
}
