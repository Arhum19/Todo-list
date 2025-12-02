package com.example.Todo.mapper;

import com.example.Todo.dto.TodoRequest;
import com.example.Todo.dto.TodoResponse;
import com.example.Todo.model.todo;
import com.example.Todo.model.User;

public class TodoMapper {

    public static todo toEntity(TodoRequest req, Long id, User user) {
        todo todo = new todo();
        todo.setId(id);
        todo.setTitle(req.getTitle());
        todo.setDescription(req.getDescription());
        todo.setCompleted(false);
        todo.setCreatedAt(java.time.Instant.now());
        todo.setUpdatedAt(java.time.Instant.now());
        todo.setUser(user);
        return todo;
    }

    public static TodoResponse toResponse(todo T)
    {
        TodoResponse res = new TodoResponse();
        res.setId(T.getId());
        res.setTitle(T.getTitle());
        res.setDescription(T.getDescription());
        res.setCompleted(T.isCompleted());
        res.setCreatedAt(T.getCreatedAt());
        res.setUpdatedAt(T.getUpdatedAt());
        return res;
    }
}
