package aleks.learningplatform.web.dto.course;

import lombok.Data;

@Data
public class CreateLessonRequest {
    private String title;
    private String content;
    private String videoUrl;
}
