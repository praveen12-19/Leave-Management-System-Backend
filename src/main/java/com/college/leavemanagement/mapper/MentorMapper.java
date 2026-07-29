package com.college.leavemanagement.mapper;

import com.college.leavemanagement.dto.mentor.MentorDto;
import com.college.leavemanagement.entity.Mentor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MentorMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    @Mapping(target = "assignedStudentCount", expression = "java(mentor.getStudents() != null ? mentor.getStudents().size() : 0)")
    @Mapping(target = "hostellerCount", expression = "java(mentor.getStudents() != null ? (int) mentor.getStudents().stream().filter(s -> s.getStudentType() == com.college.leavemanagement.enums.StudentType.HOSTELLER).count() : 0)")
    @Mapping(target = "dayScholarCount", expression = "java(mentor.getStudents() != null ? (int) mentor.getStudents().stream().filter(s -> s.getStudentType() == com.college.leavemanagement.enums.StudentType.DAY_SCHOLAR).count() : 0)")
    @Mapping(target = "blackTagCount", expression = "java(mentor.getStudents() != null ? (int) mentor.getStudents().stream().filter(s -> s.getStudentType() == com.college.leavemanagement.enums.StudentType.BLACK_TAG).count() : 0)")
    MentorDto toDto(Mentor mentor);
}
