package aleks.learningplatform.web.controller;

import aleks.learningplatform.domain.course.Enrollment;
import aleks.learningplatform.service.CourseService;
import aleks.learningplatform.service.EnrollmentService;
import aleks.learningplatform.web.dto.course.*;
import aleks.learningplatform.web.dto.user.UserDto;
import aleks.learningplatform.web.mapper.CourseMapper;
import aleks.learningplatform.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final CourseMapper courseMapper;
    private final UserMapper userMapper;

    @PostMapping
    public CourseDetailsDto create(@RequestBody CreateCourseRequest req) {
        return courseMapper.toDetailsDto(courseService.createCourse(req));
    }

    @GetMapping
    public List<CourseSummaryDto> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag) {
        return courseService.findCourses(category, tag).stream()
                .map(courseMapper::toSummaryDto)
                .toList();
    }

    @GetMapping("/{id}")
    public CourseDetailsDto get(@PathVariable Long id) {
        return courseMapper.toDetailsDto(courseService.getCourseWithStructure(id));
    }

    @PutMapping("/{id}")
    public CourseDetailsDto update(@PathVariable Long id,
                                   @RequestBody UpdateCourseRequest req) {
        return courseMapper.toDetailsDto(courseService.updateCourse(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    // модуль / урок

    @PostMapping("/{courseId}/modules")
    public ModuleDto addModule(@PathVariable Long courseId,
                               @RequestBody CreateModuleRequest req) {
        return courseMapper.toModuleDto(courseService.addModule(courseId, req));
    }

    @PostMapping("/modules/{moduleId}/lessons")
    public LessonDto addLesson(@PathVariable Long moduleId,
                               @RequestBody CreateLessonRequest req) {
        return courseMapper.toLessonDto(courseService.addLesson(moduleId, req));
    }

    //запись на курс

    @PostMapping("/{courseId}/enroll")
    public EnrollmentDto enroll(@PathVariable Long courseId,
                                @RequestBody EnrollRequest req) {
        Enrollment e = enrollmentService.enrollStudent(courseId, req.getStudentId());
        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(e.getId());
        dto.setCourseId(courseId);
        dto.setStudentId(e.getStudent().getId());
        dto.setStudentName(e.getStudent().getName());
        dto.setEnrollDate(e.getEnrollDate());
        dto.setStatus(e.getStatus());
        return dto;
    }

    @DeleteMapping("/{courseId}/enroll/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unenroll(@PathVariable Long courseId,
                         @PathVariable Long studentId) {
        enrollmentService.unenrollStudent(courseId, studentId);
    }

    @GetMapping("/{courseId}/students")
    public List<UserDto> students(@PathVariable Long courseId) {
        return enrollmentService.findByCourse(courseId).stream()
                .map(Enrollment::getStudent)
                .distinct()
                .map(userMapper::toDto)
                .toList();
    }
}

