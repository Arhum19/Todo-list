package com.example.Todo.specification;

import com.example.Todo.model.todo;
import org.springframework.data.jpa.domain.Specification;

public class TodoSpecification {
    public static Specification<todo> hasUserId(Long userId) {
        return (root, query, cb) -> userId == null ? null :
                cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<todo> titleContains(String keyword) {
        return (root, query, cb) -> (keyword == null || keyword.isBlank()) ? null :
                cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<todo> completedIs(Boolean completed) {
        return (root, query, cb) -> completed == null ? null :
                cb.equal(root.get("completed"), completed);
    }
}
