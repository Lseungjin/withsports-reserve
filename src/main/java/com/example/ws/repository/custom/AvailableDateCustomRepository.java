package com.example.ws.repository.custom;

import com.example.ws.domain.entity.AvailableDate;

public interface AvailableDateCustomRepository {

    AvailableDate findAvailableDateByStadiumIdAndDate(Long stadiumId, String date);
}