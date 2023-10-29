package com.example.ws.dto.reserve;

import com.example.ws.domain.entity.ReserveItem;
import com.example.ws.domain.value.ReserveStatus;
//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReserveItemWithUsernameDto {

    private Long reserveItemId;

    private String username;

    private String stadiumName;

    private String sportstypeName;

    private String reserveDate;

    private Integer reserveTime;

    private ReserveStatus reserveStatus;

    public ReserveItemWithUsernameDto(ReserveItem reserveItem){
        this.reserveItemId = reserveItem.getId();
        this.username = reserveItem.getUser().getName();
        this.stadiumName = reserveItem.getStadium().getStadiumName();
        this.sportstypeName = reserveItem.getSportstypeName();
        this.reserveDate = reserveItem.getReserveDate();
        this.reserveTime = reserveItem.getReserveTime();
        this.reserveStatus = reserveItem.getStatus();
    }

}
