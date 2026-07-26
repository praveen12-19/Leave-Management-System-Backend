package com.college.leavemanagement.repository;

import com.college.leavemanagement.entity.Student;
import com.college.leavemanagement.enums.DeptSection;
import com.college.leavemanagement.enums.HostelBlockName;
import com.college.leavemanagement.enums.Year;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRegNo(String regNo);
    Optional<Student> findByUserId(Long userId);
    List<Student> findByMentorIdOrderByRegNoAsc(Long mentorId);
    List<Student> findByHostelBlockOrderByRoomNumberAsc(HostelBlockName hostelBlock);
    List<Student> findByRegNoContainingIgnoreCaseOrNameContainingIgnoreCase(String regNo, String name);
    List<Student> findByDeptSectionAndYearOrderByRegNoAsc(DeptSection deptSection, Year year);
    boolean existsByRegNo(String regNo);
}
