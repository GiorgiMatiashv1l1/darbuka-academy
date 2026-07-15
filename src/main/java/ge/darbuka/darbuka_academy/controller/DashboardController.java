package ge.darbuka.darbuka_academy.controller;

import ge.darbuka.darbuka_academy.domain.Course;
import ge.darbuka.darbuka_academy.domain.Purchase;
import ge.darbuka.darbuka_academy.domain.PurchaseStatus;
import ge.darbuka.darbuka_academy.domain.User;
import ge.darbuka.darbuka_academy.repository.PurchaseRepository;
import ge.darbuka.darbuka_academy.services.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final PurchaseRepository purchases;
    private final CurrentUserService currentUser;

    @GetMapping("/my-courses")
    public String myCourses(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = currentUser.resolve(principal);

        List<Purchase> paid = purchases.findAllByUserIdAndStatusWithCourse(
                user.getId(), PurchaseStatus.PAID);

        List<Course> courses = paid.stream().map(Purchase::getCourse).toList();

        model.addAttribute("courses", courses);
        return "my-courses";
    }

    @GetMapping("/checkout/success")
    public String success(@RequestParam(required = false) String session_id, Model model) {
        // The webhook does the real work. This page just confirms.
        return "checkout-success";
    }
}