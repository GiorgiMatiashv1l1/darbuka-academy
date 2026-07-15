package ge.darbuka.darbuka_academy.controller;

import ge.darbuka.darbuka_academy.domain.Course;
import ge.darbuka.darbuka_academy.domain.Lesson;
import ge.darbuka.darbuka_academy.repository.CourseRepository;
import ge.darbuka.darbuka_academy.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CourseRepository courses;
    private final LessonRepository lessons;

    // ---------- dashboard ----------

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("courses", courses.findAll());
        return "admin/dashboard";
    }

    // ---------- edit a course ----------

    @GetMapping("/courses/{id}")
    public String editCourse(@PathVariable Long id, Model model) {
        Course course = courses.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Lesson> lessonList = lessons.findAllByCourseIdOrderByPositionAsc(id);
        model.addAttribute("course", course);
        model.addAttribute("lessons", lessonList);
        return "admin/course";
    }

    @PostMapping("/courses/{id}")
    public String updateCourse(@PathVariable Long id,
                               @RequestParam String title,
                               @RequestParam String description,
                               @RequestParam Integer priceCents,
                               @RequestParam(required = false) String thumbnailUrl,
                               @RequestParam(required = false, defaultValue = "false") boolean active) {
        Course course = courses.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        course.setTitle(title);
        course.setDescription(description);
        course.setPriceCents(priceCents);
        course.setThumbnailUrl(thumbnailUrl);
        course.setActive(active);
        courses.save(course);
        return "redirect:/admin/courses/" + id;
    }

    // ---------- create a lesson ----------

    @PostMapping("/courses/{courseId}/lessons")
    public String createLesson(@PathVariable Long courseId,
                               @RequestParam String title,
                               @RequestParam(required = false) String description,
                               @RequestParam String videoId,
                               @RequestParam Integer durationSeconds,
                               @RequestParam Integer position,
                               @RequestParam(required = false, defaultValue = "false") boolean freePreview) {
        Course course = courses.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setTitle(title);
        lesson.setDescription(description);
        lesson.setVideoId(videoId);
        lesson.setDurationSeconds(durationSeconds);
        lesson.setPosition(position);
        lesson.setFreePreview(freePreview);
        lessons.save(lesson);

        return "redirect:/admin/courses/" + courseId;
    }

    // ---------- edit a lesson ----------

    @GetMapping("/lessons/{id}/edit")
    public String editLesson(@PathVariable Long id, Model model) {
        Lesson lesson = lessons.findByIdWithCourse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("lesson", lesson);
        return "admin/lesson";
    }

    @PostMapping("/lessons/{id}")
    public String updateLesson(@PathVariable Long id,
                               @RequestParam String title,
                               @RequestParam(required = false) String description,
                               @RequestParam String videoId,
                               @RequestParam Integer durationSeconds,
                               @RequestParam Integer position,
                               @RequestParam(required = false, defaultValue = "false") boolean freePreview) {
        Lesson lesson = lessons.findByIdWithCourse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Long courseId = lesson.getCourse().getId();

        lesson.setTitle(title);
        lesson.setDescription(description);
        lesson.setVideoId(videoId);
        lesson.setDurationSeconds(durationSeconds);
        lesson.setPosition(position);
        lesson.setFreePreview(freePreview);
        lessons.save(lesson);

        return "redirect:/admin/courses/" + courseId;
    }

    // ---------- delete a lesson ----------

    @PostMapping("/lessons/{id}/delete")
    public String deleteLesson(@PathVariable Long id) {
        Lesson lesson = lessons.findByIdWithCourse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Long courseId = lesson.getCourse().getId();
        lessons.delete(lesson);
        return "redirect:/admin/courses/" + courseId;
    }
}