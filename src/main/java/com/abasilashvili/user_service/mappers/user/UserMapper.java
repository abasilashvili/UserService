package com.abasilashvili.user_service.mappers.user;

import com.abasilashvili.user_service.dto.user.UserDto;
import com.abasilashvili.user_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
