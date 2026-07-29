package com.college.leavemanagement.service;

import com.college.leavemanagement.dto.hod.HodCreateDto;
import com.college.leavemanagement.dto.hod.HodDto;
import com.college.leavemanagement.dto.hod.HodUpdateDto;
import com.college.leavemanagement.dto.mentor.MentorCreateDto;
import com.college.leavemanagement.dto.mentor.MentorDto;
import com.college.leavemanagement.dto.mentor.MentorUpdateDto;
import com.college.leavemanagement.dto.student.StudentCreateDto;
import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.dto.student.StudentUpdateDto;
import com.college.leavemanagement.dto.warden.WardenCreateDto;
import com.college.leavemanagement.dto.warden.WardenDto;
import com.college.leavemanagement.dto.warden.WardenUpdateDto;
import com.college.leavemanagement.entity.*;
import com.college.leavemanagement.enums.Role;
import com.college.leavemanagement.enums.StudentType;
import com.college.leavemanagement.exception.BusinessException;
import com.college.leavemanagement.exception.ResourceNotFoundException;
import com.college.leavemanagement.mapper.HodMapper;
import com.college.leavemanagement.mapper.MentorMapper;
import com.college.leavemanagement.mapper.StudentMapper;
import com.college.leavemanagement.mapper.WardenMapper;
import com.college.leavemanagement.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;
    private final HodRepository hodRepository;
    private final WardenRepository wardenRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentMapper studentMapper;
    private final MentorMapper mentorMapper;
    private final HodMapper hodMapper;
    private final WardenMapper wardenMapper;
    private final AuditLogService auditLogService;

    public AdminService(UserRepository userRepository, StudentRepository studentRepository,
                        MentorRepository mentorRepository, HodRepository hodRepository,
                        WardenRepository wardenRepository, LeaveApplicationRepository leaveApplicationRepository,
                        NotificationRepository notificationRepository, PasswordEncoder passwordEncoder,
                        StudentMapper studentMapper, MentorMapper mentorMapper,
                        HodMapper hodMapper, WardenMapper wardenMapper,
                        AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.mentorRepository = mentorRepository;
        this.hodRepository = hodRepository;
        this.wardenRepository = wardenRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentMapper = studentMapper;
        this.mentorMapper = mentorMapper;
        this.hodMapper = hodMapper;
        this.wardenMapper = wardenMapper;
        this.auditLogService = auditLogService;
    }

    // --- STUDENT CRUD ---
    @Transactional
    public StudentProfileDto createStudent(StudentCreateDto dto) {
        String username = (dto.getUsername() != null && !dto.getUsername().isBlank()) 
                ? dto.getUsername() 
                : dto.getRegNo().toLowerCase();
        
        validateUsername(username);
        if (studentRepository.existsByRegNo(dto.getRegNo())) {
            throw new BusinessException("Register Number already exists: " + dto.getRegNo());
        }
        if (dto.getStudentType() == StudentType.HOSTELLER) {
            if (dto.getHostelBlock() == null || dto.getRoomNumber() == null) {
                throw new BusinessException("Hostel Block and Room Number are required for HOSTELLER");
            }
        }

        Mentor mentor = null;
        if (dto.getMentorId() != null) {
            mentor = mentorRepository.findById(dto.getMentorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with ID: " + dto.getMentorId()));
        }

        String studentEmail = (dto.getEmail() != null && !dto.getEmail().isBlank()) ? dto.getEmail() : username + "@gmail.com";
        User user = User.builder()
                .username(username)
                .email(studentEmail)
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.STUDENT)
                .build();

        Student student = Student.builder()
                .name(dto.getName())
                .regNo(dto.getRegNo())
                .year(dto.getYear())
                .deptSection(dto.getDeptSection())
                .studentType(dto.getStudentType())
                .gender(dto.getGender() != null && !dto.getGender().isBlank() ? dto.getGender() : "Male")
                .fatherContact(dto.getFatherContact())
                .motherContact(dto.getMotherContact())
                .hostelBlock(dto.getHostelBlock())
                .roomNumber(dto.getRoomNumber())
                .mentor(mentor)
                .user(user)
                .build();

        Student saved = studentRepository.save(student);
        user.setLinkedProfileId(saved.getId());
        userRepository.save(user);

        auditLogService.logAction("CREATE_STUDENT", "Student", saved.getId(), "Created student regNo: " + saved.getRegNo());
        return studentMapper.toDto(saved);
    }

    public List<StudentProfileDto> getAllStudents() {
        return studentRepository.findAll().stream()
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

    @Transactional
    public StudentProfileDto updateStudent(Long id, StudentUpdateDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            student.setName(dto.getName());
        }
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
            Mentor mentor = mentorRepository.findById(dto.getMentorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with ID: " + dto.getMentorId()));
            student.setMentor(mentor);
        }

        Student updated = studentRepository.save(student);
        auditLogService.logAction("UPDATE_STUDENT", "Student", updated.getId(), "Updated student details");
        return studentMapper.toDto(updated);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        // 1. Delete leave applications submitted by student
        List<LeaveApplication> leaves = leaveApplicationRepository.findByStudentIdOrderBySubmittedAtDesc(id);
        if (!leaves.isEmpty()) {
            leaveApplicationRepository.deleteAll(leaves);
            leaveApplicationRepository.flush();
        }

        // 2. Unlink mentor and user reference
        student.setMentor(null);
        User user = student.getUser();
        student.setUser(null);
        studentRepository.saveAndFlush(student);

        // 3. Native SQL delete notifications & user account
        if (user != null) {
            notificationRepository.deleteNotificationsByUserIdNative(user.getId());
            userRepository.deleteUserNative(user.getId());
        }

        // 4. Delete student entity
        studentRepository.delete(student);
        studentRepository.flush();
        auditLogService.logAction("DELETE_STUDENT", "Student", id, "Deleted student ID: " + id);
    }

    // --- MENTOR CRUD ---
    @Transactional
    public MentorDto createMentor(MentorCreateDto dto) {
        String username = (dto.getUsername() != null && !dto.getUsername().isBlank())
                ? dto.getUsername()
                : dto.getName().toLowerCase().replaceAll("\\s+", "_");
        validateUsername(username);

        String emailVal = (dto.getEmail() != null && !dto.getEmail().isBlank()) ? dto.getEmail() : username + "@gmail.com";
        User user = User.builder()
                .username(username)
                .email(emailVal)
                .password(passwordEncoder.encode(dto.getPassword()))
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
            List<Student> classStudents = studentRepository.findByDeptSectionAndYearOrderByRegNoAsc(
                    saved.getDeptSection(), saved.getAssignedYear());
            for (Student s : classStudents) {
                s.setMentor(saved);
                studentRepository.save(s);
            }
        }

        auditLogService.logAction("CREATE_MENTOR", "Mentor", saved.getId(), "Created mentor: " + saved.getName());
        return mentorMapper.toDto(saved);
    }

    public List<MentorDto> getAllMentors() {
        return mentorRepository.findAll().stream().map(mentorMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public MentorDto updateMentor(Long id, MentorUpdateDto dto) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with ID: " + id));

        if (dto.getName() != null && !dto.getName().isBlank()) mentor.setName(dto.getName());
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && mentor.getUser() != null) {
            User user = mentor.getUser();
            user.setEmail(dto.getEmail());
            userRepository.saveAndFlush(user);
        }
        if (dto.getDeptSection() != null) mentor.setDeptSection(dto.getDeptSection());
        if (dto.getAssignedYear() != null) mentor.setAssignedYear(dto.getAssignedYear());

        Mentor updated = mentorRepository.saveAndFlush(mentor);

        // 1. Unlink students previously assigned to this mentor if they do not belong to the new class
        List<Student> previousStudents = studentRepository.findByMentorIdOrderByRegNoAsc(updated.getId());
        for (Student ps : previousStudents) {
            if (ps.getDeptSection() != updated.getDeptSection() || ps.getYear() != updated.getAssignedYear()) {
                ps.setMentor(null);
                studentRepository.save(ps);
            }
        }

        // 2. Link all students of the new class section & academic year to this mentor
        if (updated.getDeptSection() != null && updated.getAssignedYear() != null) {
            List<Student> classStudents = studentRepository.findByDeptSectionAndYearOrderByRegNoAsc(
                    updated.getDeptSection(), updated.getAssignedYear());
            for (Student s : classStudents) {
                s.setMentor(updated);
                studentRepository.save(s);
            }
        }
        auditLogService.logAction("UPDATE_MENTOR", "Mentor", updated.getId(), "Updated mentor details");
        return mentorMapper.toDto(updated);
    }

    @Transactional
    public void deleteMentor(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with ID: " + id));

        Long userId = (mentor.getUser() != null) ? mentor.getUser().getId() : null;

        // 1. Bulk update database table `students` so mentor_id is set to NULL immediately via native SQL
        studentRepository.unlinkMentorNative(id);

        // 2. Unlink user and execute native SQL delete for notifications & user account
        mentor.setUser(null);
        mentorRepository.saveAndFlush(mentor);

        if (userId != null) {
            notificationRepository.deleteNotificationsByUserIdNative(userId);
            userRepository.deleteUserNative(userId);
        }

        // 3. Delete mentor profile & flush SQL queue
        mentorRepository.delete(mentor);
        mentorRepository.flush();
        auditLogService.logAction("DELETE_MENTOR", "Mentor", id, "Deleted mentor ID: " + id);
    }

    // --- HOD CRUD ---
    @Transactional
    public HodDto createHod(HodCreateDto dto) {
        String username = (dto.getUsername() != null && !dto.getUsername().isBlank())
                ? dto.getUsername()
                : dto.getName().toLowerCase().replaceAll("\\s+", "_");
        validateUsername(username);

        String emailVal = (dto.getEmail() != null && !dto.getEmail().isBlank()) ? dto.getEmail() : username + "@gmail.com";
        User user = User.builder()
                .username(username)
                .email(emailVal)
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.HOD)
                .build();

        Hod hod = Hod.builder()
                .name(dto.getName())
                .managedDepartmentAssignments(dto.getManagedDepartmentAssignments())
                .user(user)
                .build();

        Hod saved = hodRepository.save(hod);
        user.setLinkedProfileId(saved.getId());
        userRepository.save(user);

        auditLogService.logAction("CREATE_HOD", "Hod", saved.getId(), "Created HOD: " + saved.getName());
        return hodMapper.toDto(saved);
    }

    public List<HodDto> getAllHods() {
        return hodRepository.findAll().stream().map(hodMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public HodDto updateHod(Long id, HodUpdateDto dto) {
        Hod hod = hodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HOD not found with ID: " + id));

        if (dto.getName() != null && !dto.getName().isBlank()) hod.setName(dto.getName());
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && hod.getUser() != null) {
            User user = hod.getUser();
            user.setEmail(dto.getEmail());
            userRepository.saveAndFlush(user);
        }
        if (dto.getManagedDepartmentAssignments() != null) hod.setManagedDepartmentAssignments(dto.getManagedDepartmentAssignments());

        Hod updated = hodRepository.saveAndFlush(hod);
        auditLogService.logAction("UPDATE_HOD", "Hod", updated.getId(), "Updated HOD details");
        return hodMapper.toDto(updated);
    }

    @Transactional
    public void deleteHod(Long id) {
        Hod hod = hodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HOD not found with ID: " + id));

        if (hod.getManagedDepartmentAssignments() != null) {
            hod.getManagedDepartmentAssignments().clear();
            hodRepository.saveAndFlush(hod);
        }

        User user = hod.getUser();
        hod.setUser(null);
        hodRepository.saveAndFlush(hod);

        if (user != null) {
            notificationRepository.deleteNotificationsByUserIdNative(user.getId());
            userRepository.deleteUserNative(user.getId());
        }

        hodRepository.delete(hod);
        hodRepository.flush();
        auditLogService.logAction("DELETE_HOD", "Hod", id, "Deleted HOD ID: " + id);
    }

    // --- WARDEN CRUD ---
    @Transactional
    public WardenDto createWarden(WardenCreateDto dto) {
        String username = (dto.getUsername() != null && !dto.getUsername().isBlank())
                ? dto.getUsername()
                : dto.getName().toLowerCase().replaceAll("\\s+", "_");
        validateUsername(username);

        Role role = (dto.getAssignedBlock() == null) ? Role.CHIEF_WARDEN : Role.WARDEN;

        User user = User.builder()
                .username(username)
                .email(dto.getEmail() != null && !dto.getEmail().isBlank() ? dto.getEmail() : username + "@gmail.com")
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(role)
                .build();

        Warden warden = Warden.builder()
                .name(dto.getName())
                .assignedBlock(dto.getAssignedBlock())
                .user(user)
                .build();

        Warden saved = wardenRepository.save(warden);
        user.setLinkedProfileId(saved.getId());
        userRepository.save(user);

        auditLogService.logAction("CREATE_WARDEN", "Warden", saved.getId(), "Created warden/chief warden: " + saved.getName());
        return wardenMapper.toDto(saved);
    }

    public List<WardenDto> getAllWardens() {
        return wardenRepository.findAll().stream().map(wardenMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public WardenDto updateWarden(Long id, WardenUpdateDto dto) {
        Warden warden = wardenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warden not found with ID: " + id));

        if (dto.getName() != null && !dto.getName().isBlank()) warden.setName(dto.getName());
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && warden.getUser() != null) {
            User user = warden.getUser();
            user.setEmail(dto.getEmail());
            userRepository.saveAndFlush(user);
        }

        warden.setAssignedBlock(dto.getAssignedBlock());
        if (warden.getUser() != null) {
            Role newRole = (dto.getAssignedBlock() == null) ? Role.CHIEF_WARDEN : Role.WARDEN;
            warden.getUser().setRole(newRole);
            userRepository.saveAndFlush(warden.getUser());
        }

        Warden updated = wardenRepository.saveAndFlush(warden);
        auditLogService.logAction("UPDATE_WARDEN", "Warden", updated.getId(), "Updated warden details");
        return wardenMapper.toDto(updated);
    }

    @Transactional
    public void deleteWarden(Long id) {
        Warden warden = wardenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warden not found with ID: " + id));

        User user = warden.getUser();
        warden.setUser(null);
        wardenRepository.saveAndFlush(warden);

        if (user != null) {
            notificationRepository.deleteNotificationsByUserIdNative(user.getId());
            userRepository.deleteUserNative(user.getId());
        }

        wardenRepository.delete(warden);
        wardenRepository.flush();
        auditLogService.logAction("DELETE_WARDEN", "Warden", id, "Deleted warden ID: " + id);
    }

    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("Username already exists: " + username);
        }
    }
}
