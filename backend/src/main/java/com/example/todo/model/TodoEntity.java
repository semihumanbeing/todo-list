package com.example.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Todo")
public class TodoEntity {

    @Id
    @GeneratedValue(generator = "system-uuid") // jpa에서 자동 생성되어 @GenericGenerator를 참조해 사용한다.
    @GenericGenerator(name = "system-uuid", strategy = "uuid") // uuid를 사용하는 "system-uuid" 라는 이름의 GenericGenerator
    private String id;

    private String userId;
    private String title;
    private boolean done;


}
