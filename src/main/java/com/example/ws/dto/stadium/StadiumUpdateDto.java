package com.example.ws.dto.stadium;


import com.example.ws.domain.entity.Stadium;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class StadiumUpdateDto {

    private Long id;

    private String stadiumName;

    private String startDate;

    private String endDate;

    @NotNull(message = "일일 최대 예약가능 인원을 입력해주세요.")
    private Integer dateAccept;

    private String startTime;

    private String endTime;
    @NotNull(message = "시간당 최대 예약가능 인원을 입력해주세요.")
    private Integer timeAccept;

    private String address;
    private String detailAddress;


    @NotNull(message = "수량을 입력해주세요.")
    private Integer soccer;
    @NotNull(message = "수량을 입력해주세요.")
    private Integer basketball;
    @NotNull(message = "수량을 입력해주세요.")
    private Integer futsal;

    // 스포츠 종류마다 잔여수량을 달리하기 위해 Map 사용 (key:스포츠종류 이름, value:잔여수령)
    private Map<String, Integer> sportstypeInfoMap = new HashMap<>();

    public Stadium toStadiumEntity() {
        return Stadium.createStadium()
                .stadiumName(this.stadiumName)
                .address(this.address)
                .detailAddress(this.detailAddress)
                .build();
    }

    @Builder(builderMethodName = "createstadiumUpdateDto")
    public StadiumUpdateDto(Long id, String stadiumName, String startDate, String endDate, Integer dateAccept,
                            String startTime, String endTime, Integer timeAccept, String address,
                            String detailAddress, Integer soccer,
                            Integer basketball, Integer futsal) {
        this.id=id;
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