package com.college.leavemanagement.service;

import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.entity.LeaveApplication;
import com.college.leavemanagement.entity.Student;
import com.college.leavemanagement.enums.ApprovalStatus;
import com.college.leavemanagement.exception.ResourceNotFoundException;
import com.college.leavemanagement.mapper.StudentMapper;
import com.college.leavemanagement.repository.LeaveApplicationRepository;
import com.college.leavemanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, LeaveApplicationRepository leaveApplicationRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.studentMapper = studentMapper;
    }

    public StudentProfileDto getStudentProfile(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        List<LeaveApplication> leaves = leaveApplicationRepository.findByStudentIdOrderBySubmittedAtDesc(studentId);

        long totalSubmitted = leaves.size();
        long totalApproved = leaves.stream().filter(l -> l.getFinalStatus() == ApprovalStatus.APPROVED).count();

        StudentProfileDto dto = studentMapper.toDto(student);
        dto.setTotalLeavesSubmitted(totalSubmitted);
        dto.setTotalLeavesApproved(totalApproved);
        return dto;
    }
}
