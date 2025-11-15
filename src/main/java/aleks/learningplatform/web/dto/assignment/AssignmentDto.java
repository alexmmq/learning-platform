package aleks.learningplatform.web.dto.assignment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignmentDto {
    private Long id;
    private Long lessonId;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Integer maxScore;
}