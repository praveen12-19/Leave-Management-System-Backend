package com.college.leavemanagement.dto.mentor;

import com.college.leavemanagement.enums.DeptSection;
import com.college.leavemanagement.enums.Year;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MentorCreateDto {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    @NotNull(message = "Department Section is required")
    private DeptSection deptSection;

    @NotNull(message = "Assigned Year is required")
    private Year assignedYear;

    public MentorCreateDto() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public DeptSection getDeptSection() { return deptSection; }
    public void setDeptSection(DeptSection deptSection) { this.deptSection = deptSection; }

    public Year getAssignedYear() { return assignedYear; }
    public void setAssignedYear(Year assignedYear) { this.assignedYear = assignedYear; }
}
