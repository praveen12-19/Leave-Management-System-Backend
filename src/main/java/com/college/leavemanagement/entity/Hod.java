package com.college.leavemanagement.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hods")
public class Hod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "hod_department_assignments", joinColumns = @JoinColumn(name = "hod_id"))
    private Set<HodDepartmentAssignment> managedDepartmentAssignments = new HashSet<>();

    public Hod() {}

    public Hod(Long id, String name, User user, Set<HodDepartmentAssignment> managedDepartmentAssignments) {
        this.id = id;
        this.name = name;
        this.user = user;
        if (managedDepartmentAssignments != null) this.managedDepartmentAssignments = managedDepartmentAssignments;
    }

    public static HodBuilder builder() {
        return new HodBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Set<HodDepartmentAssignment> getManagedDepartmentAssignments() { return managedDepartmentAssignments; }
    public void setManagedDepartmentAssignments(Set<HodDepartmentAssignment> managedDepartmentAssignments) {
        this.managedDepartmentAssignments = managedDepartmentAssignments;
    }

    public static class HodBuilder {
        private Long id;
        private String name;
        private User user;
        private Set<HodDepartmentAssignment> managedDepartmentAssignments = new HashSet<>();

        HodBuilder() {}

        public HodBuilder id(Long id) { this.id = id; return this; }
        public HodBuilder name(String name) { this.name = name; return this; }
        public HodBuilder user(User user) { this.user = user; return this; }
        public HodBuilder managedDepartmentAssignments(Set<HodDepartmentAssignment> managedDepartmentAssignments) {
            this.managedDepartmentAssignments = managedDepartmentAssignments;
            return this;
        }

        public Hod build() {
            return new Hod(id, name, user, managedDepartmentAssignments);
        }
    }
}
