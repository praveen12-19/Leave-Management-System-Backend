package com.college.leavemanagement.entity;

import com.college.leavemanagement.enums.DeptSection;
import com.college.leavemanagement.enums.HostelBlockName;
import com.college.leavemanagement.enums.StudentType;
import com.college.leavemanagement.enums.Year;
import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String regNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "student_year", nullable = false)
    private Year year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeptSection deptSection;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentType studentType;

    private String gender;

    private String fatherContact;
    private String motherContact;

    @Enumerated(EnumType.STRING)
    private HostelBlockName hostelBlock;

    private String roomNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.SET_NULL)
    private Mentor mentor;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Student() {}

    public Student(Long id, String name, String regNo, Year year, DeptSection deptSection, StudentType studentType,
                   String gender, String fatherContact, String motherContact, HostelBlockName hostelBlock, String roomNumber,
                   Mentor mentor, User user) {
        this.id = id;
        this.name = name;
        this.regNo = regNo;
        this.year = year;
        this.deptSection = deptSection;
        this.studentType = studentType;
        this.gender = gender;
        this.fatherContact = fatherContact;
        this.motherContact = motherContact;
        this.hostelBlock = hostelBlock;
        this.roomNumber = roomNumber;
        this.mentor = mentor;
        this.user = user;
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }

    public Year getYear() { return year; }
    public void setYear(Year year) { this.year = year; }

    public DeptSection getDeptSection() { return deptSection; }
    public void setDeptSection(DeptSection deptSection) { this.deptSection = deptSection; }

    public StudentType getStudentType() { return studentType; }
    public void setStudentType(StudentType studentType) { this.studentType = studentType; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getFatherContact() { return fatherContact; }
    public void setFatherContact(String fatherContact) { this.fatherContact = fatherContact; }

    public String getMotherContact() { return motherContact; }
    public void setMotherContact(String motherContact) { this.motherContact = motherContact; }

    public HostelBlockName getHostelBlock() { return hostelBlock; }
    public void setHostelBlock(HostelBlockName hostelBlock) { this.hostelBlock = hostelBlock; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public Mentor getMentor() { return mentor; }
    public void setMentor(Mentor mentor) { this.mentor = mentor; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public static class StudentBuilder {
        private Long id;
        private String name;
        private String regNo;
        private Year year;
        private DeptSection deptSection;
        private StudentType studentType;
        private String gender;
        private String fatherContact;
        private String motherContact;
        private HostelBlockName hostelBlock;
        private String roomNumber;
        private Mentor mentor;
        private User user;

        StudentBuilder() {}

        public StudentBuilder id(Long id) { this.id = id; return this; }
        public StudentBuilder name(String name) { this.name = name; return this; }
        public StudentBuilder regNo(String regNo) { this.regNo = regNo; return this; }
        public StudentBuilder year(Year year) { this.year = year; return this; }
        public StudentBuilder deptSection(DeptSection deptSection) { this.deptSection = deptSection; return this; }
        public StudentBuilder studentType(StudentType studentType) { this.studentType = studentType; return this; }
        public StudentBuilder gender(String gender) { this.gender = gender; return this; }
        public StudentBuilder fatherContact(String fatherContact) { this.fatherContact = fatherContact; return this; }
        public StudentBuilder motherContact(String motherContact) { this.motherContact = motherContact; return this; }
        public StudentBuilder hostelBlock(HostelBlockName hostelBlock) { this.hostelBlock = hostelBlock; return this; }
        public StudentBuilder roomNumber(String roomNumber) { this.roomNumber = roomNumber; return this; }
        public StudentBuilder mentor(Mentor mentor) { this.mentor = mentor; return this; }
        public StudentBuilder user(User user) { this.user = user; return this; }

        public Student build() {
            return new Student(id, name, regNo, year, deptSection, studentType, gender, fatherContact, motherContact, hostelBlock, roomNumber, mentor, user);
        }
    }
}
