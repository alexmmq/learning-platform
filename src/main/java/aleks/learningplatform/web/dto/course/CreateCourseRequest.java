package aleks.learningplatform.web.dto.course;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateCourseRequest {
    private String title;
    private String description;
    private Long categoryId;
    private Long teacherId;
    private Integer durationInHours;
    private LocalDate startDate;
    private List<String> tags;
}
