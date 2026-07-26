package com.college.leavemanagement.entity;

import com.college.leavemanagement.enums.ApprovalStatus;
import com.college.leavemanagement.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_history")
public class ApprovalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_application_id", nullable = false)
    private LeaveApplication leaveApplication;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role approverRole;

    private Long approverId;
    private String approverName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status;

    private String remarks;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public ApprovalHistory() {}

    public ApprovalHistory(Long id, LeaveApplication leaveApplication, Role approverRole, Long approverId, String approverName, ApprovalStatus status, String remarks, LocalDateTime timestamp) {
        this.id = id;
        this.leaveApplication = leaveApplication;
        this.approverRole = approverRole;
        this.approverId = approverId;
        this.approverName = approverName;
        this.status = status;
        this.remarks = remarks;
        this.timestamp = timestamp;
    }

    public static ApprovalHistoryBuilder builder() {
        return new ApprovalHistoryBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LeaveApplication getLeaveApplication() { return leaveApplication; }
    public void setLeaveApplication(LeaveApplication leaveApplication) { this.leaveApplication = leaveApplication; }

    public Role getApproverRole() { return approverRole; }
    public void setApproverRole(Role approverRole) { this.approverRole = approverRole; }

    public Long getApproverId() { return approverId; }
    public void setApproverId(Long approverId) { this.approverId = approverId; }

    public String getApproverName() { return approverName; }
    public void setApproverName(String approverName) { this.approverName = approverName; }

    public ApprovalStatus getStatus() { return status; }
    public void setStatus(ApprovalStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class ApprovalHistoryBuilder {
        private Long id;
        private LeaveApplication leaveApplication;
        private Role approverRole;
        private Long approverId;
        private String approverName;
        private ApprovalStatus status;
        private String remarks;
        private LocalDateTime timestamp;

        ApprovalHistoryBuilder() {}

        public ApprovalHistoryBuilder id(Long id) { this.id = id; return this; }
        public ApprovalHistoryBuilder leaveApplication(LeaveApplication leaveApplication) { this.leaveApplication = leaveApplication; return this; }
        public ApprovalHistoryBuilder approverRole(Role approverRole) { this.approverRole = approverRole; return this; }
        public ApprovalHistoryBuilder approverId(Long approverId) { this.approverId = approverId; return this; }
        public ApprovalHistoryBuilder approverName(String approverName) { this.approverName = approverName; return this; }
        public ApprovalHistoryBuilder status(ApprovalStatus status) { this.status = status; return this; }
        public ApprovalHistoryBuilder remarks(String remarks) { this.remarks = remarks; return this; }
        public ApprovalHistoryBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public ApprovalHistory build() {
            return new ApprovalHistory(id, leaveApplication, approverRole, approverId, approverName, status, remarks, timestamp);
        }
    }
}
