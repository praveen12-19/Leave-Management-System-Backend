package com.college.leavemanagement.controller;

import com.college.leavemanagement.dto.leave.LeaveApplicationDto;
import com.college.leavemanagement.dto.leave.LeaveReviewRequest;
import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.dto.student.StudentUpdateDto;
import com.college.leavemanagement.dto.warden.RoomOccupancyDto;
import com.college.leavemanagement.enums.ApprovalStatus;
import com.college.leavemanagement.security.UserPrincipal;
import com.college.leavemanagement.service.LeaveApplicationService;
import com.college.leavemanagement.service.MentorService;
import com.college.leavemanagement.service.WardenService;
import com.college.leavemanagement.dto.warden.WardenDto;
import com.college.leavemanagement.dto.warden.WardenUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/warden")
@PreAuthorize("hasAnyRole('WARDEN', 'CHIEF_WARDEN')")
@Tag(name = "Warden / Chief Warden Portal", description = "Hostel block oversight, room occupancy dashboard, and Tier-3 leave approval workflow")
public class WardenController {

    private final WardenService wardenService;
    private final MentorService mentorService;
    private final LeaveApplicationService leaveApplicationService;

    public WardenController(WardenService wardenService, MentorService mentorService, LeaveApplicationService leaveApplicationService) {
        this.wardenService = wardenService;
        this.mentorService = mentorService;
        this.leaveApplicationService = leaveApplicationService;
    }

    @GetMapping("/students")
    @Operation(summary = "Get full hostel student list (Scoped to assigned block; Chief Warden gets all)")
    public ResponseEntity<List<StudentProfileDto>> getStudents(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(wardenService.getStudentsForWarden(user.getLinkedProfileId()));
    }

    @PutMapping("/students/{id}")
    @Operation(summary = "Update student hostel block or room number details")
    public ResponseEntity<StudentProfileDto> updateStudent(@PathVariable Long id, @RequestBody StudentUpdateDto dto) {
        return ResponseEntity.ok(mentorService.updateStudentDetails(id, dto));
    }

    @GetMapping("/leave-requests")
    @Operation(summary = "Get pending leave requests (Only HOD-approved hosteller requests)")
    public ResponseEntity<List<LeaveApplicationDto>> getPendingLeaveRequests(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(leaveApplicationService.getWardenPendingRequests(user.getLinkedProfileId()));
    }

    @PutMapping("/leave-requests/{id}/approve")
    @Operation(summary = "Warden Approve leave request")
    public ResponseEntity<LeaveApplicationDto> approveLeave(@AuthenticationPrincipal UserPrincipal user,
                                                             @PathVariable Long id,
                                                             @RequestBody(required = false) LeaveReviewRequest request) {
        if (request == null) request = new LeaveReviewRequest();
        request.setStatus(ApprovalStatus.APPROVED);
        return ResponseEntity.ok(leaveApplicationService.wardenReview(id, user.getLinkedProfileId(), request));
    }

    @PutMapping("/leave-requests/{id}/reject")
    @Operation(summary = "Warden Reject leave request (remarks mandatory)")
    public ResponseEntity<LeaveApplicationDto> rejectLeave(@AuthenticationPrincipal UserPrincipal user,
                                                            @PathVariable Long id,
                                                            @Valid @RequestBody LeaveReviewRequest request) {
        request.setStatus(ApprovalStatus.REJECTED);
        return ResponseEntity.ok(leaveApplicationService.wardenReview(id, user.getLinkedProfileId(), request));
    }

    @GetMapping("/leave-history")
    @Operation(summary = "Paginated & status-filterable leave history for Warden / Chief Warden")
    public ResponseEntity<Page<LeaveApplicationDto>> getLeaveHistory(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(value = "status", required = false) ApprovalStatus status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(leaveApplicationService.getWardenLeaveHistory(user.getLinkedProfileId(), status, pageable));
    }

    @GetMapping("/rooms/occupancy")
    @Operation(summary = "Room occupancy dashboard: Lists students currently away on approved leave for today")
    public ResponseEntity<List<RoomOccupancyDto>> getRoomOccupancyDashboard(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(wardenService.getRoomOccupancyDashboard(user.getLinkedProfileId(), date));
    }

    @GetMapping("/manage/wardens")
    @PreAuthorize("hasAnyRole('CHIEF_WARDEN', 'ADMIN')")
    @Operation(summary = "Chief Warden / Admin endpoint to list all block wardens")
    public ResponseEntity<List<WardenDto>> getBlockWardens() {
        return ResponseEntity.ok(wardenService.getBlockWardens());
    }

    @PutMapping("/manage/wardens/{id}")
    @PreAuthorize("hasAnyRole('CHIEF_WARDEN', 'ADMIN')")
    @Operation(summary = "Chief Warden / Admin endpoint to update block warden details")
    public ResponseEntity<WardenDto> updateBlockWarden(@PathVariable Long id, @RequestBody WardenUpdateDto dto) {
        return ResponseEntity.ok(wardenService.updateBlockWarden(id, dto));
    }
}
