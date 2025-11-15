package aleks.learningplatform.web.dto.course;

import lombok.Data;

@Data
public class LessonDto {
    private Long id;
    private String title;
    private String content;
    private String videoUrl;
}
