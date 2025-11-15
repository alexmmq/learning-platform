package aleks.learningplatform.web.dto.course;

import lombok.Data;

import java.util.List;

@Data
public class ModuleDto {
    private Long id;
    private String title;
    private Integer orderIndex;
    private List<LessonDto> lessons;
}
