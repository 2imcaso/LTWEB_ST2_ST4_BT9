package security.controller;
import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.dto.ProductDTO;
import security.entity.Product;
import security.mapper.ProductMapper;
import security.repository.*;
import security.security.CustomUserDetails;
import security.service.ImageService;
@Controller @RequestMapping("/products") public class ProductController {
 private final ProductRepository products;private final UserRepository users;private final ProductMapper mapper;private final ImageService images;
 public ProductController(ProductRepository p,UserRepository u,ProductMapper m,ImageService i){products=p;users=u;mapper=m;images=i;}
 @GetMapping public String list(@RequestParam(defaultValue="")String keyword,@RequestParam(defaultValue="0")int page,@RequestParam(defaultValue="10")int size,Authentication a,Model m){var d=(CustomUserDetails)a.getPrincipal();boolean admin=d.getAuthorities().stream().anyMatch(x->x.getAuthority().equals("ROLE_ADMIN"));Page<Product> result=admin?(keyword.isBlank()?products.findAll(PageRequest.of(page,size)):products.search(keyword,PageRequest.of(page,size))):products.findByUserId(d.getId(),PageRequest.of(page,size));m.addAttribute("products",result.map(mapper::toDto));m.addAttribute("keyword",keyword);m.addAttribute("size",size);return "product/list";}
 @GetMapping("/create") public String create(Model m){m.addAttribute("productDTO",new ProductDTO());m.addAttribute("mode","create");return "product/form";}
 @PostMapping("/create") public String create(@Valid @ModelAttribute ProductDTO d,BindingResult b,@RequestParam(required=false)MultipartFile image,Authentication a,Model m,RedirectAttributes r)throws IOException{if(b.hasErrors()){m.addAttribute("mode","create");return "product/form";}var principal=(CustomUserDetails)a.getPrincipal();Product p=mapper.toEntity(d);p.setUser(users.findById(principal.getId()).orElseThrow());try{var upload=images.upload(image);if(upload!=null){p.setImageUrl(upload.url());p.setImagePublicId(upload.publicId());}}catch(IllegalStateException e){b.reject("image",e.getMessage());m.addAttribute("mode","create");return "product/form";}products.save(p);r.addFlashAttribute("success","Đã thêm sản phẩm.");return "redirect:/products";}
 @GetMapping("/edit/{id}") public String edit(@PathVariable Long id,Authentication a,Model m){Product p=owned(id,a);m.addAttribute("productDTO",mapper.toDto(p));m.addAttribute("mode","edit");return "product/form";}
 @PostMapping("/edit/{id}") public String edit(@PathVariable Long id,@Valid @ModelAttribute ProductDTO d,BindingResult b,@RequestParam(required=false)MultipartFile image,Authentication a,Model m,RedirectAttributes r)throws IOException{Product p=owned(id,a);if(b.hasErrors()){m.addAttribute("mode","edit");return "product/form";}p.setName(d.getName());p.setDescription(d.getDescription());p.setPrice(d.getPrice());try{var upload=images.upload(image);if(upload!=null){images.delete(p.getImagePublicId());p.setImageUrl(upload.url());p.setImagePublicId(upload.publicId());}}catch(IllegalStateException e){b.reject("image",e.getMessage());m.addAttribute("mode","edit");return "product/form";}products.save(p);r.addFlashAttribute("success","Đã cập nhật sản phẩm.");return "redirect:/products";}
 @PostMapping("/delete/{id}") public String delete(@PathVariable Long id,Authentication a,RedirectAttributes r)throws IOException{Product p=owned(id,a);images.delete(p.getImagePublicId());products.delete(p);r.addFlashAttribute("success","Đã xóa sản phẩm.");return "redirect:/products";}
 private Product owned(Long id,Authentication a){Product p=products.findById(id).orElseThrow();var d=(CustomUserDetails)a.getPrincipal();boolean admin=d.getAuthorities().stream().anyMatch(x->x.getAuthority().equals("ROLE_ADMIN"));if(!admin&&!p.getUser().getId().equals(d.getId()))throw new org.springframework.security.access.AccessDeniedException("Không có quyền");return p;}
}
