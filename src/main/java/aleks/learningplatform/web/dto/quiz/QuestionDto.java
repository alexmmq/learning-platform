package aleks.learningplatform.web.dto.quiz;

import aleks.learningplatform.domain.quiz.QuestionType;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    private Long id;
    private String text;
    private QuestionType type;
    private List<AnswerOptionDto> options;
}
