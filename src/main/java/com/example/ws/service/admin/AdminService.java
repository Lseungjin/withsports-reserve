package com.example.ws.service.admin;

import com.example.ws.dto.stadium.*;
import com.example.ws.dto.reserve.ReserveItemWithUsernameDto;

import java.text.ParseException;
import java.util.List;

public interface AdminService {

    /**
     * 경기장등록
     */
    Long addStadium(StadiumRequestDto stadiumAddDto, String adminName) throws Exception;


    /**
     * 경기장이름으로 경기장 단건 조회
     */
    StadiumResponseDto getStadiumInfo(String stadiumName);

    /**
     * 어드민이 관리하는 병원 리스트를 보여주기 위한 메서드
     */
    List<StadiumSimpleInfoDto> getAllSimpleStadiumInfo(String name);

    List<StadiumListDto> getStadiumList(String name, String address);

    /**
     * 경기장 상세 정보 조회 후 dto로 변환
     */
    StadiumUpdateDto getStadium(Long id);

    /**
     * 경기장 update
     */
    Long stadiumUpdate(StadiumUpdateDto dto) throws ParseException;

    /**
     * 예약 현황 정보
     */
    List<ReserveItemWithUsernameDto> getReserveItemCondition(Long stadiumId);
}
