package com.college.leavemanagement.dto.warden;

import com.college.leavemanagement.enums.HostelBlockName;
import jakarta.validation.constraints.NotBlank;

public class WardenCreateDto {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    private HostelBlockName assignedBlock;

    public WardenCreateDto() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public HostelBlockName getAssignedBlock() { return assignedBlock; }
    public void setAssignedBlock(HostelBlockName assignedBlock) { this.assignedBlock = assignedBlock; }
}
