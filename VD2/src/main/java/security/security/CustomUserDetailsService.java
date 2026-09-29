package security.security;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import security.repository.UserRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    public CustomUserDetailsService(UserRepository users) { this.users = users; }
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return users.findByUsernameIgnoreCaseOrEmailIgnoreCase(login.trim(), login.trim())
            .map(CustomUserDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản."));
    }
}
