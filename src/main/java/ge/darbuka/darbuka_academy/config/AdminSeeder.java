package ge.darbuka.darbuka_academy.config;

import ge.darbuka.darbuka_academy.domain.Role;
import ge.darbuka.darbuka_academy.domain.User;
import ge.darbuka.darbuka_academy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner seedAdmin(UserRepository users,
                                PasswordEncoder encoder,
                                @Value("${app.admin.email}") String email,
                                @Value("${app.admin.password}") String password) {
        return args -> {
            if(users.existsByEmail(email)){
                return;
            }
            User admin = new User();
            admin.setEmail(email);
            admin.setPasswordHash(encoder.encode(password));
            admin.setFullName("Admin");
            admin.setRole(Role.ADMIN);
            users.save(admin);
        };
    }
}
