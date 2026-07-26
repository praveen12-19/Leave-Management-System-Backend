package com.college.leavemanagement.entity;

import com.college.leavemanagement.enums.DeptSection;
import com.college.leavemanagement.enums.Year;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mentors")
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeptSection deptSection;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Year assignedYear;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Student> students = new ArrayList<>();

    public Mentor() {}

    public Mentor(Long id, String name, DeptSection deptSection, Year assignedYear, User user, List<Student> students) {
        this.id = id;
        this.name = name;
        this.deptSection = deptSection;
        this.assignedYear = assignedYear;
        this.user = user;
        if (students != null) this.students = students;
    }

    public static MentorBuilder builder() {
        return new MentorBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public DeptSection getDeptSection() { return deptSection; }
    public void setDeptSection(DeptSection deptSection) { this.deptSection = deptSection; }

    public Year getAssignedYear() { return assignedYear; }
    public void setAssignedYear(Year assignedYear) { this.assignedYear = assignedYear; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }

    public static class MentorBuilder {
        private Long id;
        private String name;
        private DeptSection deptSection;
        private Year assignedYear;
        private User user;
        private List<Student> students = new ArrayList<>();

        MentorBuilder() {}

        public MentorBuilder id(Long id) { this.id = id; return this; }
        public MentorBuilder name(String name) { this.name = name; return this; }
        public MentorBuilder deptSection(DeptSection deptSection) { this.deptSection = deptSection; return this; }
        public MentorBuilder assignedYear(Year assignedYear) { this.assignedYear = assignedYear; return this; }
        public MentorBuilder user(User user) { this.user = user; return this; }
        public MentorBuilder students(List<Student> students) { this.students = students; return this; }

        public Mentor build() {
            return new Mentor(id, name, deptSection, assignedYear, user, students);
        }
    }
}
