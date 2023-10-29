package com.example.ws.dto.reserve;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailableTimeDto {

    private Long id;
    private Integer time;
}