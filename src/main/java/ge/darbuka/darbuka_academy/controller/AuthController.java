package ge.darbuka.darbuka_academy.controller;


import ge.darbuka.darbuka_academy.domain.Role;
import ge.darbuka.darbuka_academy.domain.User;
import ge.darbuka.darbuka_academy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    @GetMapping("/login")
        public String login(){
            return "login";
        }

    @GetMapping("/register")
    public String registerForm(){
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String fullName,
                           @RequestParam String password,
                           Model model) {
        if (users.existsByEmail(email)){
            model.addAttribute("error", "that email is already registered.");
            return "register";
        }

        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPasswordHash(encoder.encode(password));
        user.setRole(Role.USER);
        users.save(user);

        return "redirect:/login?registered";
    }
}
