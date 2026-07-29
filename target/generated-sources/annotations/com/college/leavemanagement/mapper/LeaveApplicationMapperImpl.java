package com.college.leavemanagement.mapper;

import com.college.leavemanagement.dto.leave.ApprovalHistoryDto;
import com.college.leavemanagement.dto.leave.LeaveApplicationDto;
import com.college.leavemanagement.entity.ApprovalHistory;
import com.college.leavemanagement.entity.LeaveApplication;
import com.college.leavemanagement.entity.Student;
import com.college.leavemanagement.enums.DeptSection;
import com.college.leavemanagement.enums.HostelBlockName;
import com.college.leavemanagement.enums.StudentType;
import com.college.leavemanagement.enums.Year;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-29T19:33:52+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class LeaveApplicationMapperImpl implements LeaveApplicationMapper {

    @Override
    public LeaveApplicationDto toDto(LeaveApplication leaveApplication) {
        if ( leaveApplication == null ) {
            return null;
        }

        LeaveApplicationDto leaveApplicationDto = new LeaveApplicationDto();

        leaveApplicationDto.setStudentId( leaveApplicationStudentId( leaveApplication ) );
        leaveApplicationDto.setStudentName( leaveApplicationStudentName( leaveApplication ) );
        leaveApplicationDto.setRegNo( leaveApplicationStudentRegNo( leaveApplication ) );
        leaveApplicationDto.setYear( leaveApplicationStudentYear( leaveApplication ) );
        leaveApplicationDto.setDeptSection( leaveApplicationStudentDeptSection( leaveApplication ) );
        leaveApplicationDto.setStudentType( leaveApplicationStudentStudentType( leaveApplication ) );
        leaveApplicationDto.setHostelBlock( leaveApplicationStudentHostelBlock( leaveApplication ) );
        leaveApplicationDto.setRoomNumber( leaveApplicationStudentRoomNumber( leaveApplication ) );
        leaveApplicationDto.setFatherContact( leaveApplicationStudentFatherContact( leaveApplication ) );
        leaveApplicationDto.setMotherContact( leaveApplicationStudentMotherContact( leaveApplication ) );
        leaveApplicationDto.setId( leaveApplication.getId() );
        leaveApplicationDto.setFromDate( leaveApplication.getFromDate() );
        leaveApplicationDto.setToDate( leaveApplication.getToDate() );
        leaveApplicationDto.setReturnDate( leaveApplication.getReturnDate() );
        leaveApplicationDto.setFromTime( leaveApplication.getFromTime() );
        leaveApplicationDto.setToTime( leaveApplication.getToTime() );
        leaveApplicationDto.setAddressDuringLeave( leaveApplication.getAddressDuringLeave() );
        leaveApplicationDto.setReason( leaveApplication.getReason() );
        leaveApplicationDto.setSubmittedAt( leaveApplication.getSubmittedAt() );
        leaveApplicationDto.setCurrentStage( leaveApplication.getCurrentStage() );
        leaveApplicationDto.setMentorStatus( leaveApplication.getMentorStatus() );
        leaveApplicationDto.setMentorRemarks( leaveApplication.getMentorRemarks() );
        leaveApplicationDto.setMentorActedAt( leaveApplication.getMentorActedAt() );
        leaveApplicationDto.setHodStatus( leaveApplication.getHodStatus() );
        leaveApplicationDto.setHodRemarks( leaveApplication.getHodRemarks() );
        leaveApplicationDto.setHodActedAt( leaveApplication.getHodActedAt() );
        leaveApplicationDto.setWardenStatus( leaveApplication.getWardenStatus() );
        leaveApplicationDto.setWardenRemarks( leaveApplication.getWardenRemarks() );
        leaveApplicationDto.setWardenActedAt( leaveApplication.getWardenActedAt() );
        leaveApplicationDto.setFinalStatus( leaveApplication.getFinalStatus() );

        return leaveApplicationDto;
    }

    @Override
    public ApprovalHistoryDto toHistoryDto(ApprovalHistory history) {
        if ( history == null ) {
            return null;
        }

        ApprovalHistoryDto approvalHistoryDto = new ApprovalHistoryDto();

        approvalHistoryDto.setId( history.getId() );
        approvalHistoryDto.setApproverRole( history.getApproverRole() );
        approvalHistoryDto.setApproverId( history.getApproverId() );
        approvalHistoryDto.setApproverName( history.getApproverName() );
        approvalHistoryDto.setStatus( history.getStatus() );
        approvalHistoryDto.setRemarks( history.getRemarks() );
        approvalHistoryDto.setTimestamp( history.getTimestamp() );

        return approvalHistoryDto;
    }

    @Override
    public List<ApprovalHistoryDto> toHistoryDtoList(List<ApprovalHistory> historyList) {
        if ( historyList == null ) {
            return null;
        }

        List<ApprovalHistoryDto> list = new ArrayList<ApprovalHistoryDto>( historyList.size() );
        for ( ApprovalHistory approvalHistory : historyList ) {
            list.add( toHistoryDto( approvalHistory ) );
        }

        return list;
    }

    private Long leaveApplicationStudentId(LeaveApplication leaveApplication) {
        Student student = leaveApplication.getStudent();
        if ( student == null ) {
            return null;
        }
        return student.getId();
    }

    private String leaveApplicationStudentName(LeaveApplication leaveApplication) {
        Student student = leaveApplication.getStudent();
        if ( student == null ) {
            return null;
        }
        return student.getName();
    }

    private String leaveApplicationStudentRegNo(LeaveApplication leaveApplication) {
        Student student = leaveApplication.getStudent();
        if ( student == null ) {
            return null;
        }
        return student.getRegNo();
    }

    private Year leaveApplicationStudentYear(LeaveApplication leaveApplication) {
        Student student = leaveApplication.getStudent();
        if ( student == null ) {
            return null;
        }
        return student.getYear();
    }

    private DeptSection leaveApplicationStudentDeptSection(LeaveApplication leaveApplication) {
        Student student = leaveApplication.getStudent();
        if ( student == null ) {
            return null;
        }
        return student.getDeptSection();
    }

    private StudentType leaveApplicationStudentStudentType(LeaveApplication leaveApplication) {
        Student student = leaveApplication.getStudent();
        if ( student == null ) {
            return null;
        }
        return student.getStudentType();
    }

    private HostelBlockName leaveApplicationStudentHostelBlock(LeaveApplication leaveApplication) {
        Student student = leaveApplication.getStudent();
        if ( student == null ) {
            return null;
        }
        return student.getHostelBlock();
    }

    private String leaveApplicationStudentRoomNumber(LeaveApplication leaveApplication) {
        Student student = leaveApplication.getStudent();
        if ( student == null ) {
            return null;
        }
        return student.getRoomNumber();
    }

    private String leaveApplicationStudentFatherContact(LeaveApplication leaveApplication) {
        Student student = leaveApplication.getStudent();
        if ( student == null ) {
            return null;
        }
        return student.getFatherContact();
    }

    private String leaveApplicationStudentMotherContact(LeaveApplication leaveApplication) {
        Student student = leaveApplication.getStudent();
        if ( student == null ) {
            return null;
        }
        return student.getMotherContact();
    }
}
