package aleks.learningplatform.web.controller;

import aleks.learningplatform.domain.content.Assignment;
import aleks.learningplatform.domain.content.Submission;
import aleks.learningplatform.service.AssignmentService;
import aleks.learningplatform.web.dto.assignment.*;
import aleks.learningplatform.web.mapper.AssignmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentMapper assignmentMapper;

    @PostMapping("/lessons/{lessonId}/assignments")
    public AssignmentDto createAssignment(@PathVariable Long lessonId,
                                          @RequestBody CreateAssignmentRequest req) {
        Assignment assignment = assignmentService.createAssignment(lessonId, req);
        return assignmentMapper.toAssignmentDto(assignment);
    }

    @GetMapping("/assignments/{id}")
    public AssignmentDto getAssignment(@PathVariable Long id) {
        return assignmentMapper.toAssignmentDto(assignmentService.getAssignment(id));
    }

    @PostMapping("/assignments/{assignmentId}/submit")
    public SubmissionDto submit(@PathVariable Long assignmentId,
                                @RequestBody SubmitAssignmentRequest req) {
        Submission s = assignmentService
                .submitAssignment(assignmentId, req.getStudentId(), req.getContent());
        return assignmentMapper.toSubmissionDto(s);
    }

    @GetMapping("/assignments/{assignmentId}/submissions")
    public List<SubmissionDto> listSubmissions(@PathVariable Long assignmentId) {
        return assignmentService.getSubmissionsForAssignment(assignmentId).stream()
                .map(assignmentMapper::toSubmissionDto)
                .toList();
    }

    @PatchMapping("/submissions/{id}/grade")
    public SubmissionDto grade(@PathVariable Long id,
                               @RequestBody GradeSubmissionRequest req) {
        Submission s = assignmentService.gradeSubmission(id,
                req.getScore(), req.getFeedback());
        return assignmentMapper.toSubmissionDto(s);
    }
}
