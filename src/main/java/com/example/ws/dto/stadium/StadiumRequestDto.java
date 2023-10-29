package com.example.ws.dto.stadium;

import com.example.ws.domain.entity.Stadium;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 어드민으로부터 경기장등록 요청을 처리하기 위한 DTO
 */

@Data
@NoArgsConstructor
public class StadiumRequestDto {
    @NotEmpty(message = "경기장이름을 입력해주세요.")
    private String stadiumName;

    @NotEmpty(message = "예약가능 시작날짜를 선택해주세요.")
    private String startDate;
    @NotEmpty(message = "예약가능 종료날짜를 선택해주세요.")
    private String endDate;
    @NotNull(message = "일일 최대 예약가능 인원을 입력해주세요.")
    private Integer dateAccept;

    @NotNull(message = "예약가능 시작시간을 선택해주세요.")
    private String startTime;
    @NotNull(message = "예약가능 종료시간을 선택해주세요.")
    private String endTime;
    @NotNull(message = "시간당 최대 예약가능 인원을 입력해주세요.")
    private Integer timeAccept;

    @NotEmpty(message = "경기장 주소를 입력해주세요.")
    private String address;
    @NotEmpty(message = "경기장 상세주소를 입력해주세요.")
    private String detailAddress;



    private List<String> sportstypeNames = new ArrayList<>();

    private List<Integer> sportstypeQuantities = new ArrayList<>();

    @NotNull(message = "수량을 입력해주세요.")
    private Integer soccer;
    @NotNull(message = "수량을 입력해주세요.")
    private Integer basketball;
    @NotNull(message = "수량을 입력해주세요.")
    private Integer futsal;

    // 스포츠 종류마다 잔여수량을 달리하기 위해 Map 사용 (key:스포츠종류이름, value:잔여수령)
    private Map<String, Integer> sportstypeInfoMap = new HashMap<>();

    public Stadium toStadiumEntity() {
        return Stadium.createStadium()
                .stadiumName(this.stadiumName)
                .address(this.address)
                .detailAddress(this.detailAddress)
                .dateAccept(dateAccept)
                .timeAccept(timeAccept)
                .build();
    }

    @Builder(builderMethodName = "createStadiumRequestDto")
    public StadiumRequestDto(String stadiumName, String startDate, String endDate, Integer dateAccept,
                             String startTime, String endTime, Integer timeAccept, String address,
                             String detailAddress, Integer soccer,
                             Integer basketball, Integer futsal) {
        this.stadiumName = stadiumName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateAccept = dateAccept;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeAccept = timeAccept;
        this.address = address;
        this.detailAddress = detailAddress;
        this.soccer = soccer;
        this.basketball = basketball;
        this.futsal = futsal;

    }

}