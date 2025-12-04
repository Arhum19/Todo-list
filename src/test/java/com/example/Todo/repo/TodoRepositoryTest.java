package com.example.Todo.repo;

import com.example.Todo.model.todo;
import com.example.Todo.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;
import  java.time.Instant;
import  static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class TodoRepositoryTest {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private  UserRepository userRepository;

    private User user1;
    private User user2;


    @BeforeEach
    void setup(){
        user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        userRepository.save(user1);

        user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password2");
        userRepository.save(user2);

        todo todo1 = new todo();
        todo1.setTitle("Buy groceries");
        todo1.setDescription("Milk, Bread, Eggs");
        todo1.setCompleted(false);
        todo1.setUser(user1);
        todo1.setCreatedAt(Instant.now());
        todo1.setUpdatedAt(Instant.now());


        todo todo2 = new todo();
        todo2.setTitle("Read book");
        todo2.setDescription("Read 'Clean Code'");
        todo2.setCompleted(true);
        todo2.setUser(user1);
        todo2.setCreatedAt(Instant.now());
        todo2.setUpdatedAt(Instant.now());


        todo todo3 = new todo();
        todo3.setTitle("Workout");
        todo3.setDescription("Gym session at 6 PM");
        todo3.setCompleted(false);
        todo3.setUser(user2);
        todo3.setUpdatedAt(Instant.now());
        todo3.setCreatedAt(Instant.now());

        todoRepository.saveAll(List.of(todo1, todo2, todo3));
    }

    @Test
    void testFindbyCompleted(){
        Pageable pg = PageRequest.of(0,10);
        Page<todo> todos = todoRepository.findByUserAndCompleted(user1, false, pg);
        assertEquals(1, todos.getTotalElements());
        assertEquals("Buy groceries", todos.getContent().get(0).getTitle());
        System.out.println(todos.getContent().get(0).getTitle());
    }

    @Test
    void testFindByTitleContainingIgnoreCase(){
        Pageable pg = PageRequest.of(0,10);
        Page<todo> todos = todoRepository.findByUserAndTitleContainingIgnoreCase(user1, "read", pg);
        assertEquals(1, todos.getTotalElements());
        assertEquals("Read book", todos.getContent().get(0).getTitle());
        System.out.println(todos.getContent().get(0).getTitle());
    }

    @Test
    void findByUser(){
        Pageable pg = PageRequest.of(0,10);
        Page<todo> todos = todoRepository.findByUser(user1, pg);
        assertEquals(2, todos.getTotalElements());
    }

    @Test
    void findByUserId(){
        List<todo> byUserid = todoRepository.findByUserId(user1.getId());
        assertEquals(2, byUserid.size());
    }

    @Test
    void pagedFindByUser() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<todo> page = todoRepository.findByUser(user1, pageable);
        assertEquals(1, page.getSize());
        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
        assertFalse(page.getContent().isEmpty());
    }
}