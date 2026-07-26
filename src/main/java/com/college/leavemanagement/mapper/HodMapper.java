package com.college.leavemanagement.mapper;

import com.college.leavemanagement.dto.hod.HodDto;
import com.college.leavemanagement.entity.Hod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HodMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    HodDto toDto(Hod hod);
}
