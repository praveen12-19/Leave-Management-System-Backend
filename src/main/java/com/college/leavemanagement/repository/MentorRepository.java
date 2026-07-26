package com.college.leavemanagement.repository;

import com.college.leavemanagement.entity.Mentor;
import com.college.leavemanagement.enums.DeptSection;
import com.college.leavemanagement.enums.Year;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    Optional<Mentor> findByUserId(Long userId);
    List<Mentor> findByDeptSection(DeptSection deptSection);
    List<Mentor> findByDeptSectionAndAssignedYear(DeptSection deptSection, Year assignedYear);
}
