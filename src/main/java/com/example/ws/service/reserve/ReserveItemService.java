package com.example.ws.service.reserve;

import com.example.ws.dto.stadium.StadiumListDto;
import com.example.ws.dto.reserve.AvailableDateDto;
import com.example.ws.dto.reserve.AvailableTimeDto;
import com.example.ws.dto.reserve.ReserveItemSimpleDto;
import com.example.ws.dto.sportstype.SportsTypeReserveDto;

import java.util.List;

public interface ReserveItemService {

    List<StadiumListDto> getAllStadiumInfo(int offset, int limit);

    List<StadiumListDto> getAllStadiumInfoSearchByAddress(String address, int offset, int limit);

    List<AvailableDateDto> getAvailableDates(Long stadiumId);

    List<AvailableTimeDto> getAvailableTimes(Long id);

    List<SportsTypeReserveDto> getAvailableSportstypeNameList(Long stadiumId);

    Long reserve(String username, Long stadiumId, String sportstypeName, Long dateId, Long timeId);

    ReserveItemSimpleDto getReserveResult(String username);

    void validateDuplicateUser(String username);

    void cancelReserveItem(Long reserveItemId);
}
