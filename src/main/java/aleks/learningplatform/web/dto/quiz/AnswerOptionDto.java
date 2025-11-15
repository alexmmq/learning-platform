package aleks.learningplatform.web.dto.quiz;

import lombok.Data;

@Data
public class AnswerOptionDto {
    private Long id;
    private String text;
    private boolean correct;
}
