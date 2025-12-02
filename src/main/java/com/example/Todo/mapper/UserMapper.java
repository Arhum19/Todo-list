package com.example.Todo.mapper;

import com.example.Todo.dto.UserRequest;
import com.example.Todo.dto.UserResponse;
import com.example.Todo.model.User;

public class UserMapper {

    public static User toEntity(UserRequest req) {
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(req.getPassword());
        u.setRoles(req.getRoles());
        return u;
    }

    public static UserResponse toResponse(User user) {
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setRoles(user.getRoles());
        return res;
    }
}
