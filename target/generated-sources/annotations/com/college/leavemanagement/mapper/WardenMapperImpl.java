package com.college.leavemanagement.mapper;

import com.college.leavemanagement.dto.warden.WardenDto;
import com.college.leavemanagement.entity.User;
import com.college.leavemanagement.entity.Warden;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-29T19:33:52+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class WardenMapperImpl implements WardenMapper {

    @Override
    public WardenDto toDto(Warden warden) {
        if ( warden == null ) {
            return null;
        }

        WardenDto wardenDto = new WardenDto();

        wardenDto.setUsername( wardenUserUsername( warden ) );
        wardenDto.setEmail( wardenUserEmail( warden ) );
        wardenDto.setId( warden.getId() );
        wardenDto.setName( warden.getName() );
        wardenDto.setAssignedBlock( warden.getAssignedBlock() );

        wardenDto.setChiefWarden( warden.getAssignedBlock() == null );

        return wardenDto;
    }

    private String wardenUserUsername(Warden warden) {
        User user = warden.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getUsername();
    }

    private String wardenUserEmail(Warden warden) {
        User user = warden.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getEmail();
    }
}
