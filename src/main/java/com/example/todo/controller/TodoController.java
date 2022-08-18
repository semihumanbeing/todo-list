package com.example.todo.controller;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.TodoDTO;
import com.example.todo.model.TodoEntity;
import com.example.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("testTodo")
    public ResponseEntity<?> testTodo(){
        String str = todoService.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){

        try {
            // todoEntity 로 변환하기
            TodoEntity entity = TodoDTO.toEntity(dto);
            // id를 초기화한다
            entity.setId(null);
            // userid를 임시로 설정해준다.
            entity.setUserId(userId);
            // service를 통해 TodoEntity 생성
            List<TodoEntity> entities = todoService.create(entity);
            // 리턴된 Entity list 를 TodoDTO 리스트로 변경
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            // 변환된 TodoDTO list를 통해 ResponseDTO 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);

        } catch(Exception e){
            // 예외가 있는 경우 dto대신 error에 메시지를 넣어 리턴한다.

            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){

        // retrieve 메서드를 사용해 todolist를 가져온다.
        List<TodoEntity> entities = todoService.retrieve(userId);
        // 가져온 entity list를 dto list로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        // 변환된 dto리스트로 ResponseDTO 초기화
        ResponseDTO response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId,
                                        @RequestBody TodoDTO dto){
        // dto를 entity로 변환한다.
        TodoEntity entity = TodoDTO.toEntity(dto);
        // id를 temporary user id로 초기화하기
        entity.setUserId(userId);
        // entity update
        List<TodoEntity> entities = todoService.update(entity);
        // 리턴된 entity list를 dto list 로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        // 변환된 TodoDTO 리스트를 ResponseDTO로 초기화한다.
        ResponseDTO response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId,
                                        @RequestBody TodoDTO dto){

        try {
            // dto를 entity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);
            // userid를 temp userid 로 지정
            entity.setUserId(userId);
            // entity delete
            List<TodoEntity> entities = todoService.delete(entity);
            // 리턴된 entity list를 dto로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            // 변환된 dto리스트를 responseDTO로 변환한다.
            ResponseDTO response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);

        } catch (Exception e){
            String error = e.getMessage();
            ResponseDTO response = ResponseDTO.<TodoDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }

    }

}
