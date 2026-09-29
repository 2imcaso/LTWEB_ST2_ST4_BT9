package security.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.*;
import security.entity.User;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    @EntityGraph(attributePaths = "role")
    Optional<User> findById(Long id);

    @Override
    @EntityGraph(attributePaths = "role")
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "role")
    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);
    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);
    Page<User> findByEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(String email, String fullName, Pageable pageable);
    @Query("select u from User u join fetch u.role where lower(u.email) = lower(:email)")
    Optional<User> findByEmailWithRole(@Param("email") String email);
    @Query("select u from User u join fetch u.role where lower(u.username) like lower(concat('%',:keyword,'%')) or lower(u.email) like lower(concat('%',:keyword,'%')) or lower(u.fullName) like lower(concat('%',:keyword,'%'))")
    Page<User> search(@Param("keyword") String keyword, Pageable pageable);
}
