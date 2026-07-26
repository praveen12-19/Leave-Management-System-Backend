package com.college.leavemanagement.service;

import com.college.leavemanagement.dto.leave.ApprovalHistoryDto;
import com.college.leavemanagement.dto.leave.LeaveApplicationDto;
import com.college.leavemanagement.dto.leave.LeaveReviewRequest;
import com.college.leavemanagement.dto.leave.LeaveSubmitRequest;
import com.college.leavemanagement.entity.*;
import com.college.leavemanagement.enums.*;
import com.college.leavemanagement.exception.BusinessException;
import com.college.leavemanagement.exception.ResourceNotFoundException;
import com.college.leavemanagement.exception.UnauthorizedException;
import com.college.leavemanagement.mapper.LeaveApplicationMapper;
import com.college.leavemanagement.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class LeaveApplicationService {

    private static final Logger log = LoggerFactory.getLogger(LeaveApplicationService.class);

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;
    private final HodRepository hodRepository;
    private final WardenRepository wardenRepository;
    private final ApprovalHistoryRepository approvalHistoryRepository;
    private final LeaveApplicationMapper leaveApplicationMapper;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final AuditLogService auditLogService;

    public LeaveApplicationService(LeaveApplicationRepository leaveApplicationRepository,
                                   StudentRepository studentRepository,
                                   MentorRepository mentorRepository,
                                   HodRepository hodRepository,
                                   WardenRepository wardenRepository,
                                   ApprovalHistoryRepository approvalHistoryRepository,
                                   LeaveApplicationMapper leaveApplicationMapper,
                                   NotificationService notificationService,
                                   EmailService emailService,
                                   AuditLogService auditLogService) {
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.studentRepository = studentRepository;
        this.mentorRepository = mentorRepository;
        this.hodRepository = hodRepository;
        this.wardenRepository = wardenRepository;
        this.approvalHistoryRepository = approvalHistoryRepository;
        this.leaveApplicationMapper = leaveApplicationMapper;
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.auditLogService = auditLogService;
    }

    // --- STUDENT ACTIONS ---
    @Transactional
    public LeaveApplicationDto submitLeave(Long studentId, LeaveSubmitRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for ID: " + studentId));

        if (student.getMentor() == null) {
            throw new BusinessException("Cannot submit leave request. No mentor is currently assigned to you.");
        }

        LocalDateTime now = LocalDateTime.now();
        ApprovalStatus wardenInitialStatus = (student.getStudentType() == StudentType.HOSTELLER) ? ApprovalStatus.PENDING : null;

        LeaveApplication leave = LeaveApplication.builder()
                .student(student)
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .reason(request.getReason())
                .submittedAt(now)
                .currentStage(LeaveStage.MENTOR_REVIEW)
                .mentorStatus(ApprovalStatus.PENDING)
                .hodStatus(ApprovalStatus.PENDING)
                .wardenStatus(wardenInitialStatus)
                .finalStatus(ApprovalStatus.PENDING)
                .build();

        LeaveApplication savedLeave = leaveApplicationRepository.save(leave);

        ApprovalHistory history = ApprovalHistory.builder()
                .leaveApplication(savedLeave)
                .approverRole(Role.STUDENT)
                .approverId(student.getId())
                .approverName(student.getName())
                .status(ApprovalStatus.PENDING)
                .remarks("Leave submitted by student: " + request.getReason())
                .timestamp(now)
                .build();
        approvalHistoryRepository.save(history);

        Mentor mentor = student.getMentor();
        if (mentor.getUser() != null) {
            String message = String.format("New leave request (#%d) submitted by student %s (%s) from %s to %s.",
                    savedLeave.getId(), student.getName(), student.getRegNo(), request.getFromDate(), request.getToDate());

            notificationService.createNotification(mentor.getUser(), message, savedLeave.getId());

            String mentorEmail = mentor.getUser().getUsername();
            emailService.sendEmail(mentorEmail, "New Leave Request Submitted by " + student.getName(), message);
        }

        auditLogService.logAction("SUBMIT_LEAVE", "LeaveApplication", savedLeave.getId(),
                "Leave submitted from " + request.getFromDate() + " to " + request.getToDate());

        return enrichWithHistory(savedLeave);
    }

    public List<LeaveApplicationDto> getStudentLeaveHistory(Long studentId) {
        return leaveApplicationRepository.findByStudentIdOrderBySubmittedAtDesc(studentId)
                .stream()
                .map(this::enrichWithHistory)
                .toList();
    }

    // --- MENTOR ACTIONS ---
    public List<LeaveApplicationDto> getMentorPendingRequests(Long mentorId) {
        return leaveApplicationRepository.findByStudentMentorIdAndCurrentStageOrderBySubmittedAtAsc(mentorId, LeaveStage.MENTOR_REVIEW)
                .stream()
                .map(this::enrichWithHistory)
                .toList();
    }

    public List<LeaveApplicationDto> getMentorLeaveHistory(Long mentorId) {
        return leaveApplicationRepository.findByStudentMentorIdAndMentorStatusNotOrderByMentorActedAtDesc(mentorId, ApprovalStatus.PENDING)
                .stream()
                .map(this::enrichWithHistory)
                .toList();
    }

    @Transactional
    public LeaveApplicationDto mentorReview(Long leaveId, Long mentorId, LeaveReviewRequest review) {
        LeaveApplication leave = leaveApplicationRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave application not found with ID: " + leaveId));

        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor profile not found with ID: " + mentorId));

        if (!leave.getStudent().getMentor().getId().equals(mentorId)) {
            throw new UnauthorizedException("You are not authorized to review leave for this student.");
        }

        if (leave.getCurrentStage() != LeaveStage.MENTOR_REVIEW) {
            throw new BusinessException("Leave application is not currently under Mentor review.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (review.getStatus() == ApprovalStatus.REJECTED) {
            if (review.getRemarks() == null || review.getRemarks().isBlank()) {
                throw new BusinessException("Remarks are mandatory when rejecting a leave application.");
            }
            leave.setMentorStatus(ApprovalStatus.REJECTED);
            leave.setMentorRemarks(review.getRemarks());
            leave.setMentorActedAt(now);
            leave.setCurrentStage(LeaveStage.REJECTED);
            leave.setFinalStatus(ApprovalStatus.REJECTED);
        } else if (review.getStatus() == ApprovalStatus.APPROVED) {
            leave.setMentorStatus(ApprovalStatus.APPROVED);
            leave.setMentorRemarks(review.getRemarks());
            leave.setMentorActedAt(now);
            leave.setCurrentStage(LeaveStage.HOD_REVIEW);
        }

        LeaveApplication updated = leaveApplicationRepository.save(leave);

        saveHistory(updated, Role.MENTOR, mentor.getId(), mentor.getName(), review.getStatus(), review.getRemarks(), now);
        auditLogService.logAction("MENTOR_REVIEW_LEAVE", "LeaveApplication", leaveId,
                "Mentor action: " + review.getStatus() + " | Remarks: " + review.getRemarks());

        return enrichWithHistory(updated);
    }

    // --- HOD ACTIONS ---
    public List<LeaveApplicationDto> getHodPendingRequests(Long hodId) {
        Hod hod = hodRepository.findById(hodId)
                .orElseThrow(() -> new ResourceNotFoundException("HOD profile not found with ID: " + hodId));

        Set<HodDepartmentAssignment> assignments = hod.getManagedDepartmentAssignments();
        List<LeaveApplication> result = new ArrayList<>();
        for (HodDepartmentAssignment assignment : assignments) {
            result.addAll(leaveApplicationRepository.findPendingForHod(assignment.getDeptSection(), assignment.getYear()));
        }
        return result.stream().map(this::enrichWithHistory).toList();
    }

    public List<LeaveApplicationDto> getHodLeaveHistory(Long hodId) {
        Hod hod = hodRepository.findById(hodId)
                .orElseThrow(() -> new ResourceNotFoundException("HOD profile not found with ID: " + hodId));

        Set<HodDepartmentAssignment> assignments = hod.getManagedDepartmentAssignments();
        List<LeaveApplication> result = new ArrayList<>();
        for (HodDepartmentAssignment assignment : assignments) {
            result.addAll(leaveApplicationRepository.findHistoryForHod(assignment.getDeptSection(), assignment.getYear()));
        }
        return result.stream().map(this::enrichWithHistory).toList();
    }

    @Transactional
    public LeaveApplicationDto hodReview(Long leaveId, Long hodId, LeaveReviewRequest review) {
        LeaveApplication leave = leaveApplicationRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave application not found with ID: " + leaveId));

        Hod hod = hodRepository.findById(hodId)
                .orElseThrow(() -> new ResourceNotFoundException("HOD profile not found with ID: " + hodId));

        if (leave.getMentorStatus() != ApprovalStatus.APPROVED || leave.getCurrentStage() != LeaveStage.HOD_REVIEW) {
            throw new BusinessException("Leave application is not eligible for HOD review (must be mentor approved first).");
        }

        LocalDateTime now = LocalDateTime.now();
        if (review.getStatus() == ApprovalStatus.REJECTED) {
            if (review.getRemarks() == null || review.getRemarks().isBlank()) {
                throw new BusinessException("Remarks are mandatory when rejecting a leave application.");
            }
            leave.setHodStatus(ApprovalStatus.REJECTED);
            leave.setHodRemarks(review.getRemarks());
            leave.setHodActedAt(now);
            leave.setCurrentStage(LeaveStage.REJECTED);
            leave.setFinalStatus(ApprovalStatus.REJECTED);
        } else if (review.getStatus() == ApprovalStatus.APPROVED) {
            leave.setHodStatus(ApprovalStatus.APPROVED);
            leave.setHodRemarks(review.getRemarks());
            leave.setHodActedAt(now);

            if (leave.getStudent().getStudentType() == StudentType.HOSTELLER) {
                leave.setCurrentStage(LeaveStage.WARDEN_REVIEW);
            } else {
                leave.setCurrentStage(LeaveStage.COMPLETED);
                leave.setFinalStatus(ApprovalStatus.APPROVED);
            }
        }

        LeaveApplication updated = leaveApplicationRepository.save(leave);

        saveHistory(updated, Role.HOD, hod.getId(), hod.getName(), review.getStatus(), review.getRemarks(), now);
        auditLogService.logAction("HOD_REVIEW_LEAVE", "LeaveApplication", leaveId,
                "HOD action: " + review.getStatus() + " | Remarks: " + review.getRemarks());

        return enrichWithHistory(updated);
    }

    // --- WARDEN ACTIONS ---
    public List<LeaveApplicationDto> getWardenPendingRequests(Long wardenId) {
        Warden warden = wardenRepository.findById(wardenId)
                .orElseThrow(() -> new ResourceNotFoundException("Warden profile not found with ID: " + wardenId));

        List<LeaveApplication> pending;
        if (warden.getAssignedBlock() == null) {
            pending = leaveApplicationRepository.findPendingForChiefWarden(StudentType.HOSTELLER);
        } else {
            pending = leaveApplicationRepository.findPendingForWardenScoped(StudentType.HOSTELLER, warden.getAssignedBlock());
        }

        return pending.stream().map(this::enrichWithHistory).toList();
    }

    public Page<LeaveApplicationDto> getWardenLeaveHistory(Long wardenId, ApprovalStatus status, Pageable pageable) {
        Warden warden = wardenRepository.findById(wardenId)
                .orElseThrow(() -> new ResourceNotFoundException("Warden profile not found with ID: " + wardenId));

        Page<LeaveApplication> page;
        if (warden.getAssignedBlock() == null) {
            if (status != null) {
                page = leaveApplicationRepository.findHistoryForChiefWarden(status, pageable);
            } else {
                page = leaveApplicationRepository.findAllHistoryForChiefWarden(pageable);
            }
        } else {
            if (status != null) {
                page = leaveApplicationRepository.findHistoryForWardenScoped(warden.getAssignedBlock(), status, pageable);
            } else {
                page = leaveApplicationRepository.findAllHistoryForWardenScoped(warden.getAssignedBlock(), pageable);
            }
        }

        return page.map(this::enrichWithHistory);
    }

    @Transactional
    public LeaveApplicationDto wardenReview(Long leaveId, Long wardenId, LeaveReviewRequest review) {
        LeaveApplication leave = leaveApplicationRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave application not found with ID: " + leaveId));

        Warden warden = wardenRepository.findById(wardenId)
                .orElseThrow(() -> new ResourceNotFoundException("Warden profile not found with ID: " + wardenId));

        if (leave.getHodStatus() != ApprovalStatus.APPROVED || leave.getStudent().getStudentType() != StudentType.HOSTELLER) {
            throw new BusinessException("Leave application is not eligible for Warden review (must be HOD-approved hosteller).");
        }

        if (warden.getAssignedBlock() != null && !warden.getAssignedBlock().equals(leave.getStudent().getHostelBlock())) {
            throw new UnauthorizedException("Warden is not assigned to student's hostel block: " + leave.getStudent().getHostelBlock());
        }

        if (leave.getCurrentStage() != LeaveStage.WARDEN_REVIEW) {
            throw new BusinessException("Leave application is not currently under Warden review.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (review.getStatus() == ApprovalStatus.REJECTED) {
            if (review.getRemarks() == null || review.getRemarks().isBlank()) {
                throw new BusinessException("Remarks are mandatory when rejecting a leave application.");
            }
            leave.setWardenStatus(ApprovalStatus.REJECTED);
            leave.setWardenRemarks(review.getRemarks());
            leave.setWardenActedAt(now);
            leave.setCurrentStage(LeaveStage.REJECTED);
            leave.setFinalStatus(ApprovalStatus.REJECTED);
        } else if (review.getStatus() == ApprovalStatus.APPROVED) {
            leave.setWardenStatus(ApprovalStatus.APPROVED);
            leave.setWardenRemarks(review.getRemarks());
            leave.setWardenActedAt(now);
            leave.setCurrentStage(LeaveStage.COMPLETED);
            leave.setFinalStatus(ApprovalStatus.APPROVED);
        }

        LeaveApplication updated = leaveApplicationRepository.save(leave);

        Role role = (warden.getAssignedBlock() == null) ? Role.CHIEF_WARDEN : Role.WARDEN;
        saveHistory(updated, role, warden.getId(), warden.getName(), review.getStatus(), review.getRemarks(), now);
        auditLogService.logAction("WARDEN_REVIEW_LEAVE", "LeaveApplication", leaveId,
                "Warden action: " + review.getStatus() + " | Remarks: " + review.getRemarks());

        return enrichWithHistory(updated);
    }

    private void saveHistory(LeaveApplication leave, Role role, Long approverId, String approverName, ApprovalStatus status, String remarks, LocalDateTime timestamp) {
        ApprovalHistory history = ApprovalHistory.builder()
                .leaveApplication(leave)
                .approverRole(role)
                .approverId(approverId)
                .approverName(approverName)
                .status(status)
                .remarks(remarks)
                .timestamp(timestamp)
                .build();
        approvalHistoryRepository.save(history);
    }

    private LeaveApplicationDto enrichWithHistory(LeaveApplication leave) {
        LeaveApplicationDto dto = leaveApplicationMapper.toDto(leave);
        List<ApprovalHistory> historyList = approvalHistoryRepository.findByLeaveApplicationIdOrderByTimestampAsc(leave.getId());
        List<ApprovalHistoryDto> historyDtos = leaveApplicationMapper.toHistoryDtoList(historyList);
        dto.setHistory(historyDtos);
        return dto;
    }
}
