package aleks.learningplatform.web.dto.course;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateCourseRequest {
    private String title;
    private String description;
    private Long categoryId;
    private Integer durationInHours;
    private LocalDate startDate;
    private List<String> tags;
}