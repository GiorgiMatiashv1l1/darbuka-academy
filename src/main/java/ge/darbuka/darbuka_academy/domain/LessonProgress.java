package ge.darbuka.darbuka_academy.domain;
import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "lesson_progress")
@Getter
@Setter
@NoArgsConstructor
public class LessonProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    @Column(name = "last_position_seconds", nullable = false)
    private Integer lastPositionSeconds = 0;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    void touch() {
        this.updatedAt = Instant.now();
    }

}
