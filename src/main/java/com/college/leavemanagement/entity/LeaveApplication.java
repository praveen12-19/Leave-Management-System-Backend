package com.college.leavemanagement.entity;

import com.college.leavemanagement.enums.ApprovalStatus;
import com.college.leavemanagement.enums.LeaveStage;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_applications")
public class LeaveApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    @Column(nullable = false, length = 1000)
    private String reason;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStage currentStage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus mentorStatus;
    private String mentorRemarks;
    private LocalDateTime mentorActedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus hodStatus;
    private String hodRemarks;
    private LocalDateTime hodActedAt;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus wardenStatus;
    private String wardenRemarks;
    private LocalDateTime wardenActedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus finalStatus;

    public LeaveApplication() {}

    public LeaveApplication(Long id, Student student, LocalDate fromDate, LocalDate toDate, String reason,
                            LocalDateTime submittedAt, LeaveStage currentStage, ApprovalStatus mentorStatus,
                            String mentorRemarks, LocalDateTime mentorActedAt, ApprovalStatus hodStatus,
                            String hodRemarks, LocalDateTime hodActedAt, ApprovalStatus wardenStatus,
                            String wardenRemarks, LocalDateTime wardenActedAt, ApprovalStatus finalStatus) {
        this.id = id;
        this.student = student;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
        this.submittedAt = submittedAt;
        this.currentStage = currentStage;
        this.mentorStatus = mentorStatus;
        this.mentorRemarks = mentorRemarks;
        this.mentorActedAt = mentorActedAt;
        this.hodStatus = hodStatus;
        this.hodRemarks = hodRemarks;
        this.hodActedAt = hodActedAt;
        this.wardenStatus = wardenStatus;
        this.wardenRemarks = wardenRemarks;
        this.wardenActedAt = wardenActedAt;
        this.finalStatus = finalStatus;
    }

    public static LeaveApplicationBuilder builder() {
        return new LeaveApplicationBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LeaveStage getCurrentStage() { return currentStage; }
    public void setCurrentStage(LeaveStage currentStage) { this.currentStage = currentStage; }

    public ApprovalStatus getMentorStatus() { return mentorStatus; }
    public void setMentorStatus(ApprovalStatus mentorStatus) { this.mentorStatus = mentorStatus; }

    public String getMentorRemarks() { return mentorRemarks; }
    public void setMentorRemarks(String mentorRemarks) { this.mentorRemarks = mentorRemarks; }

    public LocalDateTime getMentorActedAt() { return mentorActedAt; }
    public void setMentorActedAt(LocalDateTime mentorActedAt) { this.mentorActedAt = mentorActedAt; }

    public ApprovalStatus getHodStatus() { return hodStatus; }
    public void setHodStatus(ApprovalStatus hodStatus) { this.hodStatus = hodStatus; }

    public String getHodRemarks() { return hodRemarks; }
    public void setHodRemarks(String hodRemarks) { this.hodRemarks = hodRemarks; }

    public LocalDateTime getHodActedAt() { return hodActedAt; }
    public void setHodActedAt(LocalDateTime hodActedAt) { this.hodActedAt = hodActedAt; }

    public ApprovalStatus getWardenStatus() { return wardenStatus; }
    public void setWardenStatus(ApprovalStatus wardenStatus) { this.wardenStatus = wardenStatus; }

    public String getWardenRemarks() { return wardenRemarks; }
    public void setWardenRemarks(String wardenRemarks) { this.wardenRemarks = wardenRemarks; }

    public LocalDateTime getWardenActedAt() { return wardenActedAt; }
    public void setWardenActedAt(LocalDateTime wardenActedAt) { this.wardenActedAt = wardenActedAt; }

    public ApprovalStatus getFinalStatus() { return finalStatus; }
    public void setFinalStatus(ApprovalStatus finalStatus) { this.finalStatus = finalStatus; }

    public static class LeaveApplicationBuilder {
        private Long id;
        private Student student;
        private LocalDate fromDate;
        private LocalDate toDate;
        private String reason;
        private LocalDateTime submittedAt;
        private LeaveStage currentStage;
        private ApprovalStatus mentorStatus;
        private String mentorRemarks;
        private LocalDateTime mentorActedAt;
        private ApprovalStatus hodStatus;
        private String hodRemarks;
        private LocalDateTime hodActedAt;
        private ApprovalStatus wardenStatus;
        private String wardenRemarks;
        private LocalDateTime wardenActedAt;
        private ApprovalStatus finalStatus;

        LeaveApplicationBuilder() {}

        public LeaveApplicationBuilder id(Long id) { this.id = id; return this; }
        public LeaveApplicationBuilder student(Student student) { this.student = student; return this; }
        public LeaveApplicationBuilder fromDate(LocalDate fromDate) { this.fromDate = fromDate; return this; }
        public LeaveApplicationBuilder toDate(LocalDate toDate) { this.toDate = toDate; return this; }
        public LeaveApplicationBuilder reason(String reason) { this.reason = reason; return this; }
        public LeaveApplicationBuilder submittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; return this; }
        public LeaveApplicationBuilder currentStage(LeaveStage currentStage) { this.currentStage = currentStage; return this; }
        public LeaveApplicationBuilder mentorStatus(ApprovalStatus mentorStatus) { this.mentorStatus = mentorStatus; return this; }
        public LeaveApplicationBuilder mentorRemarks(String mentorRemarks) { this.mentorRemarks = mentorRemarks; return this; }
        public LeaveApplicationBuilder mentorActedAt(LocalDateTime mentorActedAt) { this.mentorActedAt = mentorActedAt; return this; }
        public LeaveApplicationBuilder hodStatus(ApprovalStatus hodStatus) { this.hodStatus = hodStatus; return this; }
        public LeaveApplicationBuilder hodRemarks(String hodRemarks) { this.hodRemarks = hodRemarks; return this; }
        public LeaveApplicationBuilder hodActedAt(LocalDateTime hodActedAt) { this.hodActedAt = hodActedAt; return this; }
        public LeaveApplicationBuilder wardenStatus(ApprovalStatus wardenStatus) { this.wardenStatus = wardenStatus; return this; }
        public LeaveApplicationBuilder wardenRemarks(String wardenRemarks) { this.wardenRemarks = wardenRemarks; return this; }
        public LeaveApplicationBuilder wardenActedAt(LocalDateTime wardenActedAt) { this.wardenActedAt = wardenActedAt; return this; }
        public LeaveApplicationBuilder finalStatus(ApprovalStatus finalStatus) { this.finalStatus = finalStatus; return this; }

        public LeaveApplication build() {
            return new LeaveApplication(id, student, fromDate, toDate, reason, submittedAt, currentStage, mentorStatus,
                    mentorRemarks, mentorActedAt, hodStatus, hodRemarks, hodActedAt, wardenStatus, wardenRemarks, wardenActedAt, finalStatus);
        }
    }
}
