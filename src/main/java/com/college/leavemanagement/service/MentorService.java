package com.college.leavemanagement.service;

import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.dto.student.StudentUpdateDto;
import com.college.leavemanagement.entity.Mentor;
import com.college.leavemanagement.entity.Student;
import com.college.leavemanagement.exception.ResourceNotFoundException;
import com.college.leavemanagement.mapper.StudentMapper;
import com.college.leavemanagement.repository.MentorRepository;
import com.college.leavemanagement.repository.StudentRepository;
import com.college.leavemanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MentorService {

    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentMapper studentMapper;
    private final StudentService studentService;
    private final AuditLogService auditLogService;

    public MentorService(MentorRepository mentorRepository, StudentRepository studentRepository,
                         UserRepository userRepository, StudentMapper studentMapper,
                         StudentService studentService, AuditLogService auditLogService) {
        this.mentorRepository = mentorRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.studentMapper = studentMapper;
        this.studentService = studentService;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public List<StudentProfileDto> getAssignedStudents(Long mentorId) {
        Mentor mentor = mentorRepository.findById(mentorId).orElse(null);
        if (mentor == null) {
            return List.of();
        }

        List<Student> students = new java.util.ArrayList<>(studentRepository.findByMentorIdOrderByRegNoAsc(mentorId));

        if (mentor.getDeptSection() != null && mentor.getAssignedYear() != null) {
            List<Student> classStudents = studentRepository.findByDeptSectionAndYearOrderByRegNoAsc(
                    mentor.getDeptSection(), mentor.getAssignedYear());
            for (Student s : classStudents) {
                if (s.getMentor() == null || !s.getMentor().getId().equals(mentor.getId())) {
                    s.setMentor(mentor);
                    studentRepository.save(s);
                }
                if (students.stream().noneMatch(existing -> existing.getId().equals(s.getId()))) {
                    students.add(s);
                }
            }
        }

        return students.stream()
                .map(s -> studentService.getStudentProfile(s.getId()))
                .collect(Collectors.toList());
    }

    public StudentProfileDto getStudentDetails(Long studentId) {
        return studentService.getStudentProfile(studentId);
    }

    public List<StudentProfileDto> searchStudents(String query) {
        return studentRepository.findByRegNoContainingIgnoreCaseOrNameContainingIgnoreCase(query, query)
                .stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentProfileDto updateStudentDetails(Long studentId, StudentUpdateDto dto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        if (dto.getName() != null && !dto.getName().isBlank()) student.setName(dto.getName());
        if (dto.getRegNo() != null && !dto.getRegNo().isBlank()) {
            student.setRegNo(dto.getRegNo());
            if (student.getUser() != null) {
                student.getUser().setUsername(dto.getRegNo().toLowerCase());
                userRepository.save(student.getUser());
            }
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && student.getUser() != null) {
            student.getUser().setEmail(dto.getEmail());
            userRepository.save(student.getUser());
        }
        if (dto.getYear() != null) student.setYear(dto.getYear());
        if (dto.getDeptSection() != null) student.setDeptSection(dto.getDeptSection());
        if (dto.getStudentType() != null) student.setStudentType(dto.getStudentType());
        if (dto.getGender() != null && !dto.getGender().isBlank()) student.setGender(dto.getGender());
        if (dto.getFatherContact() != null) student.setFatherContact(dto.getFatherContact());
        if (dto.getMotherContact() != null) student.setMotherContact(dto.getMotherContact());
        if (dto.getHostelBlock() != null) student.setHostelBlock(dto.getHostelBlock());
        if (dto.getRoomNumber() != null) student.setRoomNumber(dto.getRoomNumber());

        if (dto.getMentorId() != null) {
            Mentor newMentor = mentorRepository.findById(dto.getMentorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with ID: " + dto.getMentorId()));
            student.setMentor(newMentor);
        }

        Student updated = studentRepository.save(student);
        auditLogService.logAction("UPDATE_STUDENT_PROFILE", "Student", updated.getId(), "Updated student details");
        return studentMapper.toDto(updated);
    }
}
