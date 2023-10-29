package com.example.ws.repository.custom;

import com.example.ws.domain.entity.AvailableDate;
import com.example.ws.domain.entity.AvailableTime;
import com.example.ws.domain.entity.ReserveItem;
import com.example.ws.domain.entity.Sportstype;

import java.util.List;

public interface ReserveItemCustomRepository {

    /**
     * 예약가능 날짜 조회
     */
    List<AvailableDate> findAvailableDatesByStadiumId(Long id);

    /**
     * 예약가능 시간 조회
     */
    List<AvailableTime> findAvailableTimesByAvailableDateId(Long id);


    /**
     * 예약가능 백신이름 조회
     */
    List<Sportstype> findAvailableSportstypes(Long stadiumId);

    /**
     * 예약 현황 조회
     */
    List<ReserveItem> findAllReserveItem(Long stadiumId);


}
