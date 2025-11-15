package aleks.learningplatform.service;

import aleks.learningplatform.domain.course.Course;
import aleks.learningplatform.domain.course.Enrollment;
import aleks.learningplatform.domain.user.User;
import aleks.learningplatform.repository.CourseRepository;
import aleks.learningplatform.repository.EnrollmentRepository;
import aleks.learningplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Enrollment enrollStudent(Long courseId, Long studentId) {
        boolean exists = enrollmentRepository.findByStudentId(studentId).stream()
                .anyMatch(e -> e.getCourse().getId().equals(courseId));
        if (exists) {
            throw new IllegalStateException("Student already enrolled");
        }

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDate.now());
        enrollment.setStatus("ACTIVE");
        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void unenrollStudent(Long courseId, Long studentId) {
        enrollmentRepository.findByStudentId(studentId).stream()
                .filter(e -> e.getCourse().getId().equals(courseId))
                .findFirst()
                .ifPresent(enrollmentRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> findByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }
}

