package aleks.learningplatform.web.dto.quiz;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TakeQuizRequest {
    private Long studentId;
    private Map<Long, List<Long>> answersByQuestionId;
}

