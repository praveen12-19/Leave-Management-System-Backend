package com.college.leavemanagement.mapper;

import com.college.leavemanagement.dto.warden.WardenDto;
import com.college.leavemanagement.entity.Warden;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WardenMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    @Mapping(target = "chiefWarden", expression = "java(warden.getAssignedBlock() == null)")
    WardenDto toDto(Warden warden);
}
