package aleks.learningplatform.web.dto.quiz;


import lombok.Data;

import java.util.List;

@Data
public class QuizDto {
    private Long id;
    private String title;
    private Integer timeLimitMinutes;
    private Long moduleId;
    private List<QuestionDto> questions;
}
