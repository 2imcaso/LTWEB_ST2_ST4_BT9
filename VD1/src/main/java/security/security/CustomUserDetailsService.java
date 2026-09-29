package security.security;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import security.repository.UserRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    public CustomUserDetailsService(UserRepository users) { this.users = users; }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = users.findByEmailWithRole(email.trim())
            .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản."));
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
            .password(user.getPassword()).roles(user.getRole().getName())
            .disabled(!user.isEnabled()).build();
    }
}
