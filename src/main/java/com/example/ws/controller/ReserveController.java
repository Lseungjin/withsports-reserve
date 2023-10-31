package com.example.ws.controller;


import com.example.ws.dto.Result;
import com.example.ws.dto.stadium.StadiumListDto;
import com.example.ws.dto.reserve.AvailableDateDto;
import com.example.ws.dto.reserve.AvailableTimeDto;
import com.example.ws.dto.reserve.ReserveItemRequestDto;
import com.example.ws.dto.reserve.ReserveItemSimpleDto;
import com.example.ws.dto.security.PrincipalDetails;
import com.example.ws.dto.sportstype.SportsTypeReserveDto;
import com.example.ws.exception.reserveexception.reserveException;
import com.example.ws.service.reserve.ReserveItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ReserveController {

    private final ReserveItemService reserveItemService;

    /**
     * 예약가능 경기장 조회
     */
    @GetMapping("/stadiums")
    public ResponseEntity<Result> stadiumList(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @AuthenticationPrincipal PrincipalDetails user) {

        try {
            reserveItemService.validateDuplicateUser(user.getUsername());
            List<StadiumListDto> stadiumListDtos = reserveItemService.getAllStadiumInfo(offset, limit);

            Result result = Result.createSuccessResult(stadiumListDtos);
            return ResponseEntity.ok(result);
        } catch (reserveException e) {
            return new ResponseEntity<>(e.getErrorResult(), e.getStatus());
        }
    }

    /**
     * 예약가능 경기장주소로 검색
     */
    @GetMapping("/search")
    public ResponseEntity<Result> searchByAddress(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam String addressSearch) {

        try {
            List<StadiumListDto> stadiumListDtos = reserveItemService.getAllStadiumInfoSearchByAddress(addressSearch, offset, limit);
            Result result = Result.createSuccessResult(stadiumListDtos);
            return ResponseEntity.ok(result);
        } catch (reserveException e) {
            return new ResponseEntity<>(e.getErrorResult(), e.getStatus());
        }
    }

    /**
     * 예약가능날짜 조회 및 선택
     */
    @GetMapping("/{stadiumId}/dates")
    public ResponseEntity<Result> getAvailableDates(@PathVariable Long stadiumId) {
        try {
            List<AvailableDateDto> availableDates = reserveItemService.getAvailableDates(stadiumId);
            Result result = Result.createSuccessResult(availableDates);
            return ResponseEntity.ok(result);
        } catch (reserveException e) {
            return new ResponseEntity<>(e.getErrorResult(), e.getStatus());
        }
    }

    /**
     * 예약가능시간 조회 및 선택
     */
    @GetMapping("/{stadiumId}/times")
    public ResponseEntity<Result> getAvailableTimes(
            @PathVariable Long stadiumId,
            @RequestParam(name="date") Long selectedDateId) {
        try {
            // 선택한 예약날짜의 pk로 예약가능시간 조회
            List<AvailableTimeDto> times = reserveItemService.getAvailableTimes(selectedDateId);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("date", selectedDateId);
            responseData.put("times", times);
            return ResponseEntity.ok(Result.createSuccessResult(responseData));
        } catch (reserveException e) {
            return new ResponseEntity<>(e.getErrorResult(), e.getStatus());
        }
    }


    /**
     * 예약가능 스포츠타입 조회 및 선택
     */
    @GetMapping("/{stadiumId}/sportstype")
    public ResponseEntity<Result> selectSportstype(
            @PathVariable Long stadiumId,
            @RequestParam(name = "date") Long selectedDateId,
            @RequestParam(name = "time") Long selectedTimeId) {

        try {
            List<SportsTypeReserveDto> sportstypes = reserveItemService.getAvailableSportstypeNameList(stadiumId);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sportstypes", sportstypes);
            responseData.put("date", selectedDateId);
            responseData.put("time", selectedTimeId);
            responseData.put("stadiumId", stadiumId);
            Result result = Result.createSuccessResult(responseData);
            return ResponseEntity.ok(result);
        } catch (reserveException e) {
            return new ResponseEntity<>(e.getErrorResult(), e.getStatus());
        }
    }

    /**
     * 예약처리
     */
    @PostMapping
    public ResponseEntity<Result> reserve(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody ReserveItemRequestDto reserveItemRequestDto) {
        try {
            log.info("stadiumId = {}", reserveItemRequestDto.getStadiumId());
            log.info("sportstypeName = {}", reserveItemRequestDto.getSportstypeName());
            log.info("reserveDateId = {}", reserveItemRequestDto.getReserveDateId());
            log.info("reserveTimeId = {}", reserveItemRequestDto.getReserveTimeId());

            String username = principal.getUsername();
            log.info("username = {}", username);

            Long savedUserId = reserveItemService.reserve(
                    username,
                    reserveItemRequestDto.getStadiumId(),
                    reserveItemRequestDto.getSportstypeName(),
                    reserveItemRequestDto.getReserveDateId(),
                    reserveItemRequestDto.getReserveTimeId()
            );

            Result result = Result.success();
            return ResponseEntity.ok(result);
        } catch (reserveException e) {
            return new ResponseEntity<>(e.getErrorResult(), e.getStatus());
        }
    }

    /**
     * 예약조회
     */
    @GetMapping
    public ResponseEntity<Result> reserveResult(@AuthenticationPrincipal PrincipalDetails principal) {
        try {
            String username = principal.getUsername();
            log.info("username = {}", username);

            ReserveItemSimpleDto reserveResult = reserveItemService.getReserveResult(username);
            if (reserveResult.getStadiumName() == null) {
                return new ResponseEntity<>(Result.createErrorResult("No reservation found"), HttpStatus.NOT_FOUND);
            }
            Result result = Result.createSuccessResult(reserveResult);
            return ResponseEntity.ok(result);
        } catch (reserveException e) {
            return new ResponseEntity<>(e.getErrorResult(), e.getStatus());
        }
    }

    /**
     * 예약취소
     */
    @DeleteMapping("/{reserveItemId}/cancel")
    public ResponseEntity<Result> cancel(@PathVariable Long reserveItemId) {
        try {
            reserveItemService.cancelReserveItem(reserveItemId);
            return ResponseEntity.ok(Result.success());
        } catch (reserveException e) {
            return new ResponseEntity<>(e.getErrorResult(), e.getStatus());
        }
    }

}