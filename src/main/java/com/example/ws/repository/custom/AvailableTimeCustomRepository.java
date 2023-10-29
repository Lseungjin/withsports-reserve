package com.example.ws.repository.custom;

import com.example.ws.domain.entity.AvailableTime;

public interface AvailableTimeCustomRepository {

    AvailableTime findAvailableTimeById(Long timeId);

    AvailableTime findAvailableTimeByTimeAndDateId(Integer time, Long dateId);
}