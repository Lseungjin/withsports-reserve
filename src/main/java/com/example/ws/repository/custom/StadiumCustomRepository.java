package com.example.ws.repository.custom;

import com.example.ws.domain.entity.Stadium;
import com.example.ws.dto.stadium.StadiumListDto;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StadiumCustomRepository {

    List<StadiumListDto> findAllStadiumInfo(Long id);

    Optional<Stadium> findStadiumDetail(Long id);

    List<StadiumListDto> findStadiumListPaging(int offset, int limit);

    List<StadiumListDto> findStadiumListByAddressPaging(int offset, int limit, @Param("address") String address);

    List<StadiumListDto> findStadiumListByAddressAndAdmin(@Param("address") String address, Long adminId);

}
