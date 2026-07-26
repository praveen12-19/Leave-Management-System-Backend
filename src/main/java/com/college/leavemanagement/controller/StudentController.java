package com.college.leavemanagement.controller;

import com.college.leavemanagement.dto.leave.LeaveApplicationDto;
import com.college.leavemanagement.dto.leave.LeaveSubmitRequest;
import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.security.UserPrincipal;
import com.college.leavemanagement.service.LeaveApplicationService;
import com.college.leavemanagement.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')")
@Tag(name = "Student Portal", description = "Student profile, leave application submission, and leave tracking")
public class StudentController {

    private final StudentService studentService;
    private final LeaveApplicationService leaveApplicationService;

    public StudentController(StudentService studentService, LeaveApplicationService leaveApplicationService) {
        this.studentService = studentService;
        this.leaveApplicationService = leaveApplicationService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get student profile and leave summary statistics")
    public ResponseEntity<StudentProfileDto> getMyProfile(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(studentService.getStudentProfile(user.getLinkedProfileId()));
    }

    @PostMapping("/leave")
    @Operation(summary = "Submit a new leave application")
    public ResponseEntity<LeaveApplicationDto> submitLeave(@AuthenticationPrincipal UserPrincipal user,
                                                             @Valid @RequestBody LeaveSubmitRequest request) {
        LeaveApplicationDto dto = leaveApplicationService.submitLeave(user.getLinkedProfileId(), request);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/leave/history")
    @Operation(summary = "Get full leave application history and current approval stage per role")
    public ResponseEntity<List<LeaveApplicationDto>> getLeaveHistory(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(leaveApplicationService.getStudentLeaveHistory(user.getLinkedProfileId()));
    }
}
