package security.controller;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.dto.UserDTO;
import security.entity.User;
import security.mapper.UserMapper;
import security.repository.*;
@Controller @RequestMapping("/users") public class UserController {
 private final UserRepository users;private final RoleRepository roles;private final ProductRepository products;private final UserMapper mapper;private final PasswordEncoder encoder;
 public UserController(UserRepository u,RoleRepository r,ProductRepository p,UserMapper m,PasswordEncoder e){users=u;roles=r;products=p;mapper=m;encoder=e;}
 @GetMapping public String list(@RequestParam(defaultValue="")String keyword,@RequestParam(defaultValue="0")int page,@RequestParam(defaultValue="10")int size,Model m){Page<User> result=keyword.isBlank()?users.findAll(PageRequest.of(page,size)):users.search(keyword,PageRequest.of(page,size));var dto=result.map(u->{var d=mapper.toDto(u);d.setProductCount(products.countByUserId(u.getId()));return d;});m.addAttribute("users",dto);m.addAttribute("keyword",keyword);m.addAttribute("size",size);return "user/list";}
 @GetMapping("/create") public String create(Model m){m.addAttribute("userDTO",new UserDTO());m.addAttribute("roles",roles.findAll());m.addAttribute("mode","create");return "user/form";}
 @PostMapping("/create") public String create(@Valid @ModelAttribute UserDTO d,BindingResult b,Model m,RedirectAttributes r){if(users.existsByEmailIgnoreCase(d.getEmail()))b.rejectValue("email","exists","Email đã tồn tại");if(users.existsByUsernameIgnoreCase(d.getUsername()))b.rejectValue("username","exists","Username đã tồn tại");if(b.hasErrors()){m.addAttribute("roles",roles.findAll());m.addAttribute("mode","create");return "user/form";}User u=mapper.toEntity(d);u.setPassword(encoder.encode("123456"));u.setRole(roles.findById(d.getRoleId()).orElseThrow());users.save(u);r.addFlashAttribute("success","Đã thêm user. Mật khẩu mặc định: 123456");return "redirect:/users";}
 @GetMapping("/edit/{id}") public String edit(@PathVariable Long id,Model m){m.addAttribute("userDTO",mapper.toDto(users.findById(id).orElseThrow()));m.addAttribute("roles",roles.findAll());m.addAttribute("mode","edit");return "user/form";}
 @PostMapping("/edit/{id}") public String edit(@PathVariable Long id,@Valid @ModelAttribute UserDTO d,BindingResult b,Model m,RedirectAttributes r){if(b.hasErrors()){m.addAttribute("roles",roles.findAll());m.addAttribute("mode","edit");return "user/form";}User u=users.findById(id).orElseThrow();u.setUsername(d.getUsername());u.setEmail(d.getEmail());u.setFullName(d.getFullName());u.setEnabled(d.isEnabled());u.setRole(roles.findById(d.getRoleId()).orElseThrow());users.save(u);r.addFlashAttribute("success","Đã cập nhật user.");return "redirect:/users";}
 @PostMapping("/delete/{id}") public String delete(@PathVariable Long id,RedirectAttributes r){users.deleteById(id);r.addFlashAttribute("success","Đã xóa user.");return "redirect:/users";}
}
