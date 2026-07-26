package com.college.leavemanagement.repository;

import com.college.leavemanagement.entity.LeaveApplication;
import com.college.leavemanagement.enums.ApprovalStatus;
import com.college.leavemanagement.enums.DeptSection;
import com.college.leavemanagement.enums.HostelBlockName;
import com.college.leavemanagement.enums.LeaveStage;
import com.college.leavemanagement.enums.StudentType;
import com.college.leavemanagement.enums.Year;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {

    List<LeaveApplication> findByStudentIdOrderBySubmittedAtDesc(Long studentId);

    // Mentor Queries
    List<LeaveApplication> findByStudentMentorIdAndCurrentStageOrderBySubmittedAtAsc(Long mentorId, LeaveStage currentStage);

    List<LeaveApplication> findByStudentMentorIdAndMentorStatusNotOrderByMentorActedAtDesc(Long mentorId, ApprovalStatus status);

    // HOD Queries
    @Query("SELECT l FROM LeaveApplication l WHERE l.mentorStatus = 'APPROVED' " +
           "AND l.currentStage = 'HOD_REVIEW' " +
           "AND l.student.deptSection = :deptSection AND l.student.year = :year " +
           "ORDER BY l.submittedAt ASC")
    List<LeaveApplication> findPendingForHod(@Param("deptSection") DeptSection deptSection, @Param("year") Year year);

    @Query("SELECT l FROM LeaveApplication l WHERE l.hodStatus != 'PENDING' " +
           "AND l.student.deptSection = :deptSection AND l.student.year = :year " +
           "ORDER BY l.hodActedAt DESC")
    List<LeaveApplication> findHistoryForHod(@Param("deptSection") DeptSection deptSection, @Param("year") Year year);

    // Warden Queries (Scoped to single block)
    @Query("SELECT l FROM LeaveApplication l WHERE l.hodStatus = 'APPROVED' " +
           "AND l.student.studentType = :studentType " +
           "AND l.currentStage = 'WARDEN_REVIEW' " +
           "AND l.student.hostelBlock = :hostelBlock " +
           "ORDER BY l.submittedAt ASC")
    List<LeaveApplication> findPendingForWardenScoped(@Param("studentType") StudentType studentType,
                                                       @Param("hostelBlock") HostelBlockName hostelBlock);

    // Warden Queries (Chief Warden - All blocks)
    @Query("SELECT l FROM LeaveApplication l WHERE l.hodStatus = 'APPROVED' " +
           "AND l.student.studentType = :studentType " +
           "AND l.currentStage = 'WARDEN_REVIEW' " +
           "ORDER BY l.submittedAt ASC")
    List<LeaveApplication> findPendingForChiefWarden(@Param("studentType") StudentType studentType);

    // Warden History (Scoped)
    @Query("SELECT l FROM LeaveApplication l WHERE l.wardenStatus = :status " +
           "AND l.student.hostelBlock = :hostelBlock " +
           "ORDER BY l.wardenActedAt DESC")
    Page<LeaveApplication> findHistoryForWardenScoped(@Param("hostelBlock") HostelBlockName hostelBlock,
                                                      @Param("status") ApprovalStatus status,
                                                      Pageable pageable);

    @Query("SELECT l FROM LeaveApplication l WHERE l.wardenStatus != 'PENDING' " +
           "AND l.student.hostelBlock = :hostelBlock " +
           "ORDER BY l.wardenActedAt DESC")
    Page<LeaveApplication> findAllHistoryForWardenScoped(@Param("hostelBlock") HostelBlockName hostelBlock,
                                                         Pageable pageable);

    // Warden History (Chief Warden)
    @Query("SELECT l FROM LeaveApplication l WHERE l.wardenStatus = :status " +
           "ORDER BY l.wardenActedAt DESC")
    Page<LeaveApplication> findHistoryForChiefWarden(@Param("status") ApprovalStatus status,
                                                     Pageable pageable);

    @Query("SELECT l FROM LeaveApplication l WHERE l.wardenStatus != 'PENDING' " +
           "ORDER BY l.wardenActedAt DESC")
    Page<LeaveApplication> findAllHistoryForChiefWarden(Pageable pageable);

    // Occupancy Query: Approved leaves active on given date
    @Query("SELECT l FROM LeaveApplication l WHERE l.finalStatus = 'APPROVED' " +
           "AND l.fromDate <= :targetDate AND l.toDate >= :targetDate " +
           "AND l.student.studentType = 'HOSTELLER'")
    List<LeaveApplication> findActiveApprovedLeavesOnDate(@Param("targetDate") LocalDate targetDate);
}
