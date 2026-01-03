package com.example.electro_shop.modules.user.mapper;

import com.example.electro_shop.modules.user.dto.UserProfileDto;
import com.example.electro_shop.modules.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "active", source = "active")
    @Mapping(target = "fullName", source = ".", qualifiedByName = "joinNames")
    UserProfileDto toUserProfileDto(User user);

    @Named("joinNames")
    default String joinNames(User user) {
        if (user == null) return null;
        return (user.getFirstName() + " " + user.getLastName()).trim();
    }
}
