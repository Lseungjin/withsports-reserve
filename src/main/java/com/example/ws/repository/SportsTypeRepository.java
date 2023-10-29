package com.example.ws.repository;

import com.example.ws.domain.entity.Sportstype;
import com.example.ws.repository.custom.SportsTypeCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SportsTypeRepository extends JpaRepository<Sportstype, Long>, SportsTypeCustomRepository {
}