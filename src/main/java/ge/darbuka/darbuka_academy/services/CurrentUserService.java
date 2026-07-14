package ge.darbuka.darbuka_academy.services;


import ge.darbuka.darbuka_academy.domain.User;
import ge.darbuka.darbuka_academy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository users;

    public User resolve(UserDetails principal){
        if(principal == null) return null;

        return users.findByEmail(principal.getUsername()).orElse(null);
    }
}
