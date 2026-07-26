package com.college.leavemanagement.dto.leave;

import com.college.leavemanagement.enums.ApprovalStatus;
import jakarta.validation.constraints.NotNull;

public class LeaveReviewRequest {

    @NotNull(message = "Approval status is required (APPROVED or REJECTED)")
    private ApprovalStatus status;

    private String remarks;

    public LeaveReviewRequest() {}

    public LeaveReviewRequest(ApprovalStatus status, String remarks) {
        this.status = status;
        this.remarks = remarks;
    }

    public ApprovalStatus getStatus() { return status; }
    public void setStatus(ApprovalStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
