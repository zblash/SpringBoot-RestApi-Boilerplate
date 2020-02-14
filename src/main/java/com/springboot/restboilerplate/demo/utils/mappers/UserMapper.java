package com.springboot.restboilerplate.demo.utils.mappers;

import com.springboot.restboilerplate.demo.dtos.user.ReadableRegister;
import com.springboot.restboilerplate.demo.dtos.user.ReadableUserInfo;
import com.springboot.restboilerplate.demo.dtos.user.WritableRegister;
import com.springboot.restboilerplate.demo.enums.RoleType;
import com.springboot.restboilerplate.demo.models.Role;
import com.springboot.restboilerplate.demo.models.User;

public final class UserMapper {

    public static User writableRegisterToUser(WritableRegister writableRegister) {
        if (writableRegister == null) {
            return null;
        } else {
            User user = new User();
            user.setUsername(writableRegister.getUsername());
            user.setName(writableRegister.getName());
            user.setPassword(writableRegister.getPassword());
            user.setEmail(writableRegister.getEmail());
            return user;
        }
    }

    public static ReadableRegister userToReadableRegister(User user){
        if (user == null) {
            return null;
        } else {
            ReadableRegister readableRegister = new ReadableRegister();
            readableRegister.setId(user.getUuid().toString());
            readableRegister.setEmail(user.getEmail());
            readableRegister.setName(user.getName());
            readableRegister.setStatus(user.isStatus());
            readableRegister.setUsername(user.getUsername());
            return readableRegister;
        }
    }

    public static RoleType roleToRoleType(Role role){
        if (role == null) {
            return null;
        } else {
            return RoleType.fromValue(role.getName().split("_")[1]);
        }
    }

    public static ReadableUserInfo userToReadableUserInfo(User user){
        if (user == null){
            return null;
        }else {
            ReadableUserInfo.Builder userInfoBuilder = new ReadableUserInfo.Builder(user.getUsername());
            userInfoBuilder.id(user.getUuid().toString());
            userInfoBuilder.email(user.getEmail());
            userInfoBuilder.name(user.getName());
            String role = user.getRole().getName().split("_")[1];
            userInfoBuilder.role(role);
            return userInfoBuilder.build();
        }
    }

}
