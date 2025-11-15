package aleks.learningplatform;


import aleks.learningplatform.domain.content.Lesson;
import aleks.learningplatform.domain.content.Module;
import aleks.learningplatform.domain.course.Category;
import aleks.learningplatform.domain.course.Course;
import aleks.learningplatform.domain.user.User;
import aleks.learningplatform.domain.user.UserRole;
import aleks.learningplatform.repository.CategoryRepository;
import aleks.learningplatform.repository.CourseRepository;
import aleks.learningplatform.repository.LessonRepository;
import aleks.learningplatform.repository.ModuleRepository;
import aleks.learningplatform.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseCrudIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private LessonRepository lessonRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    void createCourseWithModulesAndLessonsCascadePersistWorks() {
        User teacher = new User();
        teacher.setName("Teacher CRUD");
        teacher.setEmail("teacher.crud@example.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        Category cat = new Category();
        cat.setName("Databases");
        cat = categoryRepository.save(cat);

        Course course = new Course();
        course.setTitle("Hibernate CRUD Test");
        course.setDescription("Проверка каскадов");
        course.setTeacher(teacher);
        course.setCategory(cat);
        course.setStartDate(LocalDate.now());
        course = courseRepository.save(course);

        Module m1 = new Module();
        m1.setTitle("Модуль CRUD 1");
        m1.setOrderIndex(1);
        m1.setCourse(course);

        Lesson l11 = new Lesson();
        l11.setTitle("Урок CRUD 1.1");
        l11.setContent("Content CRUD 1.1");
        l11.setModule(m1);

        m1.getLessons().add(l11);
        course.getModules().add(m1);

        Course saved = courseRepository.save(course);

        em.flush();
        em.clear();

        Course found = courseRepository.findById(saved.getId())
                .orElseThrow();

        assertEquals(1, found.getModules().size());
        Module foundModule = found.getModules().get(0);
        assertEquals(1, foundModule.getLessons().size());
        assertEquals("Урок CRUD 1.1", foundModule.getLessons().get(0).getTitle());
    }

    @Test
    @Transactional
    void updateCourseDescriptionPersisted() {
        User teacher = new User();
        teacher.setName("Teacher Update");
        teacher.setEmail("teacher.update@example.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        Course course = new Course();
        course.setTitle("Update Test");
        course.setDescription("Old description");
        course.setTeacher(teacher);
        course = courseRepository.save(course);

        Long id = course.getId();
        course.setDescription("New description");
        em.flush();
        em.clear();

        Course updated = courseRepository.findById(id).orElseThrow();
        assertEquals("New description", updated.getDescription());
    }

    @Test
    @Transactional
    void deleteCourseCascadesToModulesAndLessons() {
        User teacher = new User();
        teacher.setName("Teacher Delete");
        teacher.setEmail("teacher.delete@example.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        Course course = new Course();
        course.setTitle("Delete Test");
        course.setTeacher(teacher);
        course = courseRepository.save(course);

        Module module = new Module();
        module.setTitle("Module for delete");
        module.setCourse(course);
        module = moduleRepository.save(module);

        Lesson lesson = new Lesson();
        lesson.setTitle("Lesson for delete");
        lesson.setModule(module);
        lesson = lessonRepository.save(lesson);

        Long courseId = course.getId();
        Long moduleId = module.getId();
        Long lessonId = lesson.getId();

        courseRepository.deleteById(courseId);
        em.flush();
        em.clear();

        assertFalse(courseRepository.findById(courseId).isPresent());
        assertFalse(moduleRepository.findById(moduleId).isPresent());
        assertFalse(lessonRepository.findById(lessonId).isPresent());
    }
}
