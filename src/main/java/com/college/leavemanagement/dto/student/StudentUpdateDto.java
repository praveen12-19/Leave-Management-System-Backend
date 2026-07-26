package com.college.leavemanagement.dto.student;

import com.college.leavemanagement.enums.DeptSection;
import com.college.leavemanagement.enums.HostelBlockName;
import com.college.leavemanagement.enums.StudentType;
import com.college.leavemanagement.enums.Year;

public class StudentUpdateDto {
    private String name;
    private String regNo;
    private String email;
    private Year year;
    private DeptSection deptSection;
    private StudentType studentType;
    private String gender;
    private String fatherContact;
    private String motherContact;
    private HostelBlockName hostelBlock;
    private String roomNumber;
    private Long mentorId;

    public StudentUpdateDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

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

    public Long getMentorId() { return mentorId; }
    public void setMentorId(Long mentorId) { this.mentorId = mentorId; }
}
