package com.college.leavemanagement.service;

import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.dto.warden.RoomOccupancyDto;
import com.college.leavemanagement.dto.warden.WardenDto;
import com.college.leavemanagement.entity.LeaveApplication;
import com.college.leavemanagement.entity.Student;
import com.college.leavemanagement.entity.Warden;
import com.college.leavemanagement.enums.HostelBlockName;
import com.college.leavemanagement.enums.StudentType;
import com.college.leavemanagement.exception.ResourceNotFoundException;
import com.college.leavemanagement.mapper.StudentMapper;
import com.college.leavemanagement.mapper.WardenMapper;
import com.college.leavemanagement.repository.LeaveApplicationRepository;
import com.college.leavemanagement.repository.StudentRepository;
import com.college.leavemanagement.repository.WardenRepository;
import com.college.leavemanagement.dto.warden.WardenUpdateDto;
import com.college.leavemanagement.enums.Role;
import com.college.leavemanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WardenService {

    private final WardenRepository wardenRepository;
    private final StudentRepository studentRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final UserRepository userRepository;
    private final WardenMapper wardenMapper;
    private final StudentMapper studentMapper;
    private final AuditLogService auditLogService;

    public WardenService(WardenRepository wardenRepository, StudentRepository studentRepository,
                         LeaveApplicationRepository leaveApplicationRepository, UserRepository userRepository,
                         WardenMapper wardenMapper, StudentMapper studentMapper,
                         AuditLogService auditLogService) {
        this.wardenRepository = wardenRepository;
        this.studentRepository = studentRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.userRepository = userRepository;
        this.wardenMapper = wardenMapper;
        this.studentMapper = studentMapper;
        this.auditLogService = auditLogService;
    }

    public WardenDto getWardenProfile(Long wardenId) {
        Warden warden = wardenRepository.findById(wardenId)
                .orElseThrow(() -> new ResourceNotFoundException("Warden not found with ID: " + wardenId));
        return wardenMapper.toDto(warden);
    }

    public List<StudentProfileDto> getStudentsForWarden(Long wardenId) {
        Warden warden = wardenRepository.findById(wardenId)
                .orElseThrow(() -> new ResourceNotFoundException("Warden not found with ID: " + wardenId));

        if (warden.getAssignedBlock() == null) {
            return studentRepository.findAll().stream()
                    .filter(s -> s.getStudentType() == StudentType.HOSTELLER)
                    .map(studentMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return studentRepository.findByHostelBlockOrderByRoomNumberAsc(warden.getAssignedBlock())
                    .stream()
                    .map(studentMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    public List<RoomOccupancyDto> getRoomOccupancyDashboard(Long wardenId, LocalDate date) {
        if (date == null) date = LocalDate.now();

        Warden warden = wardenRepository.findById(wardenId)
                .orElseThrow(() -> new ResourceNotFoundException("Warden not found with ID: " + wardenId));

        List<Student> students;
        if (warden.getAssignedBlock() == null) {
            students = studentRepository.findAll().stream()
                    .filter(s -> s.getStudentType() == StudentType.HOSTELLER && s.getHostelBlock() != null && s.getRoomNumber() != null)
                    .toList();
        } else {
            students = studentRepository.findByHostelBlockOrderByRoomNumberAsc(warden.getAssignedBlock());
        }

        List<LeaveApplication> activeLeaves = leaveApplicationRepository.findActiveApprovedLeavesOnDate(date);
        Set<Long> awayStudentIds = activeLeaves.stream()
                .map(l -> l.getStudent().getId())
                .collect(Collectors.toSet());

        Map<String, List<Student>> roomMap = new HashMap<>();
        for (Student student : students) {
            if (student.getHostelBlock() != null && student.getRoomNumber() != null) {
                String key = student.getHostelBlock().name() + "-" + student.getRoomNumber();
                roomMap.computeIfAbsent(key, k -> new ArrayList<>()).add(student);
            }
        }

        List<RoomOccupancyDto> occupancyList = new ArrayList<>();
        for (Map.Entry<String, List<Student>> entry : roomMap.entrySet()) {
            List<Student> roomStudents = entry.getValue();
            HostelBlockName block = roomStudents.get(0).getHostelBlock();
            String roomNum = roomStudents.get(0).getRoomNumber();

            List<StudentProfileDto> roomRoster = roomStudents.stream().map(studentMapper::toDto).toList();
            List<StudentProfileDto> awayStudents = roomStudents.stream()
                    .filter(s -> awayStudentIds.contains(s.getId()))
                    .map(studentMapper::toDto)
                    .toList();

            RoomOccupancyDto dto = RoomOccupancyDto.builder()
                    .hostelBlock(block)
                    .roomNumber(roomNum)
                    .totalStudents(roomRoster.size())
                    .awayStudentCount(awayStudents.size())
                    .awayStudents(awayStudents)
                    .roomRoster(roomRoster)
                    .build();

            occupancyList.add(dto);
        }

        occupancyList.sort(Comparator.comparing((RoomOccupancyDto r) -> r.getHostelBlock().name()).thenComparing(RoomOccupancyDto::getRoomNumber));
        return occupancyList;
    }

    public List<WardenDto> getBlockWardens() {
        return wardenRepository.findAll().stream()
                .filter(w -> w.getAssignedBlock() != null)
                .map(wardenMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public WardenDto updateBlockWarden(Long wardenId, WardenUpdateDto dto) {
        Warden warden = wardenRepository.findById(wardenId)
                .orElseThrow(() -> new ResourceNotFoundException("Warden not found with ID: " + wardenId));

        if (dto.getName() != null && !dto.getName().isBlank()) warden.setName(dto.getName());
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && warden.getUser() != null) {
            warden.getUser().setEmail(dto.getEmail());
        }

        warden.setAssignedBlock(dto.getAssignedBlock());
        if (warden.getUser() != null) {
            Role newRole = (dto.getAssignedBlock() == null) ? Role.CHIEF_WARDEN : Role.WARDEN;
            warden.getUser().setRole(newRole);
            userRepository.save(warden.getUser());
        }

        Warden updated = wardenRepository.save(warden);
        auditLogService.logAction("UPDATE_BLOCK_WARDEN", "Warden", updated.getId(), 
                "Updated block warden details by Chief Warden / Admin");
        return wardenMapper.toDto(updated);
    }
}
