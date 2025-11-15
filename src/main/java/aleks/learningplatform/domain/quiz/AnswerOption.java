package aleks.learningplatform.domain.quiz;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "answer_options")
@Data
public class AnswerOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, nullable = false)
    private String text;

    @Column(nullable = false)
    private boolean correct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}

