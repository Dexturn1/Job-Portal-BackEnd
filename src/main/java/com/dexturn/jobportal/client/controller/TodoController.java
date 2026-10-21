package com.dexturn.jobportal.client.controller;


import com.dexturn.jobportal.client.service.RestClientTodoService;
import com.dexturn.jobportal.dto.TodoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {
    
    private final RestClientTodoService restClientTodoService;


    @GetMapping()
    ResponseEntity<List<TodoDto>> findAll(){
        return ResponseEntity.ok(restClientTodoService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<TodoDto> findById(@PathVariable Long id){
      return ResponseEntity.ok(restClientTodoService.findById(id));
    }

    @PostMapping
    ResponseEntity<TodoDto> create(@RequestBody TodoDto todoDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(restClientTodoService.create(todoDto));
    }

    @PutMapping("/{id}")
    ResponseEntity<TodoDto> update(@RequestBody TodoDto todoDto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(restClientTodoService.update(id, todoDto));
    }


    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable Long id){
        restClientTodoService.delete(id);
        return ResponseEntity.ok("Todo deleted Successfully");
    }

}
