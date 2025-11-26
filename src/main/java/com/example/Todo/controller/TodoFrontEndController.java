//package com.example.Todo.controller;
//
//import com.example.Todo.service.TodoService;
//import org.springframework.ui.Model;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class TodoFrontEndController
//{
//    private  final TodoService todoService;
//
//    public TodoFrontEndController(TodoService todoService) {
//        this.todoService = todoService;
//    }
//
//    @GetMapping("/")
//    public String viewTodos(Model model) {
//        model.addAttribute("todos", todoService.findAll());
//        return "index";
//    }
//}
