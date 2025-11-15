package aleks.learningplatform;

import aleks.learningplatform.domain.course.Course;
import aleks.learningplatform.domain.user.User;
import aleks.learningplatform.domain.user.UserRole;
import aleks.learningplatform.repository.CourseRepository;
import aleks.learningplatform.repository.UserRepository;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.jpa.open-in-view=false"
})
class LazyLoadingTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Test
    void lazyCollectionAccessOutsideTransaction_throwsException() {
        User teacher = new User();
        teacher.setName("Lazy Teacher");
        teacher.setEmail("lazy.teacher@example.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        Course course = new Course();
        course.setTitle("Lazy course");
        course.setTeacher(teacher);
        course = courseRepository.save(course);

        Long courseId = course.getId();
        Course detached = loadCourseInTransactionalMethod(courseId);

        assertThrows(LazyInitializationException.class,
                () -> detached.getModules().size());
    }

    @Transactional
    protected Course loadCourseInTransactionalMethod(Long id) {
        return courseRepository.findById(id).orElseThrow();
    }
}

