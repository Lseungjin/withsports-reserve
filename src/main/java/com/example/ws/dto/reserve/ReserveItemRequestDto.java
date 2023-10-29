package com.example.ws.dto.reserve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReserveItemRequestDto {
    private Long stadiumId;
    private String sportstypeName;
    private Long reserveDateId;
    private Long reserveTimeId;
}
