package aleks.learningplatform;


import aleks.learningplatform.domain.content.Assignment;
import aleks.learningplatform.domain.content.Lesson;
import aleks.learningplatform.domain.content.Submission;
import aleks.learningplatform.domain.content.Module;
import aleks.learningplatform.domain.course.Course;
import aleks.learningplatform.domain.user.User;
import aleks.learningplatform.domain.user.UserRole;
import aleks.learningplatform.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AssignmentSubmissionIntegrationTest {

    @Autowired private UserRepository userRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private ModuleRepository moduleRepository;
    @Autowired private LessonRepository lessonRepository;
    @Autowired private AssignmentRepository assignmentRepository;
    @Autowired private SubmissionRepository submissionRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    void createAndGradeSubmission() {
        User teacher = new User();
        teacher.setName("Teacher Assign");
        teacher.setEmail("teacher.assign@example.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        User student = new User();
        student.setName("Student Assign");
        student.setEmail("student.assign@example.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        Course course = new Course();
        course.setTitle("Assignments course");
        course.setTeacher(teacher);
        course = courseRepository.save(course);

        Module module = new Module();
        module.setTitle("Module A");
        module.setCourse(course);
        module = moduleRepository.save(module);

        Lesson lesson = new Lesson();
        lesson.setTitle("Lesson A");
        lesson.setModule(module);
        lesson = lessonRepository.save(lesson);

        Assignment assignment = new Assignment();
        assignment.setTitle("HW #1");
        assignment.setLesson(lesson);
        assignment.setDescription("Do something");
        assignment.setDueDate(LocalDateTime.now().plusDays(2));
        assignment.setMaxScore(100);
        assignment = assignmentRepository.save(assignment);

        Submission sub = new Submission();
        sub.setAssignment(assignment);
        sub.setStudent(student);
        sub.setSubmittedAt(LocalDateTime.now());
        sub.setContent("My work");
        sub = submissionRepository.save(sub);

        Long submissionId = sub.getId();

        // обновление (оценивание)
        sub.setScore(85);
        sub.setFeedback("Неплохо");
        em.flush();
        em.clear();

        Submission updated = submissionRepository.findById(submissionId)
                .orElseThrow();

        assertEquals(85, updated.getScore());
        assertEquals("Неплохо", updated.getFeedback());
    }
}

