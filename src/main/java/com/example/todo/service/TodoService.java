package com.example.todo.service;

import com.example.todo.model.TodoEntity;
import com.example.todo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<TodoEntity> create(final TodoEntity entity){

        validate(entity);

        todoRepository.save(entity);
        log.info("Entity id ="+entity.getId());

        return todoRepository.findByUserId(entity.getUserId());

    }

    public List<TodoEntity> update(final TodoEntity entity){
        validate(entity);
        // 넘겨받은 entity id를 사용해 todoEntity를 가져온다.
        final Optional<TodoEntity> original = todoRepository.findById(entity.getId());

        original.ifPresent(todo ->{
            // 반환된 todoEntity가 존재하면 값을 덮어씌운다.
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            todoRepository.save(todo);
        });

        return retrieve(entity.getUserId());

    }

    public List<TodoEntity> delete(final TodoEntity entity){
        validate(entity);

        try {
            todoRepository.delete(entity);
        } catch (Exception e){
            log.error("error deleting entity"+ entity.getId(), e);
            // controller로 exception을 보낸다. 데이터베이스 내부 로직을 캡슐화하려면 새 exception object를 리턴한다.
            throw new RuntimeException("error deleting entity" + entity.getId());
        }
        return retrieve(entity.getUserId());

    }

    private void validate(TodoEntity entity) {
        // validation
        if(entity == null){
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }
        if(entity.getUserId()==null){
            log.warn("Unknown User");
            throw new RuntimeException("Unknown User");
        }
    }

    public List<TodoEntity> retrieve(final String userId){
        return todoRepository.findByUserId(userId);
    }

    public String testService(){
        TodoEntity entity = TodoEntity.builder().title("my first todo item").build();
        todoRepository.save(entity);
        TodoEntity savedEntity = todoRepository.findById(entity.getId()).get();
        return savedEntity.getTitle();

    }

}
