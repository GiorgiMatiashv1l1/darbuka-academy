package ge.darbuka.darbuka_academy.controller;


import ge.darbuka.darbuka_academy.domain.Course;
import ge.darbuka.darbuka_academy.domain.CourseLevel;
import ge.darbuka.darbuka_academy.domain.Lesson;
import ge.darbuka.darbuka_academy.domain.User;
import ge.darbuka.darbuka_academy.repository.CourseRepository;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courses;
    private final LessonRepository lessons;
    private final AccessService access;
    private final CurrentUserService currentUser;


    @GetMapping("/courses/{level}")
    public String course(@PathVariable CourseLevel level,
                         @AuthenticationPrincipal UserDetails principal,
                         Model model){

        Course course = courses.findByLevel(level).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<Lesson> lessonList = lessons.findAllByCourseIdOrderByPositionAsc(course.getId());
        User user = currentUser.resolve(principal);

        Map<Long, Boolean> unlocked = new LinkedHashMap<>();
        for(Lesson lesson : lessonList){
            unlocked.put(lesson.getId(), access.canAccess(user, lesson));
        }

        model.addAttribute("course", course);
        model.addAttribute("lessons", lessonList);
        model.addAttribute("unlocked", unlocked);
        model.addAttribute("owned", access.owns(user, course.getId()));

        return "course";
    }
}
