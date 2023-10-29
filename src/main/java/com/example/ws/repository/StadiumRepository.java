package com.example.ws.repository;

import com.example.ws.domain.entity.Admin;
import com.example.ws.domain.entity.Stadium;
import com.example.ws.dto.stadium.StadiumSimpleInfoDto;
import com.example.ws.repository.custom.StadiumCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StadiumRepository extends JpaRepository<Stadium, Long>, StadiumCustomRepository {

    Optional<Stadium> findByStadiumName(String stadiumName);

    /**
     *  StadiumSimpleInfoDto 를 이용한 모든 경기장의 이름, 주소 조회
     *  @return StadiumSimpleInfoDto
     */
    @Query("select new com.example.ws.dto.stadium.StadiumSimpleInfoDto(h.stadiumName,h.address) " +
            "from Stadium h " +
            "where h.enabled = true")
    List<StadiumSimpleInfoDto> findAllStadiumNameAndAddress();

    /**
     * 어드민이 관리하는 모든 경기장 정보 조회 (경기장이름, 장소)
     * 어드민이 등록한 모든 경기장의 간단한 정보만을 조회하기 위한 쿼리
     */
    @Query("select new com.example.ws.dto.stadium.StadiumSimpleInfoDto(h.stadiumName, h.address) " +
            "from Stadium h " +
            "where h.admin = :admin")
    List<StadiumSimpleInfoDto> findAllByAdmin(Admin admin);
}