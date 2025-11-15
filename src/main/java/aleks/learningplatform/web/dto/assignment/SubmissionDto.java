package aleks.learningplatform.web.dto.assignment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmissionDto {
    private Long id;
    private Long assignmentId;
    private Long studentId;
    private String studentName;
    private LocalDateTime submittedAt;
    private String content;
    private Integer score;
    private String feedback;
}
