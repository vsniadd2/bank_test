package com.example.bankcards.mapper;

import com.example.bankcards.dto.auth.RegistrationRequest;
import com.example.bankcards.dto.user.AdminUserDto;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-18T16:15:20+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.6 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.username( user.getUsername() );
        userDto.email( user.getEmail() );

        return userDto.build();
    }

    @Override
    public User toEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( dto.id() );
        user.username( dto.username() );
        user.email( dto.email() );

        return user.build();
    }

    @Override
    public User toEntity(RegistrationRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( request.username() );
        user.email( request.email() );
        user.password( request.password() );

        user.roles( defaultRoles() );
        user.isActive( true );

        return user.build();
    }

    @Override
    public AdminUserDto toAdminDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String email = null;
        List<Role> roles = null;
        LocalDateTime dateTimeOfCreated = null;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        List<Role> list = user.getRoles();
        if ( list != null ) {
            roles = new ArrayList<Role>( list );
        }
        dateTimeOfCreated = user.getDateTimeOfCreated();

        boolean isActive = false;

        AdminUserDto adminUserDto = new AdminUserDto( id, username, email, roles, isActive, dateTimeOfCreated );

        return adminUserDto;
    }
}
