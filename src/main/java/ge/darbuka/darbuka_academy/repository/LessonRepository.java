package ge.darbuka.darbuka_academy.repository;

import ge.darbuka.darbuka_academy.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByCourseIdOrderByPositionAsc(Long courseId);
}
