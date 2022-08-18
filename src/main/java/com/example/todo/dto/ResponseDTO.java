package com.example.todo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T> {
    private String error;
    // 다른 모델의 dto 도 ResponseDTO를 이용할 수 있도록 제네릭 사용
    private List<T> data;
}
