package com.college.leavemanagement.dto.mentor;

import com.college.leavemanagement.enums.DeptSection;
import com.college.leavemanagement.enums.Year;

public class MentorUpdateDto {
    private String name;
    private String email;
    private DeptSection deptSection;
    private Year assignedYear;

    public MentorUpdateDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public DeptSection getDeptSection() { return deptSection; }
    public void setDeptSection(DeptSection deptSection) { this.deptSection = deptSection; }

    public Year getAssignedYear() { return assignedYear; }
    public void setAssignedYear(Year assignedYear) { this.assignedYear = assignedYear; }
}
