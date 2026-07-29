package com.college.leavemanagement.dto.leave;

import com.college.leavemanagement.validation.ValidDateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@ValidDateRange
public class LeaveSubmitRequest {

    @NotNull(message = "From Date is required")
    private LocalDate fromDate;

    @NotNull(message = "To Date is required")
    private LocalDate toDate;

    private LocalDate returnDate;
    private String fromTime;
    private String toTime;
    private String addressDuringLeave;

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

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public String getFromTime() { return fromTime; }
    public void setFromTime(String fromTime) { this.fromTime = fromTime; }

    public String getToTime() { return toTime; }
    public void setToTime(String toTime) { this.toTime = toTime; }

    public String getAddressDuringLeave() { return addressDuringLeave; }
    public void setAddressDuringLeave(String addressDuringLeave) { this.addressDuringLeave = addressDuringLeave; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
