package com.college.leavemanagement.dto.leave;

import com.college.leavemanagement.enums.ApprovalStatus;
import com.college.leavemanagement.enums.Role;

import java.time.LocalDateTime;

public class ApprovalHistoryDto {
    private Long id;
    private Role approverRole;
    private Long approverId;
    private String approverName;
    private ApprovalStatus status;
    private String remarks;
    private LocalDateTime timestamp;

    public ApprovalHistoryDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
}
