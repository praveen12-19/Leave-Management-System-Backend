package com.college.leavemanagement.controller;

import com.college.leavemanagement.dto.leave.LeaveApplicationDto;
import com.college.leavemanagement.dto.leave.LeaveReviewRequest;
import com.college.leavemanagement.dto.mentor.MentorDto;
import com.college.leavemanagement.dto.mentor.MentorUpdateDto;
import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.dto.student.StudentUpdateDto;
import com.college.leavemanagement.entity.HodDepartmentAssignment;
import com.college.leavemanagement.enums.ApprovalStatus;
import com.college.leavemanagement.security.UserPrincipal;
import com.college.leavemanagement.service.HodService;
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
@RequestMapping("/hod")
@PreAuthorize("hasRole('HOD')")
@Tag(name = "HOD Portal", description = "Department oversight, Mentor reassignment & Tier-2 leave approval workflow")
public class HodController {

    private final HodService hodService;
    private final MentorService mentorService;
    private final LeaveApplicationService leaveApplicationService;

    public HodController(HodService hodService, MentorService mentorService, LeaveApplicationService leaveApplicationService) {
        this.hodService = hodService;
        this.mentorService = mentorService;
        this.leaveApplicationService = leaveApplicationService;
    }

    @GetMapping("/departments")
    @Operation(summary = "List of managed department + year groups")
    public ResponseEntity<List<HodDepartmentAssignment>> getManagedDepartments(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(hodService.getManagedDepartments(user.getLinkedProfileId()));
    }

    @GetMapping("/departments/{deptSectionYear}")
    @Operation(summary = "Get students in managed department section")
    public ResponseEntity<List<StudentProfileDto>> getStudentsByDeptSection(@AuthenticationPrincipal UserPrincipal user,
                                                                             @PathVariable String deptSectionYear) {
        return ResponseEntity.ok(hodService.getStudentsByDeptSectionAndYear(user.getLinkedProfileId(), deptSectionYear));
    }

    @GetMapping("/students/search")
    @Operation(summary = "Search students by regNo or name")
    public ResponseEntity<List<StudentProfileDto>> searchStudents(@RequestParam("query") String query) {
        return ResponseEntity.ok(mentorService.searchStudents(query));
    }

    @PutMapping("/students/{id}")
    @Operation(summary = "Edit student details (HOD shared capability)")
    public ResponseEntity<StudentProfileDto> updateStudent(@PathVariable Long id, @RequestBody StudentUpdateDto dto) {
        return ResponseEntity.ok(mentorService.updateStudentDetails(id, dto));
    }

    @GetMapping("/mentors")
    @Operation(summary = "List mentors in HOD's managed department sections")
    public ResponseEntity<List<MentorDto>> getMentors(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(hodService.getMentorsForHod(user.getLinkedProfileId()));
    }

    @PutMapping("/mentors/{id}")
    @Operation(summary = "Edit mentor details & reassign mentor to different department/year")
    public ResponseEntity<MentorDto> updateMentor(@PathVariable Long id, @RequestBody MentorUpdateDto dto) {
        return ResponseEntity.ok(hodService.updateMentorDetails(id, dto));
    }

    @GetMapping("/leave-requests")
    @Operation(summary = "Get pending leave requests (Mentor-approved only)")
    public ResponseEntity<List<LeaveApplicationDto>> getPendingLeaveRequests(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(leaveApplicationService.getHodPendingRequests(user.getLinkedProfileId()));
    }

    @PutMapping("/leave-requests/{id}/approve")
    @Operation(summary = "HOD Approve leave request")
    public ResponseEntity<LeaveApplicationDto> approveLeave(@AuthenticationPrincipal UserPrincipal user,
                                                             @PathVariable Long id,
                                                             @RequestBody(required = false) LeaveReviewRequest request) {
        if (request == null) request = new LeaveReviewRequest();
        request.setStatus(ApprovalStatus.APPROVED);
        return ResponseEntity.ok(leaveApplicationService.hodReview(id, user.getLinkedProfileId(), request));
    }

    @PutMapping("/leave-requests/{id}/reject")
    @Operation(summary = "HOD Reject leave request (remarks mandatory)")
    public ResponseEntity<LeaveApplicationDto> rejectLeave(@AuthenticationPrincipal UserPrincipal user,
                                                            @PathVariable Long id,
                                                            @Valid @RequestBody LeaveReviewRequest request) {
        request.setStatus(ApprovalStatus.REJECTED);
        return ResponseEntity.ok(leaveApplicationService.hodReview(id, user.getLinkedProfileId(), request));
    }

    @GetMapping("/leave-history")
    @Operation(summary = "Get HOD leave review history")
    public ResponseEntity<List<LeaveApplicationDto>> getLeaveHistory(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(leaveApplicationService.getHodLeaveHistory(user.getLinkedProfileId()));
    }
}
