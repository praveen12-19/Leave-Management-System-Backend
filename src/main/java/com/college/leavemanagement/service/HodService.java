package com.college.leavemanagement.service;

import com.college.leavemanagement.dto.hod.HodDto;
import com.college.leavemanagement.dto.mentor.MentorDto;
import com.college.leavemanagement.dto.mentor.MentorUpdateDto;
import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.entity.Hod;
import com.college.leavemanagement.entity.HodDepartmentAssignment;
import com.college.leavemanagement.entity.Mentor;
import com.college.leavemanagement.exception.ResourceNotFoundException;
import com.college.leavemanagement.mapper.HodMapper;
import com.college.leavemanagement.mapper.MentorMapper;
import com.college.leavemanagement.mapper.StudentMapper;
import com.college.leavemanagement.repository.HodRepository;
import com.college.leavemanagement.repository.MentorRepository;
import com.college.leavemanagement.repository.StudentRepository;
import com.college.leavemanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HodService {

    private final HodRepository hodRepository;
    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final HodMapper hodMapper;
    private final MentorMapper mentorMapper;
    private final StudentMapper studentMapper;
    private final AuditLogService auditLogService;

    public HodService(HodRepository hodRepository, MentorRepository mentorRepository,
                      StudentRepository studentRepository, UserRepository userRepository,
                      HodMapper hodMapper, MentorMapper mentorMapper,
                      StudentMapper studentMapper, AuditLogService auditLogService) {
        this.hodRepository = hodRepository;
        this.mentorRepository = mentorRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.hodMapper = hodMapper;
        this.mentorMapper = mentorMapper;
        this.studentMapper = studentMapper;
        this.auditLogService = auditLogService;
    }

    public HodDto getHodProfile(Long hodId) {
        Hod hod = hodRepository.findById(hodId)
                .orElseThrow(() -> new ResourceNotFoundException("HOD not found with ID: " + hodId));
        return hodMapper.toDto(hod);
    }

    public List<HodDepartmentAssignment> getManagedDepartments(Long hodId) {
        Hod hod = hodRepository.findById(hodId)
                .orElseThrow(() -> new ResourceNotFoundException("HOD not found with ID: " + hodId));
        return new ArrayList<>(hod.getManagedDepartmentAssignments());
    }

    public List<StudentProfileDto> getStudentsByDeptSectionAndYear(Long hodId, String deptSectionYear) {
        Hod hod = hodRepository.findById(hodId)
                .orElseThrow(() -> new ResourceNotFoundException("HOD not found with ID: " + hodId));

        List<StudentProfileDto> result = new ArrayList<>();
        for (HodDepartmentAssignment assignment : hod.getManagedDepartmentAssignments()) {
            result.addAll(
                    studentRepository.findByDeptSectionAndYearOrderByRegNoAsc(assignment.getDeptSection(), assignment.getYear())
                            .stream()
                            .map(studentMapper::toDto)
                            .collect(Collectors.toList())
            );
        }
        return result;
    }

    public List<MentorDto> getMentorsForHod(Long hodId) {
        Hod hod = hodRepository.findById(hodId)
                .orElseThrow(() -> new ResourceNotFoundException("HOD not found with ID: " + hodId));

        Set<HodDepartmentAssignment> assignments = hod.getManagedDepartmentAssignments();
        List<Mentor> mentors = new ArrayList<>();
        for (HodDepartmentAssignment assignment : assignments) {
            mentors.addAll(mentorRepository.findByDeptSection(assignment.getDeptSection()));
        }

        return mentors.stream()
                .distinct()
                .map(mentorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MentorDto updateMentorDetails(Long mentorId, MentorUpdateDto dto) {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with ID: " + mentorId));

        if (dto.getName() != null && !dto.getName().isBlank()) mentor.setName(dto.getName());
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && mentor.getUser() != null) {
            mentor.getUser().setEmail(dto.getEmail());
            userRepository.save(mentor.getUser());
        }
        if (dto.getDeptSection() != null) mentor.setDeptSection(dto.getDeptSection());
        if (dto.getAssignedYear() != null) mentor.setAssignedYear(dto.getAssignedYear());

        Mentor updated = mentorRepository.save(mentor);
        auditLogService.logAction("UPDATE_MENTOR_PROFILE", "Mentor", updated.getId(), 
                "Updated mentor details by HOD");
        return mentorMapper.toDto(updated);
    }
}
