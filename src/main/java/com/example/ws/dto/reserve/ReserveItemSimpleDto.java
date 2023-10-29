package com.example.ws.dto.reserve;

import com.example.ws.domain.value.ReserveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveItemSimpleDto {

    private Long reserveItemId;

    private String stadiumName;

    private String sportstypeName;

    private String reserveDate;

    private Integer reserveTime;

    private ReserveStatus reserveStatus;
}