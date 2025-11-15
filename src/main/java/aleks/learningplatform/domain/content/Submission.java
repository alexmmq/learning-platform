package aleks.learningplatform.domain.content;

import aleks.learningplatform.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"assignment_id", "student_id"}))
@Data
public class Submission {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private LocalDateTime submittedAt;

    @Column(length = 8000)
    private String content;

    private Integer score;

    @Column(length = 4000)
    private String feedback;
}

