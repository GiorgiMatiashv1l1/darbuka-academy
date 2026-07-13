package ge.darbuka.darbuka_academy.repository;

import ge.darbuka.darbuka_academy.domain.Course;
import ge.darbuka.darbuka_academy.domain.CourseLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByLevel(CourseLevel level);
    List<Course> findByActiveTrueOrderByPriceCentsAsc();
}
