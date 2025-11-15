package aleks.learningplatform.web.dto.course;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EnrollmentDto {
    private Long id;
    private Long courseId;
    private Long studentId;
    private String studentName;
    private LocalDate enrollDate;
    private String status;
}
