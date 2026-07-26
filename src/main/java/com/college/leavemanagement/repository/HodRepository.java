package com.college.leavemanagement.repository;

import com.college.leavemanagement.entity.Hod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HodRepository extends JpaRepository<Hod, Long> {
    Optional<Hod> findByUserId(Long userId);
}
