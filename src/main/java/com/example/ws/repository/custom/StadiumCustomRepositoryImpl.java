package com.example.ws.repository.custom;

import com.example.ws.domain.entity.Admin;
import com.example.ws.domain.entity.AvailableDate;
import com.example.ws.domain.entity.Stadium;
import com.example.ws.dto.stadium.StadiumListDto;
import com.example.ws.dto.stadium.StadiumRequestDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class StadiumCustomRepositoryImpl implements StadiumCustomRepository{

    private final EntityManager em;

    @Override
    public List<StadiumListDto> findAllStadiumInfo(Long id) {
        return em.createQuery(
                "select new com.example.ws.dto.stadium.StadiumListDto(h.id, h.stadiumName, h.address, h.totalQuantity) " +
                        "from Stadium h " +
                        "where h.admin.id = :id"
                , StadiumListDto.class).setParameter("id",id).getResultList();
    };

    /**
     * 경기장아이디로 경기장정보 조회
     */
    @Override
    public Optional<Stadium> findStadiumDetail(Long id){
        return Optional.of(em.createQuery(
                        "select distinct h from Stadium h " +
                                "join fetch h.admin a " +
                                "join fetch h.sportstypes v " +
                                "where h.id= :id",Stadium.class)
                .setParameter("id",id).getSingleResult());
    }

    /**
     * 예약가능 경기장 조회 + 페이징
     */
    @Override
    public List<StadiumListDto> findStadiumListPaging(int offset, int limit) {
        return em.createQuery(
                        "select new com.example.ws.dto.stadium.StadiumListDto(h.id, h.stadiumName, h.address, h.totalQuantity) " +
                                "from Stadium h " +
                                "where h.enabled = true", StadiumListDto.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * 주소로 예약가능 경기장 조회 + 페이징
     */
    @Override
    public List<StadiumListDto> findStadiumListByAddressPaging(int offset, int limit, @Param("address") String address) {
        return em.createQuery(
                        "select new com.example.ws.dto.stadium.StadiumListDto(h.id, h.stadiumName, h.address, h.totalQuantity) " +
                                "from Stadium h " +
                                "where h.enabled = true and h.address like '%'||:address||'%'", StadiumListDto.class)
                .setParameter("address", address)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * 주소, admin으로 경기장 조회
     */
    @Override
    public List<StadiumListDto> findStadiumListByAddressAndAdmin(@Param("address") String address, Long adminId) {
        return em.createQuery(
                        "select new com.example.ws.dto.stadium.StadiumListDto(h.id, h.stadiumName, h.address, h.totalQuantity) " +
                                "from Stadium h " +
                                "where h.admin.id= :adminId and h.address like '%'||:address||'%'", StadiumListDto.class)
                .setParameter("address", address)
                .setParameter("adminId",adminId)
                .getResultList();
    }
}
