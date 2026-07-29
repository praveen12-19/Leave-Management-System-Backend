package com.college.leavemanagement.dto.leave;

import com.college.leavemanagement.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class LeaveApplicationDto {
    private Long id;

    private Long studentId;
    private String studentName;
    private String regNo;
    private Year year;
    private DeptSection deptSection;
    private StudentType studentType;
    private HostelBlockName hostelBlock;
    private String roomNumber;
    private String fatherContact;
    private String motherContact;

    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDate returnDate;
    private String fromTime;
    private String toTime;
    private String addressDuringLeave;
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

    private List<ApprovalHistoryDto> history;

    public LeaveApplicationDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }

    public Year getYear() { return year; }
    public void setYear(Year year) { this.year = year; }

    public DeptSection getDeptSection() { return deptSection; }
    public void setDeptSection(DeptSection deptSection) { this.deptSection = deptSection; }

    public StudentType getStudentType() { return studentType; }
    public void setStudentType(StudentType studentType) { this.studentType = studentType; }

    public HostelBlockName getHostelBlock() { return hostelBlock; }
    public void setHostelBlock(HostelBlockName hostelBlock) { this.hostelBlock = hostelBlock; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getFatherContact() { return fatherContact; }
    public void setFatherContact(String fatherContact) { this.fatherContact = fatherContact; }

    public String getMotherContact() { return motherContact; }
    public void setMotherContact(String motherContact) { this.motherContact = motherContact; }

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

    public List<ApprovalHistoryDto> getHistory() { return history; }
    public void setHistory(List<ApprovalHistoryDto> history) { this.history = history; }
}
