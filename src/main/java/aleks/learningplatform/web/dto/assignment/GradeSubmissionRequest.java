package aleks.learningplatform.web.dto.assignment;

import lombok.Data;

@Data
public class GradeSubmissionRequest {
    private Integer score;
    private String feedback;
}
