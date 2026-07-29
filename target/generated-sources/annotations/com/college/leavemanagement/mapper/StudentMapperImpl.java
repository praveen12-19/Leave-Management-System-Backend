package com.college.leavemanagement.mapper;

import com.college.leavemanagement.dto.student.StudentProfileDto;
import com.college.leavemanagement.entity.Mentor;
import com.college.leavemanagement.entity.Student;
import com.college.leavemanagement.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-29T19:33:52+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class StudentMapperImpl implements StudentMapper {

    @Override
    public StudentProfileDto toDto(Student student) {
        if ( student == null ) {
            return null;
        }

        StudentProfileDto studentProfileDto = new StudentProfileDto();

        studentProfileDto.setEmail( studentUserEmail( student ) );
        studentProfileDto.setMentorId( studentMentorId( student ) );
        studentProfileDto.setMentorName( studentMentorName( student ) );
        studentProfileDto.setId( student.getId() );
        studentProfileDto.setName( student.getName() );
        studentProfileDto.setRegNo( student.getRegNo() );
        studentProfileDto.setYear( student.getYear() );
        studentProfileDto.setDeptSection( student.getDeptSection() );
        studentProfileDto.setStudentType( student.getStudentType() );
        studentProfileDto.setGender( student.getGender() );
        studentProfileDto.setFatherContact( student.getFatherContact() );
        studentProfileDto.setMotherContact( student.getMotherContact() );
        studentProfileDto.setHostelBlock( student.getHostelBlock() );
        studentProfileDto.setRoomNumber( student.getRoomNumber() );

        return studentProfileDto;
    }

    private String studentUserEmail(Student student) {
        User user = student.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getEmail();
    }

    private Long studentMentorId(Student student) {
        Mentor mentor = student.getMentor();
        if ( mentor == null ) {
            return null;
        }
        return mentor.getId();
    }

    private String studentMentorName(Student student) {
        Mentor mentor = student.getMentor();
        if ( mentor == null ) {
            return null;
        }
        return mentor.getName();
    }
}
