package com.example.bankcards.mapper;

import com.example.bankcards.dto.auth.RegistrationRequest;
import com.example.bankcards.dto.user.AdminUserDto;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", expression = "java(defaultRoles())")
    @Mapping(target = "tokens", ignore = true)
    @Mapping(target = "dateTimeOfCreated", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    User toEntity(RegistrationRequest request);

    AdminUserDto toAdminDto(User user);

    default java.util.List<Role> defaultRoles() {
        return java.util.List.of(Role.ROLE_USER);
    }
}
