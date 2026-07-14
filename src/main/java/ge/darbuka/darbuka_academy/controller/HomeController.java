package ge.darbuka.darbuka_academy.controller;


import ge.darbuka.darbuka_academy.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CourseRepository courses;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("courses", courses.findByActiveTrueOrderByPriceCentsAsc());
        return "index";
    }
}
