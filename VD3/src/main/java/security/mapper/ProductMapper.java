package security.mapper;
import org.mapstruct.*;
import security.dto.ProductDTO;
import security.entity.Product;
@Mapper(componentModel="spring",unmappedTargetPolicy=ReportingPolicy.IGNORE)
public interface ProductMapper {
 @Mapping(target="userId",source="user.id") @Mapping(target="username",source="user.username") ProductDTO toDto(Product entity);
 @Mapping(target="user",ignore=true) @Mapping(target="imagePublicId",ignore=true) Product toEntity(ProductDTO dto);
}
