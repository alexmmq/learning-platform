package aleks.learningplatform.service;

import aleks.learningplatform.domain.content.Assignment;
import aleks.learningplatform.domain.content.Lesson;
import aleks.learningplatform.domain.content.Submission;
import aleks.learningplatform.domain.user.User;
import aleks.learningplatform.repository.AssignmentRepository;
import aleks.learningplatform.repository.LessonRepository;
import aleks.learningplatform.repository.SubmissionRepository;
import aleks.learningplatform.repository.UserRepository;
import aleks.learningplatform.web.dto.assignment.CreateAssignmentRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Assignment createAssignment(Long lessonId, CreateAssignmentRequest req) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        Assignment assignment = new Assignment();
        assignment.setLesson(lesson);
        assignment.setTitle(req.getTitle());
        assignment.setDescription(req.getDescription());
        assignment.setDueDate(req.getDueDate());
        assignment.setMaxScore(req.getMaxScore());
        return assignmentRepository.save(assignment);
    }

    @Transactional(readOnly = true)
    public Assignment getAssignment(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
    }

    @Transactional
    public Submission submitAssignment(Long assignmentId, Long studentId, String content) {
        Assignment assignment = getAssignment(assignmentId);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        boolean exists = submissionRepository.findByAssignmentId(assignmentId).stream()
                .anyMatch(s -> s.getStudent().getId().equals(studentId));
        if (exists) {
            throw new IllegalStateException("Already submitted");
        }

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setContent(content);
        return submissionRepository.save(submission);
    }

    @Transactional(readOnly = true)
    public List<Submission> getSubmissionsForAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId);
    }

    @Transactional
    public Submission gradeSubmission(Long submissionId, Integer score, String feedback) {
        Submission s = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found"));
        s.setScore(score);
        s.setFeedback(feedback);
        return s;
    }
}

