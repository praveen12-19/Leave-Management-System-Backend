package com.college.leavemanagement.service;

import com.college.leavemanagement.dto.hod.HodDto;
import com.college.leavemanagement.dto.mentor.MentorCreateDto;
import com.college.leavemanagement.dto.mentor.MentorDto;
import com.college.leavemanagement.dto.mentor.MentorUpdateDto;
import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.entity.Hod;
import com.college.leavemanagement.entity.HodDepartmentAssignment;
import com.college.leavemanagement.entity.Mentor;
import com.college.leavemanagement.entity.User;
import com.college.leavemanagement.enums.Role;
import com.college.leavemanagement.exception.ResourceNotFoundException;
import com.college.leavemanagement.mapper.HodMapper;
import com.college.leavemanagement.mapper.MentorMapper;
import com.college.leavemanagement.mapper.StudentMapper;
import com.college.leavemanagement.repository.HodRepository;
import com.college.leavemanagement.repository.MentorRepository;
import com.college.leavemanagement.repository.StudentRepository;
import com.college.leavemanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HodService {

    private final HodRepository hodRepository;
    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HodMapper hodMapper;
    private final MentorMapper mentorMapper;
    private final StudentMapper studentMapper;
    private final AuditLogService auditLogService;

    public HodService(HodRepository hodRepository, MentorRepository mentorRepository,
                      StudentRepository studentRepository, UserRepository userRepository,
                      PasswordEncoder passwordEncoder, HodMapper hodMapper,
                      MentorMapper mentorMapper, StudentMapper studentMapper,
                      AuditLogService auditLogService) {
        this.hodRepository = hodRepository;
        this.mentorRepository = mentorRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        Set<HodDepartmentAssignment> rawAssignments = hod.getManagedDepartmentAssignments();
        List<HodDepartmentAssignment> result = new ArrayList<>();

        for (HodDepartmentAssignment raw : rawAssignments) {
            if (raw.getDeptSection() == null) continue;
            if (raw.getYear() != null) {
                result.add(raw);
            } else {
                // Expand across academic years (FIRST, SECOND, THIRD, FINAL)
                for (com.college.leavemanagement.enums.Year y : com.college.leavemanagement.enums.Year.values()) {
                    result.add(new HodDepartmentAssignment(raw.getDeptSection(), y));
                }
            }
        }

        return result;
    }

    public List<StudentProfileDto> getStudentsByDeptSectionAndYear(Long hodId, String deptSectionYear) {
        Hod hod = hodRepository.findById(hodId)
                .orElseThrow(() -> new ResourceNotFoundException("HOD not found with ID: " + hodId));

        List<com.college.leavemanagement.entity.Student> students = new ArrayList<>();
        String upperStr = (deptSectionYear != null) ? deptSectionYear.toUpperCase().trim() : "";

        Set<HodDepartmentAssignment> assignments = hod.getManagedDepartmentAssignments();
        List<com.college.leavemanagement.enums.DeptSection> hodDepts = assignments.stream()
                .map(HodDepartmentAssignment::getDeptSection)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        boolean isAidsQuery = upperStr.startsWith("AIDS");

        if (isAidsQuery) {
            com.college.leavemanagement.enums.Year targetYear = null;
            if (upperStr.endsWith("_FIRST") || upperStr.endsWith("_1")) targetYear = com.college.leavemanagement.enums.Year.FIRST;
            else if (upperStr.endsWith("_SECOND") || upperStr.endsWith("_2")) targetYear = com.college.leavemanagement.enums.Year.SECOND;
            else if (upperStr.endsWith("_THIRD") || upperStr.endsWith("_3")) targetYear = com.college.leavemanagement.enums.Year.THIRD;
            else if (upperStr.endsWith("_FINAL") || upperStr.endsWith("_4")) targetYear = com.college.leavemanagement.enums.Year.FINAL;

            boolean isSectionA = upperStr.contains("AIDS_A") || upperStr.contains("AIDS-A");
            boolean isSectionB = upperStr.contains("AIDS_B") || upperStr.contains("AIDS-B");

            List<com.college.leavemanagement.enums.DeptSection> deptsToSearch = new ArrayList<>();
            if (isSectionA) {
                deptsToSearch.add(com.college.leavemanagement.enums.DeptSection.AIDS_A);
                deptsToSearch.add(com.college.leavemanagement.enums.DeptSection.AIDS);
            } else if (isSectionB) {
                deptsToSearch.add(com.college.leavemanagement.enums.DeptSection.AIDS_B);
                deptsToSearch.add(com.college.leavemanagement.enums.DeptSection.AIDS);
            } else {
                deptsToSearch.add(com.college.leavemanagement.enums.DeptSection.AIDS);
                deptsToSearch.add(com.college.leavemanagement.enums.DeptSection.AIDS_A);
                deptsToSearch.add(com.college.leavemanagement.enums.DeptSection.AIDS_B);
            }

            for (com.college.leavemanagement.enums.DeptSection ds : deptsToSearch) {
                if (targetYear != null) {
                    students.addAll(studentRepository.findByDeptSectionAndYearOrderByRegNoAsc(ds, targetYear));
                } else {
                    students.addAll(studentRepository.findByDeptSectionOrderByRegNoAsc(ds));
                }
            }
        } else {
            com.college.leavemanagement.enums.DeptSection targetDept = null;
            com.college.leavemanagement.enums.Year targetYear = null;

            String[] parts = upperStr.split("_");
            if (parts.length >= 2) {
                targetDept = com.college.leavemanagement.enums.DeptSection.fromString(parts[0]);
                try {
                    targetYear = com.college.leavemanagement.enums.Year.valueOf(parts[1]);
                } catch (Exception ignored) {}
            } else {
                targetDept = com.college.leavemanagement.enums.DeptSection.fromString(upperStr);
            }

            if (targetDept != null && targetDept != com.college.leavemanagement.enums.DeptSection.OTHER && targetYear != null) {
                students.addAll(studentRepository.findByDeptSectionAndYearOrderByRegNoAsc(targetDept, targetYear));
            } else if (targetDept != null && targetDept != com.college.leavemanagement.enums.DeptSection.OTHER) {
                students.addAll(studentRepository.findByDeptSectionOrderByRegNoAsc(targetDept));
            } else {
                for (com.college.leavemanagement.enums.DeptSection ds : hodDepts) {
                    students.addAll(studentRepository.findByDeptSectionOrderByRegNoAsc(ds));
                }
            }
        }

        if (students.isEmpty()) {
            for (HodDepartmentAssignment a : assignments) {
                if (a.getDeptSection() != null) {
                    students.addAll(studentRepository.findByDeptSectionOrderByRegNoAsc(a.getDeptSection()));
                    if (a.getDeptSection() == com.college.leavemanagement.enums.DeptSection.AIDS
                            || a.getDeptSection() == com.college.leavemanagement.enums.DeptSection.AIDS_A
                            || a.getDeptSection() == com.college.leavemanagement.enums.DeptSection.AIDS_B) {
                        students.addAll(studentRepository.findByDeptSectionOrderByRegNoAsc(com.college.leavemanagement.enums.DeptSection.AIDS));
                        students.addAll(studentRepository.findByDeptSectionOrderByRegNoAsc(com.college.leavemanagement.enums.DeptSection.AIDS_A));
                        students.addAll(studentRepository.findByDeptSectionOrderByRegNoAsc(com.college.leavemanagement.enums.DeptSection.AIDS_B));
                    }
                }
            }
        }

        return students.stream()
                .distinct()
                .map(s -> {
                    if (s.getMentor() != null) {
                        if (s.getMentor().getDeptSection() != s.getDeptSection() || s.getMentor().getAssignedYear() != s.getYear()) {
                            s.setMentor(null);
                            studentRepository.save(s);
                        }
                    }
                    StudentProfileDto dto = studentMapper.toDto(s);
                    List<Mentor> classMentors = mentorRepository.findByDeptSectionAndAssignedYear(s.getDeptSection(), s.getYear());
                    if (classMentors != null && !classMentors.isEmpty()) {
                        String names = classMentors.stream().map(Mentor::getName).distinct().collect(Collectors.joining(", "));
                        dto.setMentorName(names);
                        if (classMentors.get(0) != null) dto.setMentorId(classMentors.get(0).getId());
                    } else if (s.getMentor() != null && s.getMentor().getDeptSection() == s.getDeptSection() && s.getMentor().getAssignedYear() == s.getYear()) {
                        dto.setMentorName(s.getMentor().getName());
                    } else {
                        dto.setMentorName("Unassigned");
                        dto.setMentorId(null);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<MentorDto> getMentorsForHod(Long hodId) {
        Hod hod = hodRepository.findById(hodId)
                .orElseThrow(() -> new ResourceNotFoundException("HOD not found with ID: " + hodId));

        List<Mentor> mentors = mentorRepository.findAll();
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

        // 1. Unlink students previously assigned to this mentor if they do not belong to the new class
        List<com.college.leavemanagement.entity.Student> previousStudents = studentRepository.findByMentorIdOrderByRegNoAsc(updated.getId());
        for (com.college.leavemanagement.entity.Student ps : previousStudents) {
            if (ps.getDeptSection() != updated.getDeptSection() || ps.getYear() != updated.getAssignedYear()) {
                ps.setMentor(null);
                studentRepository.save(ps);
            }
        }

        // 2. Link all students of the new class section & academic year to this mentor
        if (updated.getDeptSection() != null && updated.getAssignedYear() != null) {
            List<com.college.leavemanagement.entity.Student> classStudents = studentRepository.findByDeptSectionAndYearOrderByRegNoAsc(
                    updated.getDeptSection(), updated.getAssignedYear());
            for (com.college.leavemanagement.entity.Student s : classStudents) {
                s.setMentor(updated);
                studentRepository.save(s);
            }
        }
        auditLogService.logAction("UPDATE_MENTOR_PROFILE", "Mentor", updated.getId(), 
                "Updated mentor details by HOD");
        return mentorMapper.toDto(updated);
    }

    @Transactional
    public MentorDto createMentor(MentorCreateDto dto) {
        String username = (dto.getUsername() != null && !dto.getUsername().isBlank())
                ? dto.getUsername()
                : dto.getName().toLowerCase().replaceAll("\\s+", "_");
        if (userRepository.existsByUsername(username)) {
            username = username + "_" + (System.currentTimeMillis() % 1000);
        }

        String emailVal = (dto.getEmail() != null && !dto.getEmail().isBlank()) ? dto.getEmail() : username + "@gmail.com";
        String passwordVal = (dto.getPassword() != null && !dto.getPassword().isBlank()) ? dto.getPassword() : "mentor123";

        User user = User.builder()
                .username(username)
                .email(emailVal)
                .password(passwordEncoder.encode(passwordVal))
                .role(Role.MENTOR)
                .build();

        Mentor mentor = Mentor.builder()
                .name(dto.getName())
                .deptSection(dto.getDeptSection())
                .assignedYear(dto.getAssignedYear())
                .user(user)
                .build();

        Mentor saved = mentorRepository.save(mentor);
        user.setLinkedProfileId(saved.getId());
        userRepository.save(user);

        if (saved.getDeptSection() != null && saved.getAssignedYear() != null) {
            List<com.college.leavemanagement.entity.Student> classStudents = studentRepository.findByDeptSectionAndYearOrderByRegNoAsc(
                    saved.getDeptSection(), saved.getAssignedYear());
            for (com.college.leavemanagement.entity.Student s : classStudents) {
                s.setMentor(saved);
                studentRepository.save(s);
            }
        }

        auditLogService.logAction("CREATE_MENTOR", "Mentor", saved.getId(), "Created mentor by HOD: " + saved.getName());
        return mentorMapper.toDto(saved);
    }
}
