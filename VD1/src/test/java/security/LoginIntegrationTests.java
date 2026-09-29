package security;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;
import security.entity.User;
import security.repository.*;
import security.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:VD1;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa", "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "ADMIN_EMAIL=trungnh@hcmute.edu.vn", "ADMIN_PASSWORD=123456"
})
@AutoConfigureMockMvc
class LoginIntegrationTests {
    @Autowired MockMvc mvc;
    @Autowired UserRepository users;
    @Autowired RoleRepository roles;
    @Autowired PasswordEncoder encoder;
    @Autowired UserMapper mapper;
    @Test void publicPagesAndCsrf() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(containsString("Đăng nhập")));
        mvc.perform(get("/login")).andExpect(status().isOk()).andExpect(content().string(containsString("_csrf")));
        mvc.perform(post("/login").param("email", "trungnh@hcmute.edu.vn").param("password", "123456"))
            .andExpect(status().isForbidden());
        mvc.perform(get("/dashboard")).andExpect(status().is3xxRedirection());
    }
    @Test void adminLoginHeaderAndLogout() throws Exception {
        var result = mvc.perform(post("/login").with(csrf()).param("email", "TRUNGNH@HCMUTE.EDU.VN").param("password", "123456"))
            .andExpect(authenticated()).andExpect(redirectedUrl("/dashboard")).andReturn();
        var session = (MockHttpSession) result.getRequest().getSession(false);
        mvc.perform(get("/dashboard").session(session)).andExpect(status().isOk())
            .andExpect(content().string(containsString("System Administrator")))
            .andExpect(content().string(containsString("trungnh@hcmute.edu.vn")));
        mvc.perform(post("/logout").session(session)).andExpect(status().isForbidden());
        mvc.perform(post("/logout").session(session).with(csrf())).andExpect(redirectedUrl("/login?logout=true"))
            .andExpect(unauthenticated());
        mvc.perform(get("/dashboard")).andExpect(status().is3xxRedirection());
        var admin = users.findByEmailWithRole("trungnh@hcmute.edu.vn").orElseThrow();
        assertThat(admin.getPassword()).isNotEqualTo("123456");
        assertThat(encoder.matches("123456", admin.getPassword())).isTrue();
        assertThat(mapper.toDto(admin).getRoleName()).isEqualTo("ADMIN");
    }
    @Test void wrongPasswordAndDisabledAccountFail() throws Exception {
        mvc.perform(post("/login").with(csrf()).param("email", "trungnh@hcmute.edu.vn").param("password", "wrong"))
            .andExpect(unauthenticated()).andExpect(redirectedUrl("/login?error=true"));
        var user = createUser("disabled@example.com", false);
        mvc.perform(post("/login").with(csrf()).param("email", user.getEmail()).param("password", "123456"))
            .andExpect(unauthenticated()).andExpect(redirectedUrl("/login?error=true"));
    }
    @Test void ordinaryUserCannotOpenDashboard() throws Exception {
        var user = createUser("member@example.com", true);
        var result = mvc.perform(post("/login").with(csrf()).param("email", user.getEmail()).param("password", "123456"))
            .andExpect(authenticated()).andExpect(redirectedUrl("/")).andReturn();
        var session = (MockHttpSession) result.getRequest().getSession(false);
        mvc.perform(get("/").session(session)).andExpect(status().isOk()).andExpect(content().string(containsString("Thành viên")));
        mvc.perform(get("/dashboard").session(session)).andExpect(status().isForbidden());
        mvc.perform(get("/access-denied").session(session)).andExpect(status().isForbidden())
            .andExpect(content().string(containsString("Không có quyền truy cập")));
    }
    private User createUser(String email, boolean enabled) {
        User user = new User();
        user.setEmail(email);
        
        user.setFullName("Thành viên");
        user.setPassword(encoder.encode("123456"));
        user.setEnabled(enabled);
        user.setRole(roles.findByNameIgnoreCase("USER").orElseThrow());
        return users.save(user);
    }
    
}
