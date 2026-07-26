package com.college.leavemanagement.dto.hod;

import com.college.leavemanagement.entity.HodDepartmentAssignment;
import java.util.Set;

public class HodUpdateDto {
    private String name;
    private String email;
    private Set<HodDepartmentAssignment> managedDepartmentAssignments;

    public HodUpdateDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<HodDepartmentAssignment> getManagedDepartmentAssignments() { return managedDepartmentAssignments; }
    public void setManagedDepartmentAssignments(Set<HodDepartmentAssignment> managedDepartmentAssignments) {
        this.managedDepartmentAssignments = managedDepartmentAssignments;
    }
}
