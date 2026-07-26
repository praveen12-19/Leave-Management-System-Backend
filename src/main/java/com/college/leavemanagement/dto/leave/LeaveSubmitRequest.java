package com.college.leavemanagement.dto.leave;

import com.college.leavemanagement.validation.ValidDateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@ValidDateRange
public class LeaveSubmitRequest {

    @NotNull(message = "From Date is required")
    private LocalDate fromDate;

    @NotNull(message = "To Date (Return Date) is required")
    private LocalDate toDate;

    @NotBlank(message = "Reason for leave is required")
    private String reason;

    public LeaveSubmitRequest() {}

    public LeaveSubmitRequest(LocalDate fromDate, LocalDate toDate, String reason) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
    }

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
