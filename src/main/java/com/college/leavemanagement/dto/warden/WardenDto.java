package com.college.leavemanagement.dto.warden;

import com.college.leavemanagement.enums.HostelBlockName;

public class WardenDto {
    private Long id;
    private String name;
    private String username;
    private String email;
    private HostelBlockName assignedBlock;
    private boolean isChiefWarden;

    public WardenDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public HostelBlockName getAssignedBlock() { return assignedBlock; }
    public void setAssignedBlock(HostelBlockName assignedBlock) { this.assignedBlock = assignedBlock; }

    public boolean isChiefWarden() { return isChiefWarden; }
    public void setChiefWarden(boolean chiefWarden) { isChiefWarden = chiefWarden; }
}
