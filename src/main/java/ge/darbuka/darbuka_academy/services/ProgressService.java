package ge.darbuka.darbuka_academy.services;

import ge.darbuka.darbuka_academy.domain.Lesson;
import ge.darbuka.darbuka_academy.domain.LessonProgress;
import ge.darbuka.darbuka_academy.domain.User;
import ge.darbuka.darbuka_academy.repository.LessonProgressRepository;
import ge.darbuka.darbuka_academy.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final LessonProgressRepository progressRepo;
    private final LessonRepository lessons;

    @Transactional
    public void toggleComplete(User user, Long lessonId){
        LessonProgress progress = progressRepo
                .findByUserIdAndLessonId(user.getId(), lessonId)
                .orElseGet(() -> {
                    Lesson lesson  = lessons.findById(lessonId).orElseThrow();
                    LessonProgress p = new LessonProgress();
                    p.setUser(user);
                    p.setLesson(lesson);
                    return p;
                });

        progress.setCompleted(!progress.isCompleted());
        progress.setUpdatedAt(Instant.now());
        progressRepo.save(progress);
    }
}
