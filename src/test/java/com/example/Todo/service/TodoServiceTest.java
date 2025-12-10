package com.example.Todo.service;

import com.example.Todo.dto.*;
import com.example.Todo.exception.NotFoundException;
import com.example.Todo.model.User;
import com.example.Todo.model.todo;
import com.example.Todo.repo.TodoRepository;
import com.example.Todo.repo.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private TodoService todoService;

    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
    }

    @Test
    void testGetTodos_ReturnsPagedData() {
        todo t = new todo();
        t.setId(1L);
        t.setTitle("Test");

        Page<todo> page = new PageImpl<>(List.of(t));

        when(todoRepo.findByUser(eq(user), any(Pageable.class))).thenReturn(page);

        PagedTodoResponse response = todoService.getTodos(user, null, null, 0, 10);

        assertEquals(1, response.getTotalElements());
        assertEquals("Test", response.getContent().get(0).getTitle());
    }

    @Test
    void testUpdateTodo_ThrowsNotFound() {
        when(todoRepo.findById(100L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                todoService.updateTodo(user, 100L, new TodoRequest())
        );
    }

    @Test
    void testDeleteTodo_Success() {
        todo t = new todo();
        t.setUser(user);

        when(todoRepo.findById(1L)).thenReturn(Optional.of(t));

        assertDoesNotThrow(() -> todoService.deleteTodo(user, 1L));
        verify(todoRepo, times(1)).delete(t);
    }

    @Test
    void testGetTodos_FilteredByCompleted(){
        todo t1 = new todo();
        t1.setId(1L);
        t1.setTitle("Completed Task");
        t1.setCompleted(true);

        Page<todo> page = new PageImpl<>(List.of(t1));

        when(todoRepo.findByUserAndCompleted(eq(user), eq(true), any(Pageable.class))).thenReturn(page);

        PagedTodoResponse response = todoService.getTodos(user, true, null, 0, 10);

        assertEquals(1, response.getTotalElements());
        assertTrue(response.getContent().get(0).isCompleted());

    }

    @Test
    void TestGetTodos_AllFromUser(){
        todo t1 = new todo();
        t1.setId(1L);
        t1.setTitle("Task 1");

        todo t2 = new todo();
        t2.setId(2L);
        t2.setTitle("Task 2");

        todo t3 = new todo();
        t3.setId(3L);
        t3.setTitle("Task 3");

        Page<todo> page = new PageImpl<>(List.of(t1, t2, t3));

        when(todoRepo.findByUser(eq(user), any(Pageable.class))).thenReturn(page);

        PagedTodoResponse response = todoService.getTodos(user, null, null, 0, 10);

        assertEquals(3, response.getTotalElements());
        assertEquals("Task 1", response.getContent().get(0).getTitle());
        assertEquals("Task 2", response.getContent().get(1).getTitle());
        assertEquals("Task 3", response.getContent().get(2).getTitle());
    }


    @Test
    void TestCreateTodo_Success(){
        TodoRequest tr = new TodoRequest();
        tr.setTitle("New Task");
        tr.setDescription("New Task Description");
        todo t = new todo();
        t.setId(1L);
        t.setTitle(tr.getTitle());
        t.setDescription(tr.getDescription());
        when(todoRepo.save(any(todo.class))).thenReturn(t);
        TodoResponse res = todoService.createTodo(user, tr);
        assertEquals(1L, res.getId());
        assertEquals("New Task", res.getTitle());
        assertEquals("New Task Description", res.getDescription());

    }

    void TestUpdateTodo_Success(){
        todo existing = new todo();
        existing.setId(1L);
        existing.setUser(user);
        existing.setTitle("Old Title");
        existing.setDescription("Old Description");
        existing.setCompleted(false);

        TodoRequest tr = new TodoRequest();
        tr.setTitle("Updated Title");
        tr.setDescription("Updated Description");
        tr.setCompleted(true);

        when(todoRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(todoRepo.save(any(todo.class))).thenAnswer(i -> i.getArgument(0));

        TodoResponse res = todoService.updateTodo(user, 1L, tr);

        assertEquals(1L, res.getId());
        assertEquals("Updated Title", res.getTitle());
        assertEquals("Updated Description", res.getDescription());
        assertTrue(res.isCompleted());
    }

    void TestUpdateTodo_NotExist(){
        when(todoRepo.findById(1L)).thenReturn(Optional.empty());

        TodoRequest tr = new TodoRequest();
        tr.setTitle("Updated Title");
        tr.setDescription("Updated Description");
        tr.setCompleted(true);

        assertThrows(NotFoundException.class, () ->
                todoService.updateTodo(user, 1L, tr)
        );
    }

    void TestUpdateTodo_NotOwned(){
        User otherUser = new User();
        otherUser.setId(2L);

        todo existing = new todo();
        existing.setId(1L);
        existing.setUser(otherUser);
        existing.setTitle("Old Title");
        existing.setDescription("Old Description");
        existing.setCompleted(false);

        when(todoRepo.findById(1L)).thenReturn(Optional.of(existing));

        TodoRequest tr = new TodoRequest();
        tr.setTitle("Updated Title");
        tr.setDescription("Updated Description");
        tr.setCompleted(true);

        assertThrows(NotFoundException.class, () ->
                todoService.updateTodo(user, 1L, tr)
        );
    }
}
