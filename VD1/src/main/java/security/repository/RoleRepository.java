package security.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import security.entity.Role;
import java.util.Optional;
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameIgnoreCase(String name);
}
