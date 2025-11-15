package aleks.learningplatform.service;

import aleks.learningplatform.domain.content.Module;
import aleks.learningplatform.domain.quiz.AnswerOption;
import aleks.learningplatform.domain.quiz.Question;
import aleks.learningplatform.domain.quiz.Quiz;
import aleks.learningplatform.domain.quiz.QuizSubmission;
import aleks.learningplatform.domain.user.User;
import aleks.learningplatform.repository.ModuleRepository;
import aleks.learningplatform.repository.QuizRepository;
import aleks.learningplatform.repository.QuizSubmissionRepository;
import aleks.learningplatform.repository.UserRepository;
import aleks.learningplatform.web.dto.quiz.AnswerOptionDto;
import aleks.learningplatform.web.dto.quiz.QuestionDto;
import aleks.learningplatform.web.dto.quiz.QuizDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public Quiz createOrUpdateQuiz(Long moduleId, QuizDto dto) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module not found"));

        Quiz quiz = module.getQuiz();
        if (quiz == null) {
            quiz = new Quiz();
            quiz.setModule(module);
        }

        quiz.setTitle(dto.getTitle());
        quiz.setTimeLimitMinutes(dto.getTimeLimitMinutes());

        // пересоздаём вопросы
        quiz.getQuestions().clear();
        if (dto.getQuestions() != null) {
            for (QuestionDto qDto : dto.getQuestions()) {
                Question q = new Question();
                q.setQuiz(quiz);
                q.setText(qDto.getText());
                q.setType(qDto.getType());

                for (AnswerOptionDto optDto : qDto.getOptions()) {
                    AnswerOption opt = new AnswerOption();
                    opt.setQuestion(q);
                    opt.setText(optDto.getText());
                    opt.setCorrect(optDto.isCorrect());
                    q.getOptions().add(opt);
                }
                quiz.getQuestions().add(q);
            }
        }
        module.setQuiz(quiz);
        return quizRepository.save(quiz);
    }

    @Transactional(readOnly = true)
    public Quiz getQuizWithQuestions(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
    }

    @Transactional
    public QuizSubmission takeQuiz(Long quizId, Long studentId,
                                   Map<Long, Set<Long>> answersByQuestionId) {
        Quiz quiz = getQuizWithQuestions(quizId);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        int total = quiz.getQuestions().size();
        int correct = 0;

        for (Question q : quiz.getQuestions()) {
            Set<Long> selected = answersByQuestionId
                    .getOrDefault(q.getId(), Set.of());
            Set<Long> correctIds = q.getOptions().stream()
                    .filter(AnswerOption::isCorrect)
                    .map(AnswerOption::getId)
                    .collect(Collectors.toSet());
            if (selected.equals(correctIds)) {
                correct++;
            }
        }
        int score = total == 0 ? 0 : (int) Math.round(100.0 * correct / total);

        QuizSubmission sub = new QuizSubmission();
        sub.setQuiz(quiz);
        sub.setStudent(student);
        sub.setScore(score);
        sub.setTakenAt(LocalDateTime.now());
        return quizSubmissionRepository.save(sub);
    }

    @Transactional(readOnly = true)
    public List<QuizSubmission> getQuizSubmissions(Long quizId) {
        return quizSubmissionRepository.findByQuizId(quizId);
    }
}

