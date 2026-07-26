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
    MentorDto toDto(Mentor mentor);
}
