package security.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="products", indexes=@Index(name="idx_products_name", columnList="name")) @Getter @Setter
public class Product {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=500) private String name;
 @Column(length=2000) private String description;
 @Column(nullable=false,precision=18,scale=2) private BigDecimal price;
 @Column(length=1000) private String imageUrl;
 @Column(length=255) private String imagePublicId;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id",nullable=false) private User user;
 @Column(nullable=false) private LocalDateTime createdAt=LocalDateTime.now();
}
