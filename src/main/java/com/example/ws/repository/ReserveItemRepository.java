package com.example.ws.repository;

import com.example.ws.domain.entity.ReserveItem;
import com.example.ws.dto.reserve.ReserveItemSimpleDto;
import com.example.ws.repository.custom.ReserveItemCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReserveItemRepository extends JpaRepository<ReserveItem, Long>, ReserveItemCustomRepository {

    @Query("select new com.example.ws.dto.reserve.ReserveItemSimpleDto(ri.id, ri.stadium.stadiumName, ri.sportstypeName, ri.reserveDate, ri.reserveTime, ri.status) " +
            "from ReserveItem  ri " +
            "where ri.user.id = :userId")
    Optional<ReserveItemSimpleDto> findByUserId(Long userId);
}
