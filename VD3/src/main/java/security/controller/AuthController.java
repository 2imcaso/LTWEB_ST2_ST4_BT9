package security.controller;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import security.dto.*;
import security.service.AuthService;
@Controller
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth){this.auth=auth;}
    @GetMapping("/login")
    public String login() { return "auth/login"; }
    @GetMapping("/register") public String register(Model m){if(!m.containsAttribute("registerDTO"))m.addAttribute("registerDTO",new RegisterDTO());return "auth/register";}
    @PostMapping("/register") public String register(@Valid @ModelAttribute RegisterDTO d,BindingResult b,RedirectAttributes r){if(b.hasErrors()){r.addFlashAttribute("org.springframework.validation.BindingResult.registerDTO",b);r.addFlashAttribute("registerDTO",d);return "redirect:/register";}try{String code=auth.register(d);r.addFlashAttribute("success","Đã gửi OTP đến email.");if(!auth.mailEnabled())r.addFlashAttribute("developmentOtp",code);return "redirect:/verify-otp?email="+d.getEmail();}catch(IllegalArgumentException e){b.reject("register",e.getMessage());r.addFlashAttribute("org.springframework.validation.BindingResult.registerDTO",b);r.addFlashAttribute("registerDTO",d);return "redirect:/register";}}
    @GetMapping("/verify-otp") public String verify(@RequestParam(defaultValue="")String email,Model m){if(!m.containsAttribute("verifyOtpDTO")){var d=new VerifyOtpDTO();d.setEmail(email);m.addAttribute("verifyOtpDTO",d);}return "auth/verify-otp";}
    @PostMapping("/verify-otp") public String verify(@Valid @ModelAttribute VerifyOtpDTO d,BindingResult b,RedirectAttributes r){if(!b.hasErrors())try{auth.verifyRegister(d.getEmail(),d.getOtp());r.addFlashAttribute("success","Xác nhận thành công. Bạn có thể đăng nhập.");return "redirect:/login";}catch(IllegalArgumentException e){b.reject("otp",e.getMessage());}r.addFlashAttribute("org.springframework.validation.BindingResult.verifyOtpDTO",b);r.addFlashAttribute("verifyOtpDTO",d);return "redirect:/verify-otp?email="+d.getEmail();}
    @PostMapping("/resend-register-otp") public String resend(@RequestParam String email,RedirectAttributes r){try{String code=auth.resendRegister(email);r.addFlashAttribute("success","Đã gửi lại OTP.");if(!auth.mailEnabled())r.addFlashAttribute("developmentOtp",code);}catch(IllegalArgumentException e){r.addFlashAttribute("error",e.getMessage());}return "redirect:/verify-otp?email="+email;}
    @GetMapping("/forgot-password") public String forgot(Model m){if(!m.containsAttribute("forgotPasswordDTO"))m.addAttribute("forgotPasswordDTO",new ForgotPasswordDTO());return "auth/forgot-password";}
    @PostMapping("/forgot-password") public String forgot(@Valid @ModelAttribute ForgotPasswordDTO d,BindingResult b,RedirectAttributes r){if(!b.hasErrors())try{String code=auth.forgot(d.getEmail());r.addFlashAttribute("success","Đã gửi OTP đặt lại mật khẩu.");if(!auth.mailEnabled())r.addFlashAttribute("developmentOtp",code);return "redirect:/reset-password?email="+d.getEmail();}catch(IllegalArgumentException e){b.reject("forgot",e.getMessage());}r.addFlashAttribute("org.springframework.validation.BindingResult.forgotPasswordDTO",b);r.addFlashAttribute("forgotPasswordDTO",d);return "redirect:/forgot-password";}
    @GetMapping("/reset-password") public String reset(@RequestParam(defaultValue="")String email,Model m){if(!m.containsAttribute("resetPasswordDTO")){var d=new ResetPasswordDTO();d.setEmail(email);m.addAttribute("resetPasswordDTO",d);}return "auth/reset-password";}
    @PostMapping("/reset-password") public String reset(@Valid @ModelAttribute ResetPasswordDTO d,BindingResult b,RedirectAttributes r){if(!b.hasErrors())try{auth.reset(d.getEmail(),d.getOtp(),d.getPassword(),d.getConfirmPassword());r.addFlashAttribute("success","Đổi mật khẩu thành công.");return "redirect:/login";}catch(IllegalArgumentException e){b.reject("reset",e.getMessage());}r.addFlashAttribute("org.springframework.validation.BindingResult.resetPasswordDTO",b);r.addFlashAttribute("resetPasswordDTO",d);return "redirect:/reset-password?email="+d.getEmail();}
}
