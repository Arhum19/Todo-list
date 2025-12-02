package com.example.Todo.repo;

import com.example.Todo.model.todo;
import com.example.Todo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<todo, Long>, JpaSpecificationExecutor<todo> {

    List<todo> findByCompleted(boolean completed);
    List<todo> findByTitleContainingIgnoreCase(String keyword);

    // User-based queries
    List<todo> findByUser(User user);
    List<todo> findByUserId(Long userId);
    List<todo> findByUserAndCompleted(User user, boolean completed);
    List<todo> findByUserIdAndCompleted(Long userId, boolean completed);
    List<todo> findByUserAndTitleContainingIgnoreCase(User user, String keyword);
    List<todo> findByUserIdAndTitleContainingIgnoreCase(Long userId, String keyword);

    // --- NEW: paged versions for filters ---
    Page<todo> findByUser(User user, Pageable pageable);
    Page<todo> findByUserAndCompleted(User user, boolean completed, Pageable pageable);
    Page<todo> findByUserAndTitleContainingIgnoreCase(User user, String keyword, Pageable pageable);
}
