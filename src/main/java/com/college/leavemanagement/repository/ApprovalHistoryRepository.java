package com.college.leavemanagement.repository;

import com.college.leavemanagement.entity.ApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, Long> {
    List<ApprovalHistory> findByLeaveApplicationIdOrderByTimestampAsc(Long leaveApplicationId);
}
