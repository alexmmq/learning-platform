package aleks.learningplatform.web.controller;

import aleks.learningplatform.domain.quiz.Quiz;
import aleks.learningplatform.domain.quiz.QuizSubmission;
import aleks.learningplatform.service.QuizService;
import aleks.learningplatform.web.dto.quiz.QuizDto;
import aleks.learningplatform.web.dto.quiz.QuizSubmissionDto;
import aleks.learningplatform.web.dto.quiz.TakeQuizRequest;
import aleks.learningplatform.web.mapper.QuizMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final QuizMapper quizMapper;

    @PostMapping("/modules/{moduleId}/quiz")
    public QuizDto createOrUpdate(@PathVariable Long moduleId,
                                  @RequestBody QuizDto dto) {
        Quiz quiz = quizService.createOrUpdateQuiz(moduleId, dto);
        return quizMapper.toQuizDto(quiz, true); // для преподавателя
    }

    @GetMapping("/quizzes/{id}")
    public QuizDto getQuiz(@PathVariable Long id) {
        Quiz quiz = quizService.getQuizWithQuestions(id);
        return quizMapper.toQuizDto(quiz, false); // студенту без correct
    }

    @PostMapping("/quizzes/{id}/take")
    public QuizSubmissionDto takeQuiz(@PathVariable Long id,
                                      @RequestBody TakeQuizRequest req) {
        Map<Long, Set<Long>> answers = req.getAnswersByQuestionId().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new HashSet<>(e.getValue())
                ));
        QuizSubmission sub =
                quizService.takeQuiz(id, req.getStudentId(), answers);
        return quizMapper.toSubmissionDto(sub);
    }

    @GetMapping("/quizzes/{id}/results")
    public List<QuizSubmissionDto> results(@PathVariable Long id) {
        return quizService.getQuizSubmissions(id).stream()
                .map(quizMapper::toSubmissionDto)
                .toList();
    }
}

