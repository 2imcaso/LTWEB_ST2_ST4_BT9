package security.controller;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
    @GetMapping("/")
    public String home() { return "home"; }
    @GetMapping("/dashboard")
    public String dashboard() { return "dashboard"; }
    @GetMapping("/access-denied")
    public String denied(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "access-denied";
    }
}
