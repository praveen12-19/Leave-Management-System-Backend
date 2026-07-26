package com.college.leavemanagement.controller;

import com.college.leavemanagement.dto.hod.HodCreateDto;
import com.college.leavemanagement.dto.hod.HodDto;
import com.college.leavemanagement.dto.hod.HodUpdateDto;
import com.college.leavemanagement.dto.mentor.MentorCreateDto;
import com.college.leavemanagement.dto.mentor.MentorDto;
import com.college.leavemanagement.dto.mentor.MentorUpdateDto;
import com.college.leavemanagement.dto.student.StudentCreateDto;
import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.dto.student.StudentUpdateDto;
import com.college.leavemanagement.dto.warden.WardenCreateDto;
import com.college.leavemanagement.dto.warden.WardenDto;
import com.college.leavemanagement.dto.warden.WardenUpdateDto;
import com.college.leavemanagement.entity.AuditLog;
import com.college.leavemanagement.service.AdminService;
import com.college.leavemanagement.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Operations", description = "Bootstrap superuser endpoints for full CRUD on users & system audit logs")
public class AdminController {

    private final AdminService adminService;
    private final AuditLogService auditLogService;

    public AdminController(AdminService adminService, AuditLogService auditLogService) {
        this.adminService = adminService;
        this.auditLogService = auditLogService;
    }

    // --- STUDENT ---
    @PostMapping("/users/STUDENT")
    @Operation(summary = "Create a Student record & login user")
    public ResponseEntity<StudentProfileDto> createStudent(@Valid @RequestBody StudentCreateDto dto) {
        return new ResponseEntity<>(adminService.createStudent(dto), HttpStatus.CREATED);
    }

    @GetMapping("/users/STUDENT")
    @Operation(summary = "List all Students")
    public ResponseEntity<List<StudentProfileDto>> getAllStudents() {
        return ResponseEntity.ok(adminService.getAllStudents());
    }

    @PutMapping("/users/STUDENT/{id}")
    @Operation(summary = "Update Student profile")
    public ResponseEntity<StudentProfileDto> updateStudent(@PathVariable Long id, @RequestBody StudentUpdateDto dto) {
        return ResponseEntity.ok(adminService.updateStudent(id, dto));
    }

    @DeleteMapping("/users/STUDENT/{id}")
    @Operation(summary = "Delete Student")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        adminService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // --- MENTOR ---
    @PostMapping("/users/MENTOR")
    @Operation(summary = "Create a Mentor record & login user")
    public ResponseEntity<MentorDto> createMentor(@Valid @RequestBody MentorCreateDto dto) {
        return new ResponseEntity<>(adminService.createMentor(dto), HttpStatus.CREATED);
    }

    @GetMapping("/users/MENTOR")
    @Operation(summary = "List all Mentors")
    public ResponseEntity<List<MentorDto>> getAllMentors() {
        return ResponseEntity.ok(adminService.getAllMentors());
    }

    @PutMapping("/users/MENTOR/{id}")
    @Operation(summary = "Update Mentor profile")
    public ResponseEntity<MentorDto> updateMentor(@PathVariable Long id, @RequestBody MentorUpdateDto dto) {
        return ResponseEntity.ok(adminService.updateMentor(id, dto));
    }

    @DeleteMapping("/users/MENTOR/{id}")
    @Operation(summary = "Delete Mentor")
    public ResponseEntity<Void> deleteMentor(@PathVariable Long id) {
        adminService.deleteMentor(id);
        return ResponseEntity.noContent().build();
    }

    // --- HOD ---
    @PostMapping("/users/HOD")
    @Operation(summary = "Create an HOD record & login user")
    public ResponseEntity<HodDto> createHod(@Valid @RequestBody HodCreateDto dto) {
        return new ResponseEntity<>(adminService.createHod(dto), HttpStatus.CREATED);
    }

    @GetMapping("/users/HOD")
    @Operation(summary = "List all HODs")
    public ResponseEntity<List<HodDto>> getAllHods() {
        return ResponseEntity.ok(adminService.getAllHods());
    }

    @PutMapping("/users/HOD/{id}")
    @Operation(summary = "Update HOD profile")
    public ResponseEntity<HodDto> updateHod(@PathVariable Long id, @RequestBody HodUpdateDto dto) {
        return ResponseEntity.ok(adminService.updateHod(id, dto));
    }

    @DeleteMapping("/users/HOD/{id}")
    @Operation(summary = "Delete HOD")
    public ResponseEntity<Void> deleteHod(@PathVariable Long id) {
        adminService.deleteHod(id);
        return ResponseEntity.noContent().build();
    }

    // --- WARDEN ---
    @PostMapping("/users/WARDEN")
    @Operation(summary = "Create a Warden / Chief Warden record & login user")
    public ResponseEntity<WardenDto> createWarden(@Valid @RequestBody WardenCreateDto dto) {
        return new ResponseEntity<>(adminService.createWarden(dto), HttpStatus.CREATED);
    }

    @GetMapping("/users/WARDEN")
    @Operation(summary = "List all Wardens & Chief Wardens")
    public ResponseEntity<List<WardenDto>> getAllWardens() {
        return ResponseEntity.ok(adminService.getAllWardens());
    }

    @PutMapping("/users/WARDEN/{id}")
    @Operation(summary = "Update Warden profile")
    public ResponseEntity<WardenDto> updateWarden(@PathVariable Long id, @RequestBody WardenUpdateDto dto) {
        return ResponseEntity.ok(adminService.updateWarden(id, dto));
    }

    @DeleteMapping("/users/WARDEN/{id}")
    @Operation(summary = "Delete Warden")
    public ResponseEntity<Void> deleteWarden(@PathVariable Long id) {
        adminService.deleteWarden(id);
        return ResponseEntity.noContent().build();
    }

    // --- AUDIT LOGS ---
    @GetMapping("/audit-logs")
    @Operation(summary = "View system audit logs")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(auditLogService.getAllAuditLogs());
    }
}
