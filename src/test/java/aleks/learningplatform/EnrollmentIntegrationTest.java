package aleks.learningplatform;

import aleks.learningplatform.domain.course.Course;
import aleks.learningplatform.domain.course.Enrollment;
import aleks.learningplatform.domain.user.User;
import aleks.learningplatform.domain.user.UserRole;
import aleks.learningplatform.repository.CourseRepository;
import aleks.learningplatform.repository.EnrollmentRepository;
import aleks.learningplatform.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EnrollmentIntegrationTest {

    @Autowired private UserRepository userRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private EnrollmentRepository enrollmentRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    void enrollAndUnenrollStudent() {
        User teacher = new User();
        teacher.setName("Teacher Enroll");
        teacher.setEmail("teacher.enroll@example.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        User student = new User();
        student.setName("Student Enroll");
        student.setEmail("student.enroll@example.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        Course course = new Course();
        course.setTitle("Enrollment course");
        course.setTeacher(teacher);
        course = courseRepository.save(course);

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDate.now());
        enrollment.setStatus("ACTIVE");
        enrollment = enrollmentRepository.save(enrollment);

        em.flush();
        em.clear();

        List<Enrollment> byStudent = enrollmentRepository.findByStudentId(student.getId());
        assertEquals(1, byStudent.size());
        assertEquals(course.getId(), byStudent.get(0).getCourse().getId());

        // удаление
        enrollmentRepository.delete(byStudent.get(0));
        em.flush();
        em.clear();

        assertTrue(enrollmentRepository.findByStudentId(student.getId()).isEmpty());
    }
}

