package com.college.leavemanagement.dto.mentor;

import com.college.leavemanagement.enums.DeptSection;
import com.college.leavemanagement.enums.Year;

public class MentorDto {
    private Long id;
    private String name;
    private DeptSection deptSection;
    private Year assignedYear;
    private String username;
    private String email;
    private int assignedStudentCount;

    public MentorDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public DeptSection getDeptSection() { return deptSection; }
    public void setDeptSection(DeptSection deptSection) { this.deptSection = deptSection; }

    public Year getAssignedYear() { return assignedYear; }
    public void setAssignedYear(Year assignedYear) { this.assignedYear = assignedYear; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAssignedStudentCount() { return assignedStudentCount; }
    public void setAssignedStudentCount(int assignedStudentCount) { this.assignedStudentCount = assignedStudentCount; }
}
