package aleks.learningplatform.web.dto.assignment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateAssignmentRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Integer maxScore;
}
