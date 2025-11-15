package aleks.learningplatform.web.mapper;

import aleks.learningplatform.domain.content.Assignment;
import aleks.learningplatform.domain.content.Submission;
import aleks.learningplatform.web.dto.assignment.AssignmentDto;
import aleks.learningplatform.web.dto.assignment.SubmissionDto;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMapper {

    public AssignmentDto toAssignmentDto(Assignment a) {
        AssignmentDto dto = new AssignmentDto();
        dto.setId(a.getId());
        dto.setLessonId(a.getLesson().getId());
        dto.setTitle(a.getTitle());
        dto.setDescription(a.getDescription());
        dto.setDueDate(a.getDueDate());
        dto.setMaxScore(a.getMaxScore());
        return dto;
    }

    public SubmissionDto toSubmissionDto(Submission s) {
        SubmissionDto dto = new SubmissionDto();
        dto.setId(s.getId());
        dto.setAssignmentId(s.getAssignment().getId());
        dto.setStudentId(s.getStudent().getId());
        dto.setStudentName(s.getStudent().getName());
        dto.setSubmittedAt(s.getSubmittedAt());
        dto.setContent(s.getContent());
        dto.setScore(s.getScore());
        dto.setFeedback(s.getFeedback());
        return dto;
    }
}

