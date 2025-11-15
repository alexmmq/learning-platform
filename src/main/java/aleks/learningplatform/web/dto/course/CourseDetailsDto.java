package aleks.learningplatform.web.dto.course;

import lombok.Data;

import java.util.List;

@Data
public class CourseDetailsDto {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String teacherName;
    private List<String> tags;
    private List<ModuleDto> modules;
    private Double averageRating;
}
