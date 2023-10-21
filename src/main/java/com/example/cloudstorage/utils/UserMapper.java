package com.example.cloudstorage.utils;

import com.example.cloudstorage.dtos.UserDto;
import com.example.cloudstorage.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto dto);

    UserDto toDto(User user);

}
