package com.college.leavemanagement.controller;

import com.college.leavemanagement.dto.leave.LeaveApplicationDto;
import com.college.leavemanagement.dto.leave.LeaveReviewRequest;
import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.dto.student.StudentUpdateDto;
import com.college.leavemanagement.enums.ApprovalStatus;
import com.college.leavemanagement.security.UserPrincipal;
import com.college.leavemanagement.service.LeaveApplicationService;
import com.college.leavemanagement.service.MentorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mentor")
@PreAuthorize("hasRole('MENTOR')")
@Tag(name = "Mentor Portal", description = "Mentor student management & Tier-1 leave approval workflow")
public class MentorController {

    private final MentorService mentorService;
    private final LeaveApplicationService leaveApplicationService;

    public MentorController(MentorService mentorService, LeaveApplicationService leaveApplicationService) {
        this.mentorService = mentorService;
        this.leaveApplicationService = leaveApplicationService;
    }

    @GetMapping("/students")
    @Operation(summary = "List assigned students ordered by regNo")
    public ResponseEntity<List<StudentProfileDto>> getAssignedStudents(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(mentorService.getAssignedStudents(user.getLinkedProfileId()));
    }

    @GetMapping("/students/{id}")
    @Operation(summary = "Get full student profile including leave stats")
    public ResponseEntity<StudentProfileDto> getStudentDetails(@PathVariable Long id) {
        return ResponseEntity.ok(mentorService.getStudentDetails(id));
    }

    @PutMapping("/students/{id}")
    @Operation(summary = "Update student details")
    public ResponseEntity<StudentProfileDto> updateStudent(@PathVariable Long id, @RequestBody StudentUpdateDto dto) {
        return ResponseEntity.ok(mentorService.updateStudentDetails(id, dto));
    }

    @GetMapping("/students/search")
    @Operation(summary = "Search students by regNo or name")
    public ResponseEntity<List<StudentProfileDto>> searchStudents(@RequestParam("query") String query) {
        return ResponseEntity.ok(mentorService.searchStudents(query));
    }

    @GetMapping("/leave-requests")
    @Operation(summary = "Get pending leave requests for mentor review")
    public ResponseEntity<List<LeaveApplicationDto>> getPendingLeaveRequests(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(leaveApplicationService.getMentorPendingRequests(user.getLinkedProfileId()));
    }

    @PutMapping("/leave-requests/{id}/approve")
    @Operation(summary = "Approve student leave request")
    public ResponseEntity<LeaveApplicationDto> approveLeave(@AuthenticationPrincipal UserPrincipal user,
                                                             @PathVariable Long id,
                                                             @RequestBody(required = false) LeaveReviewRequest request) {
        if (request == null) request = new LeaveReviewRequest();
        request.setStatus(ApprovalStatus.APPROVED);
        return ResponseEntity.ok(leaveApplicationService.mentorReview(id, user.getLinkedProfileId(), request));
    }

    @PutMapping("/leave-requests/{id}/reject")
    @Operation(summary = "Reject student leave request (remarks mandatory)")
    public ResponseEntity<LeaveApplicationDto> rejectLeave(@AuthenticationPrincipal UserPrincipal user,
                                                            @PathVariable Long id,
                                                            @Valid @RequestBody LeaveReviewRequest request) {
        request.setStatus(ApprovalStatus.REJECTED);
        return ResponseEntity.ok(leaveApplicationService.mentorReview(id, user.getLinkedProfileId(), request));
    }

    @GetMapping("/leave-history")
    @Operation(summary = "Get mentor's acted leave history (approved + rejected)")
    public ResponseEntity<List<LeaveApplicationDto>> getLeaveHistory(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(leaveApplicationService.getMentorLeaveHistory(user.getLinkedProfileId()));
    }
}
