package com.example.Todo.repo;

import com.example.Todo.model.todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<todo, Long> {

    // Find todos by completed status (true/false)
    List<todo> findByCompleted(boolean completed);

    // Search todos by title (case-insensitive)
    List<todo> findByTitleContainingIgnoreCase(String keyword);
}
