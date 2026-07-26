package com.college.leavemanagement.mapper;

import com.college.leavemanagement.dto.leave.ApprovalHistoryDto;
import com.college.leavemanagement.dto.leave.LeaveApplicationDto;
import com.college.leavemanagement.entity.ApprovalHistory;
import com.college.leavemanagement.entity.LeaveApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeaveApplicationMapper {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.name", target = "studentName")
    @Mapping(source = "student.regNo", target = "regNo")
    @Mapping(source = "student.year", target = "year")
    @Mapping(source = "student.deptSection", target = "deptSection")
    @Mapping(source = "student.studentType", target = "studentType")
    @Mapping(source = "student.hostelBlock", target = "hostelBlock")
    @Mapping(source = "student.roomNumber", target = "roomNumber")
    @Mapping(target = "history", ignore = true)
    LeaveApplicationDto toDto(LeaveApplication leaveApplication);

    ApprovalHistoryDto toHistoryDto(ApprovalHistory history);

    List<ApprovalHistoryDto> toHistoryDtoList(List<ApprovalHistory> historyList);
}
