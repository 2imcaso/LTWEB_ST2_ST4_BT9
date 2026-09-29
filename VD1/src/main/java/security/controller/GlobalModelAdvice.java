package security.controller;
import java.security.Principal;
import org.springframework.web.bind.annotation.*;
import security.dto.UserDTO;
import security.mapper.UserMapper;
import security.repository.UserRepository;
@ControllerAdvice
public class GlobalModelAdvice {
    private final UserRepository users;
    private final UserMapper mapper;
    public GlobalModelAdvice(UserRepository users, UserMapper mapper) {
        this.users = users;
        this.mapper = mapper;
    }
    @ModelAttribute("currentUser")
    public UserDTO currentUser(Principal principal) {
        return principal == null ? null : users.findByEmailWithRole(principal.getName())
            .map(mapper::toDto).orElse(null);
    }
}
