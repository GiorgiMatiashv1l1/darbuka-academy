package ge.darbuka.darbuka_academy.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "duration_seconds", nullable = false)
    private Integer durationSeconds = 0;

    @Column(nullable = false)
    private Integer position;

    @Column(name = "is_free_preview", nullable = false)
    private boolean FreePreview = false;
}
