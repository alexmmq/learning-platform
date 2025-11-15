package aleks.learningplatform.web.mapper;

import aleks.learningplatform.domain.quiz.Question;
import aleks.learningplatform.domain.quiz.Quiz;
import aleks.learningplatform.domain.quiz.QuizSubmission;
import aleks.learningplatform.web.dto.quiz.AnswerOptionDto;
import aleks.learningplatform.web.dto.quiz.QuestionDto;
import aleks.learningplatform.web.dto.quiz.QuizDto;
import aleks.learningplatform.web.dto.quiz.QuizSubmissionDto;
import org.springframework.stereotype.Component;

@Component
public class QuizMapper {

    public QuizDto toQuizDto(Quiz quiz, boolean includeCorrect) {
        QuizDto dto = new QuizDto();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setTimeLimitMinutes(quiz.getTimeLimitMinutes());
        dto.setModuleId(quiz.getModule().getId());
        dto.setQuestions(quiz.getQuestions().stream()
                .map(q -> toQuestionDto(q, includeCorrect))
                .toList());
        return dto;
    }

    public QuestionDto toQuestionDto(Question q, boolean includeCorrect) {
        QuestionDto dto = new QuestionDto();
        dto.setId(q.getId());
        dto.setText(q.getText());
        dto.setType(q.getType());
        dto.setOptions(q.getOptions().stream().map(opt -> {
            AnswerOptionDto od = new AnswerOptionDto();
            od.setId(opt.getId());
            od.setText(opt.getText());
            od.setCorrect(includeCorrect && opt.isCorrect());
            return od;
        }).toList());
        return dto;
    }

    public QuizSubmissionDto toSubmissionDto(QuizSubmission sub) {
        QuizSubmissionDto dto = new QuizSubmissionDto();
        dto.setId(sub.getId());
        dto.setQuizId(sub.getQuiz().getId());
        dto.setStudentId(sub.getStudent().getId());
        dto.setStudentName(sub.getStudent().getName());
        dto.setScore(sub.getScore());
        dto.setTakenAt(sub.getTakenAt());
        return dto;
    }
}

