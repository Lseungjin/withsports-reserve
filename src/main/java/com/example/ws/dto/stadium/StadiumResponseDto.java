package com.example.ws.dto.stadium;

import com.example.ws.domain.entity.AvailableDate;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 경기장 조회 결과를 위한 dto
 * json 포맷을 빈 값 없이 맞추기 위해 stadiumRequestDto와 따로 사용
 */

@Data
@NoArgsConstructor
public class StadiumResponseDto {
    private String stadiumName;
    private List<AvailableDate> availableDates;
    private String address;
    private String detailAddress;

    private Map<String, Integer> sportstypeInfoMap = new HashMap<>();

    public StadiumResponseDto createDto(String stadiumName, List<AvailableDate> availableDates,
                                        String address, String detailAddress, Map<String, Integer> sportstypeInfoMap) {
        this.stadiumName = stadiumName;
        this.availableDates = availableDates;
        this.address = address;
        this.detailAddress = detailAddress;
        this.sportstypeInfoMap = sportstypeInfoMap;

        return this;
    }
}