package aleks.learningplatform.domain.quiz;

import aleks.learningplatform.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_submissions",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"quiz_id", "student_id"}))
@Data
public class QuizSubmission {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private Integer score;

    private LocalDateTime takenAt;
}

