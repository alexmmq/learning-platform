package aleks.learningplatform.service;

import aleks.learningplatform.domain.content.Lesson;
import aleks.learningplatform.domain.content.Module;
import aleks.learningplatform.domain.course.Category;
import aleks.learningplatform.domain.course.Course;
import aleks.learningplatform.domain.course.Tag;
import aleks.learningplatform.domain.user.User;
import aleks.learningplatform.repository.*;
import aleks.learningplatform.web.dto.course.CreateCourseRequest;
import aleks.learningplatform.web.dto.course.CreateLessonRequest;
import aleks.learningplatform.web.dto.course.CreateModuleRequest;
import aleks.learningplatform.web.dto.course.UpdateCourseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    public Course createCourse(CreateCourseRequest req) {
        User teacher = userRepository.findById(req.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        Course course = new Course();
        course.setTitle(req.getTitle());
        course.setDescription(req.getDescription());
        course.setTeacher(teacher);
        course.setDurationInHours(req.getDurationInHours());
        course.setStartDate(req.getStartDate());

        if (req.getCategoryId() != null) {
            Category cat = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            course.setCategory(cat);
        }

        if (req.getTags() != null) {
            for (String tagName : req.getTags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            Tag t = new Tag();
                            t.setName(tagName);
                            return tagRepository.save(t);
                        });
                course.getTags().add(tag);
            }
        }
        return courseRepository.save(course);
    }

    @Transactional(readOnly = true)
    public Course getCourseWithStructure(Long id) {
        // простой вариант — просто findById, коллекции ленивые
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }

    @Transactional(readOnly = true)
    public List<Course> findCourses(String category, String tag) {
        if (category != null) {
            return courseRepository.findByCategory_Name(category);
        }
        return courseRepository.findAll();
    }

    @Transactional
    public Course updateCourse(Long id, UpdateCourseRequest req) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        course.setTitle(req.getTitle());
        course.setDescription(req.getDescription());
        course.setDurationInHours(req.getDurationInHours());
        course.setStartDate(req.getStartDate());

        if (req.getCategoryId() != null) {
            Category cat = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            course.setCategory(cat);
        }

        // пересобираем теги
        course.getTags().clear();
        if (req.getTags() != null) {
            for (String tagName : req.getTags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            Tag t = new Tag();
                            t.setName(tagName);
                            return tagRepository.save(t);
                        });
                course.getTags().add(tag);
            }
        }
        return course;
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Transactional
    public aleks.learningplatform.domain.content.Module addModule(Long courseId, CreateModuleRequest req) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        aleks.learningplatform.domain.content.Module module = new aleks.learningplatform.domain.content.Module();
        module.setTitle(req.getTitle());
        module.setOrderIndex(req.getOrderIndex());
        module.setCourse(course);

        course.getModules().add(module);
        return moduleRepository.save(module);
    }

    @Transactional
    public Lesson addLesson(Long moduleId, CreateLessonRequest req) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module not found"));

        Lesson lesson = new Lesson();
        lesson.setTitle(req.getTitle());
        lesson.setContent(req.getContent());
        lesson.setVideoUrl(req.getVideoUrl());
        lesson.setModule(module);

        module.getLessons().add(lesson);
        return lessonRepository.save(lesson);
    }
}
