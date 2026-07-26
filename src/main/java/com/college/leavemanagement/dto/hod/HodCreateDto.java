package com.college.leavemanagement.dto.hod;

import com.college.leavemanagement.entity.HodDepartmentAssignment;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public class HodCreateDto {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    private Set<HodDepartmentAssignment> managedDepartmentAssignments;

    public HodCreateDto() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<HodDepartmentAssignment> getManagedDepartmentAssignments() { return managedDepartmentAssignments; }
    public void setManagedDepartmentAssignments(Set<HodDepartmentAssignment> managedDepartmentAssignments) {
        this.managedDepartmentAssignments = managedDepartmentAssignments;
    }
}
