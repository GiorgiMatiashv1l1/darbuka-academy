package ge.darbuka.darbuka_academy.controller;


import ge.darbuka.darbuka_academy.domain.Course;
import ge.darbuka.darbuka_academy.domain.User;
import ge.darbuka.darbuka_academy.repository.CourseRepository;
import ge.darbuka.darbuka_academy.services.AccessService;
import ge.darbuka.darbuka_academy.services.CheckoutService;
import ge.darbuka.darbuka_academy.services.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CourseRepository courses;
    private final CheckoutService checkout;
    private final CurrentUserService currentUser;
    private final AccessService access;

    @PostMapping("/checkout/{courseId}")
    public String checkout(@PathVariable Long courseId,
                           @AuthenticationPrincipal UserDetails principal) throws Exception{

        User user = currentUser.resolve(principal);
        if(user == null){
            return "redirect:/login";
        }

        Course course = courses.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (access.owns(user, course.getId())){
            return "redirect:/courses/" + course.getLevel();
        }

        String url = checkout.createSession(user, course);
        return "redirect:" + url;
    }
}
