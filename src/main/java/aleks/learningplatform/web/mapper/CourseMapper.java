package aleks.learningplatform.web.mapper;

import aleks.learningplatform.domain.content.Lesson;
import aleks.learningplatform.domain.course.Course;
import aleks.learningplatform.domain.course.CourseReview;
import aleks.learningplatform.domain.course.Tag;
import aleks.learningplatform.web.dto.course.CourseDetailsDto;
import aleks.learningplatform.web.dto.course.CourseSummaryDto;
import aleks.learningplatform.web.dto.course.LessonDto;
import aleks.learningplatform.web.dto.course.ModuleDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class CourseMapper {

    public CourseSummaryDto toSummaryDto(Course course) {
        CourseSummaryDto dto = new CourseSummaryDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setTeacherName(course.getTeacher().getName());
        if (course.getCategory() != null) {
            dto.setCategory(course.getCategory().getName());
        }
        dto.setTags(course.getTags().stream().map(Tag::getName).toList());

        double avg = course.getReviews().isEmpty() ? 0.0 :
                course.getReviews().stream().mapToInt(CourseReview::getRating)
                        .average().orElse(0.0);
        dto.setAverageRating(avg);
        return dto;
    }

    public CourseDetailsDto toDetailsDto(Course course) {
        CourseDetailsDto dto = new CourseDetailsDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setTeacherName(course.getTeacher().getName());
        if (course.getCategory() != null) {
            dto.setCategory(course.getCategory().getName());
        }
        dto.setTags(course.getTags().stream().map(Tag::getName).toList());
        dto.setModules(course.getModules().stream()
                .sorted(Comparator.comparing(Module::getOrderIndex,
                        Comparator.nullsLast(Integer::compareTo)))
                .map(this::toModuleDto)
                .toList());

        double avg = course.getReviews().isEmpty() ? 0.0 :
                course.getReviews().stream().mapToInt(CourseReview::getRating)
                        .average().orElse(0.0);
        dto.setAverageRating(avg);
        return dto;
    }

    public ModuleDto toModuleDto(aleks.learningplatform.domain.content.Module module) {
        ModuleDto dto = new ModuleDto();
        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setOrderIndex(module.getOrderIndex());
        dto.setLessons(module.getLessons().stream()
                .map(this::toLessonDto)
                .toList());
        return dto;
    }

    public LessonDto toLessonDto(Lesson lesson) {
        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setVideoUrl(lesson.getVideoUrl());
        return dto;
    }
}
