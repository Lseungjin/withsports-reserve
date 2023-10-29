package com.example.ws.dto.stadium;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StadiumListDto {

    private Long id;

    private String stadiumName;

    private String address;

    private Integer qty;
}