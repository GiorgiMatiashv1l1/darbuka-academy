package ge.darbuka.darbuka_academy.controller;


import ge.darbuka.darbuka_academy.domain.Course;
import ge.darbuka.darbuka_academy.domain.CourseLevel;
import ge.darbuka.darbuka_academy.domain.Lesson;
import ge.darbuka.darbuka_academy.domain.User;
import ge.darbuka.darbuka_academy.repository.LessonRepository;
import ge.darbuka.darbuka_academy.services.AccessService;
import ge.darbuka.darbuka_academy.services.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LessonController {
    private final LessonRepository lessons;
    private final AccessService access;
    private final CurrentUserService currentUser;

    @GetMapping("/courses/{level}/lessons/{id}")
    public String lesson(@PathVariable CourseLevel level, @PathVariable Long id,
                         @AuthenticationPrincipal UserDetails principal, Model model){


        Lesson lesson = lessons.findByIdWithCourse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Course course = lesson.getCourse();

        if(course.getLevel() != level){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User user = currentUser.resolve(principal);

        if(!access.canAccess(user, lesson)){
            return "redirect:/courses/" + level + "?locked";
        }

        List<Lesson> siblings = lessons.findAllByCourseIdOrderByPositionAsc(course.getId());
        int index = -1;
        for(int i = 0; i < siblings.size(); i++){
            if(siblings.get(i).getId().equals(id)){
                index = i;
                break;
            }
        }

        model.addAttribute("course", course);
        model.addAttribute("lesson", lesson);
        model.addAttribute("prev", index > 0 ? siblings.get(index - 1) : null);
        model.addAttribute("next", index < siblings.size() - 1 ? siblings.get(index + 1) : null);

        return "lesson";
    }
}
