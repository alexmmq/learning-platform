package aleks.learningplatform.web.dto.course;

import lombok.Data;

@Data
public class CreateModuleRequest {
    private String title;
    private Integer orderIndex;
}
