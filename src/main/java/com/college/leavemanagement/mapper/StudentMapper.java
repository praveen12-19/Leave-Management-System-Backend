package com.college.leavemanagement.mapper;

import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "mentor.id", target = "mentorId")
    @Mapping(source = "mentor.name", target = "mentorName")
    @Mapping(target = "totalLeavesSubmitted", ignore = true)
    @Mapping(target = "totalLeavesApproved", ignore = true)
    StudentProfileDto toDto(Student student);
}
