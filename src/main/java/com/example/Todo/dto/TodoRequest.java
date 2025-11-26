package com.example.Todo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Valid
public class TodoRequest {
    @NotBlank(message = "Title is mandatory" )
    @Size(max = 100, message = "Title can have at most 100 characters")
    private String title;
    @Size(max = 500, message = "Description can have at most 500 characters")
    private String description;
    private boolean completed;
    public  TodoRequest() {
    }
    public TodoRequest(String title, String description,boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public boolean isCompleted() {return completed;}
    public void setCompleted(boolean completed) {this.completed = completed;}

}

