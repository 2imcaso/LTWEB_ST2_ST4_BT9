package security.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import security.entity.User;
import security.entity.Role;
import security.repository.*;
@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initData(RoleRepository roles, UserRepository users, PasswordEncoder encoder,
            @Value("${ADMIN_EMAIL:trungnh@hcmute.edu.vn}") String email,
            @Value("${ADMIN_PASSWORD:123456}") String password) {
        return args -> {
            roles.findByNameIgnoreCase("USER").orElseGet(() -> roles.save(new Role("USER")));
            Role adminRole = roles.findByNameIgnoreCase("ADMIN")
                .orElseGet(() -> roles.save(new Role("ADMIN")));
            if (!users.existsByEmailIgnoreCase(email.trim())) {
                User admin = new User();
                admin.setEmail(email.trim().toLowerCase(java.util.Locale.ROOT));
                admin.setFullName("System Administrator");
                admin.setPassword(encoder.encode(password));
                admin.setRole(adminRole);
                admin.setEnabled(true);
                users.save(admin);
            }
        };
    }
}
