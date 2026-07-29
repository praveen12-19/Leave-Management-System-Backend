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
    List<Student> findByDeptSectionOrderByRegNoAsc(DeptSection deptSection);
    boolean existsByRegNo(String regNo);

    @org.springframework.data.jpa.repository.Modifying(clearAutomatically = true, flushAutomatically = true)
    @org.springframework.data.jpa.repository.Query("UPDATE Student s SET s.mentor = null WHERE s.mentor.id = :mentorId")
    int unlinkMentorFromStudents(@org.springframework.data.repository.query.Param("mentorId") Long mentorId);

    @org.springframework.data.jpa.repository.Modifying(clearAutomatically = true, flushAutomatically = true)
    @org.springframework.data.jpa.repository.Query(value = "UPDATE students SET mentor_id = NULL WHERE mentor_id = :mentorId", nativeQuery = true)
    int unlinkMentorNative(@org.springframework.data.repository.query.Param("mentorId") Long mentorId);
}
