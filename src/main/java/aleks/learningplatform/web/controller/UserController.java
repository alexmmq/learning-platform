package aleks.learningplatform.web.controller;

import aleks.learningplatform.domain.course.Enrollment;
import aleks.learningplatform.domain.user.User;
import aleks.learningplatform.repository.EnrollmentRepository;
import aleks.learningplatform.repository.QuizSubmissionRepository;
import aleks.learningplatform.repository.SubmissionRepository;
import aleks.learningplatform.service.UserService;
import aleks.learningplatform.web.dto.assignment.SubmissionDto;
import aleks.learningplatform.web.dto.course.CourseSummaryDto;
import aleks.learningplatform.web.dto.quiz.QuizSubmissionDto;
import aleks.learningplatform.web.dto.user.CreateUserRequest;
import aleks.learningplatform.web.dto.user.UserDto;
import aleks.learningplatform.web.mapper.AssignmentMapper;
import aleks.learningplatform.web.mapper.CourseMapper;
import aleks.learningplatform.web.mapper.QuizMapper;
import aleks.learningplatform.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final EnrollmentRepository enrollmentRepository;
    private final SubmissionRepository submissionRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final CourseMapper courseMapper;
    private final AssignmentMapper assignmentMapper;
    private final QuizMapper quizMapper;

    @PostMapping
    public UserDto create(@RequestBody CreateUserRequest req) {
        User user = userMapper.fromCreate(req);
        return userMapper.toDto(userService.create(user));
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return userMapper.toDto(userService.get(id));
    }

    @GetMapping("/{id}/courses")
    public List<CourseSummaryDto> getCourses(@PathVariable Long id) {
        return enrollmentRepository.findByStudentId(id).stream()
                .map(Enrollment::getCourse)
                .distinct()
                .map(courseMapper::toSummaryDto)
                .toList();
    }

    @GetMapping("/{id}/submissions")
    public List<SubmissionDto> getSubmissions(@PathVariable Long id) {
        return submissionRepository.findByStudentId(id).stream()
                .map(assignmentMapper::toSubmissionDto)
                .toList();
    }

    @GetMapping("/{id}/quiz-results")
    public List<QuizSubmissionDto> getQuizResults(@PathVariable Long id) {
        return quizSubmissionRepository.findByStudentId(id).stream()
                .map(quizMapper::toSubmissionDto)
                .toList();
    }
}

