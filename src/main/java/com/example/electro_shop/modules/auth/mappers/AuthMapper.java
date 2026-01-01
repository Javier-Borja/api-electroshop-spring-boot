package com.example.electro_shop.modules.auth.mappers;

import com.example.electro_shop.modules.auth.dtos.LoginResponse;
import com.example.electro_shop.modules.auth.dtos.RegisterRequest;
import com.example.electro_shop.modules.auth.dtos.RegisterResponse;
import com.example.electro_shop.modules.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    User toUser(RegisterRequest request);

    RegisterResponse toRegisterResponse(User user);

    LoginResponse toLoginResponse(User user);
}
