package aleks.learningplatform.web.dto.assignment;

import lombok.Data;

@Data
public class SubmitAssignmentRequest {
    private Long studentId;
    private String content;
}
