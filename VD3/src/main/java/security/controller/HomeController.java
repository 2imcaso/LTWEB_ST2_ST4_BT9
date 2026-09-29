package security.controller;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import security.repository.*;
@Controller
public class HomeController {
    private final UserRepository users;private final ProductRepository products;
    public HomeController(UserRepository u,ProductRepository p){users=u;products=p;}
    @GetMapping("/") public String home(Model m){m.addAttribute("userCount",users.count());m.addAttribute("productCount",products.count());return "home";}
    @GetMapping("/dashboard") public String dashboard(Model m){m.addAttribute("userCount",users.count());m.addAttribute("productCount",products.count());return "dashboard";}
    @GetMapping("/access-denied")
    public String denied(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "access-denied";
    }
}
