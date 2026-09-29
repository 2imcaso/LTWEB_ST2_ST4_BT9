package security;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import security.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:VD3FLOW;DB_CLOSE_DELAY=-1","spring.jpa.hibernate.ddl-auto=create-drop","app.mail.enabled=false"})
@AutoConfigureMockMvc class Example3IntegrationTests {
 @Autowired MockMvc mvc; @Autowired UserRepository users;
 @Test void publicFormsRenderAndRegisterOtpActivatesAccount()throws Exception{
  mvc.perform(get("/register")).andExpect(status().isOk()).andExpect(content().string(containsString("Đăng ký và nhận OTP")));
  mvc.perform(get("/forgot-password")).andExpect(status().isOk());
  var result=mvc.perform(post("/register").with(csrf()).param("username","newuser").param("email","new@example.com").param("fullName","Người dùng mới").param("password","123456").param("confirmPassword","123456")).andExpect(redirectedUrl("/verify-otp?email=new@example.com")).andReturn();
  String otp=(String)result.getFlashMap().get("developmentOtp"); assertThat(otp).matches("\\d{6}"); assertThat(users.findByEmailIgnoreCase("new@example.com").orElseThrow().isEnabled()).isFalse();
  mvc.perform(post("/verify-otp").with(csrf()).param("email","new@example.com").param("otp",otp)).andExpect(redirectedUrl("/login"));
  assertThat(users.findByEmailIgnoreCase("new@example.com").orElseThrow().isEnabled()).isTrue();
 }
}
