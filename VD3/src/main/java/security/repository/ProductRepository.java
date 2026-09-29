package security.repository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import security.entity.Product;
public interface ProductRepository extends JpaRepository<Product,Long> {
 @EntityGraph(attributePaths="user") Page<Product> findAll(Pageable pageable);
 @EntityGraph(attributePaths="user") @Query("select p from Product p where lower(p.name) like lower(concat('%',:keyword,'%')) or lower(coalesce(p.description,'')) like lower(concat('%',:keyword,'%'))") Page<Product> search(@Param("keyword") String keyword,Pageable pageable);
 @EntityGraph(attributePaths="user") Page<Product> findByUserId(Long userId,Pageable pageable);
 long countByUserId(Long userId);
}
