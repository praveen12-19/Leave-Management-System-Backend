package com.college.leavemanagement.mapper;

import com.college.leavemanagement.dto.hod.HodDto;
import com.college.leavemanagement.entity.Hod;
import com.college.leavemanagement.entity.HodDepartmentAssignment;
import com.college.leavemanagement.entity.User;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-29T19:33:51+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class HodMapperImpl implements HodMapper {

    @Override
    public HodDto toDto(Hod hod) {
        if ( hod == null ) {
            return null;
        }

        HodDto hodDto = new HodDto();

        hodDto.setUsername( hodUserUsername( hod ) );
        hodDto.setEmail( hodUserEmail( hod ) );
        hodDto.setId( hod.getId() );
        hodDto.setName( hod.getName() );
        Set<HodDepartmentAssignment> set = hod.getManagedDepartmentAssignments();
        if ( set != null ) {
            hodDto.setManagedDepartmentAssignments( new LinkedHashSet<HodDepartmentAssignment>( set ) );
        }

        return hodDto;
    }

    private String hodUserUsername(Hod hod) {
        User user = hod.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getUsername();
    }

    private String hodUserEmail(Hod hod) {
        User user = hod.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getEmail();
    }
}
