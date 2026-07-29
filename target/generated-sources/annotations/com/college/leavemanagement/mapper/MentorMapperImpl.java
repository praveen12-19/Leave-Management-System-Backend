package com.college.leavemanagement.mapper;

import com.college.leavemanagement.dto.mentor.MentorDto;
import com.college.leavemanagement.entity.Mentor;
import com.college.leavemanagement.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-29T19:33:52+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class MentorMapperImpl implements MentorMapper {

    @Override
    public MentorDto toDto(Mentor mentor) {
        if ( mentor == null ) {
            return null;
        }

        MentorDto mentorDto = new MentorDto();

        mentorDto.setUsername( mentorUserUsername( mentor ) );
        mentorDto.setEmail( mentorUserEmail( mentor ) );
        mentorDto.setId( mentor.getId() );
        mentorDto.setName( mentor.getName() );
        mentorDto.setDeptSection( mentor.getDeptSection() );
        mentorDto.setAssignedYear( mentor.getAssignedYear() );

        mentorDto.setAssignedStudentCount( mentor.getStudents() != null ? mentor.getStudents().size() : 0 );
        mentorDto.setHostellerCount( mentor.getStudents() != null ? (int) mentor.getStudents().stream().filter(s -> s.getStudentType() == com.college.leavemanagement.enums.StudentType.HOSTELLER).count() : 0 );
        mentorDto.setDayScholarCount( mentor.getStudents() != null ? (int) mentor.getStudents().stream().filter(s -> s.getStudentType() == com.college.leavemanagement.enums.StudentType.DAY_SCHOLAR).count() : 0 );
        mentorDto.setBlackTagCount( mentor.getStudents() != null ? (int) mentor.getStudents().stream().filter(s -> s.getStudentType() == com.college.leavemanagement.enums.StudentType.BLACK_TAG).count() : 0 );

        return mentorDto;
    }

    private String mentorUserUsername(Mentor mentor) {
        User user = mentor.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getUsername();
    }

    private String mentorUserEmail(Mentor mentor) {
        User user = mentor.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getEmail();
    }
}
