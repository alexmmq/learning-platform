package aleks.learningplatform.web.dto.quiz;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizSubmissionDto {
    private Long id;
    private Long quizId;
    private Long studentId;
    private String studentName;
    private Integer score;
    private LocalDateTime takenAt;
}
