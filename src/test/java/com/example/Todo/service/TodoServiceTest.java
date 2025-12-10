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
}
