package com.college.leavemanagement.repository;

import com.college.leavemanagement.entity.Warden;
import com.college.leavemanagement.enums.HostelBlockName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WardenRepository extends JpaRepository<Warden, Long> {
    Optional<Warden> findByUserId(Long userId);
    Optional<Warden> findByAssignedBlock(HostelBlockName assignedBlock);
}
