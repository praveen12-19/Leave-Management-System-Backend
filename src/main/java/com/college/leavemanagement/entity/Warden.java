package com.college.leavemanagement.entity;

import com.college.leavemanagement.enums.HostelBlockName;
import jakarta.persistence.*;

@Entity
@Table(name = "wardens")
public class Warden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private HostelBlockName assignedBlock; // Nullable -> null indicates CHIEF_WARDEN

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Warden() {}

    public Warden(Long id, String name, HostelBlockName assignedBlock, User user) {
        this.id = id;
        this.name = name;
        this.assignedBlock = assignedBlock;
        this.user = user;
    }

    public static WardenBuilder builder() {
        return new WardenBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public HostelBlockName getAssignedBlock() { return assignedBlock; }
    public void setAssignedBlock(HostelBlockName assignedBlock) { this.assignedBlock = assignedBlock; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public static class WardenBuilder {
        private Long id;
        private String name;
        private HostelBlockName assignedBlock;
        private User user;

        WardenBuilder() {}

        public WardenBuilder id(Long id) { this.id = id; return this; }
        public WardenBuilder name(String name) { this.name = name; return this; }
        public WardenBuilder assignedBlock(HostelBlockName assignedBlock) { this.assignedBlock = assignedBlock; return this; }
        public WardenBuilder user(User user) { this.user = user; return this; }

        public Warden build() {
            return new Warden(id, name, assignedBlock, user);
        }
    }
}
