package ge.darbuka.darbuka_academy.repository;

import ge.darbuka.darbuka_academy.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByCourseIdOrderByPositionAsc(Long courseId);

    @Query("SELECT l FROM Lesson l JOIN FETCH l.course WHERE l.id = :id")
    Optional<Lesson> findByIdWithCourse(@Param("id") Long id);
}
