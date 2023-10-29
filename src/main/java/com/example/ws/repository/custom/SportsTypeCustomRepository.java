package com.example.ws.repository.custom;

import com.example.ws.domain.entity.Sportstype;

public interface SportsTypeCustomRepository {

    Sportstype findSportstype(Long stadiumId, String sportstypeName);

    Sportstype findSportstypeDisabled(Long stadiumId, String sportstypeName);
}
