package com.college.leavemanagement.dto.warden;

import com.college.leavemanagement.enums.HostelBlockName;

public class WardenUpdateDto {
    private String name;
    private String email;
    private HostelBlockName assignedBlock;

    public WardenUpdateDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public HostelBlockName getAssignedBlock() { return assignedBlock; }
    public void setAssignedBlock(HostelBlockName assignedBlock) { this.assignedBlock = assignedBlock; }
}
