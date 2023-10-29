package com.example.ws.controller;


import com.example.ws.dto.stadium.StadiumListDto;
import com.example.ws.dto.reserve.AvailableDateDto;
import com.example.ws.dto.reserve.AvailableTimeDto;
import com.example.ws.dto.reserve.ReserveItemRequestDto;
import com.example.ws.dto.reserve.ReserveItemSimpleDto;
import com.example.ws.dto.security.PrincipalDetails;
import com.example.ws.dto.sportstype.SportsTypeReserveDto;
import com.example.ws.service.reserve.ReserveItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ReserveController {

    private final ReserveItemService reserveItemService;

    /**
     * 예약가능 경기장 조회
     */
    @GetMapping("/stadiums")
    public String stadiumList(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit, Model model,
            @AuthenticationPrincipal PrincipalDetails user) {

        reserveItemService.validateDuplicateUser(user.getUsername());
        List<StadiumListDto> stadiumListDtos = reserveItemService.getAllStadiumInfo(offset, limit);
        model.addAttribute("stadiumList",stadiumListDtos);
        return "user/reserve/stadiumList";
    }

    /**
     * 예약가능 경기장주소로 검색
     */
    @GetMapping("/search")
    public String searchByAddress(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit, Model model,
            @RequestParam String addressSearch) {
        List<StadiumListDto> stadiumListDtos = reserveItemService.getAllStadiumInfoSearchByAddress(addressSearch, offset, limit);
        model.addAttribute("stadiumList", stadiumListDtos);
        return "user/reserve/stadiumList";
    }

    /**
     * 예약가능날짜 조회 및 선택
     */
    @GetMapping("/{stadiumId}/dates")
    public String getAvailableDates(@PathVariable Long stadiumId, Model model) {
        // 경기장이름으로 해당 경기장의 예약가능날짜 조회
        List<AvailableDateDto> availableDates = reserveItemService.getAvailableDates(stadiumId);
        model.addAttribute("stadiumId", stadiumId);
        model.addAttribute("dates", availableDates);

        return "user/reserve/reserveDateSelectForm";
    }

    /**
     * 예약가능시간 조회 및 선택
     */
    @GetMapping("/{stadiumId}/times")
    public String getAvailableTimes(
            @PathVariable Long stadiumId,
            @RequestParam(name="date") Long selectedDateId, Model model) {
        // 선택한 예약날짜의 pk로 예약가능시간 조회
        List<AvailableTimeDto> times = reserveItemService.getAvailableTimes(selectedDateId);
        model.addAttribute("date", selectedDateId);
        model.addAttribute("times", times);
        return "user/reserve/reserveTimeSelectForm";
    }

    /**
     * 예약가능 스포츠타입 조회 및 선택
     */
    @GetMapping("/{stadiumId}/sportstype")
    public String selectSportstype(
            @PathVariable Long stadiumId,
            @RequestParam(name = "date") Long selectedDateId,
            @RequestParam(name = "time") Long selectedTimeId, Model model) {

        List<SportsTypeReserveDto> sportstypes = reserveItemService.getAvailableSportstypeNameList(stadiumId);

        model.addAttribute("sportstypes", sportstypes);
        model.addAttribute("date", selectedDateId);
        model.addAttribute("time", selectedTimeId);
        model.addAttribute("stadiumId", stadiumId);

        return "user/reserve/reserveSportstypeSelectForm";
    }

    /**
     * 예약처리
     */
    @PostMapping
    public String reserve(
            @AuthenticationPrincipal PrincipalDetails principal,
            @ModelAttribute ReserveItemRequestDto reserveItemRequestDto,
            RedirectAttributes redirectAttributes) {
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

        return "redirect:/reserve";
    }

    /**
     * 예약조회
     */
    @GetMapping
    public String reserveResult(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
        String username = principal.getUsername();
        log.info("username = {}", username);

        ReserveItemSimpleDto reserveResult = reserveItemService.getReserveResult(username);
        if (reserveResult.getStadiumName() == null) {
            return "redirect:/";
        }
        model.addAttribute("reserveResult", reserveResult);
        return "user/reserve/ReserveResult";
    }

    /**
     * 예약취소
     */
    @GetMapping("/{reserveItemId}/cancel")
    public String cancel(@PathVariable Long reserveItemId) {
        reserveItemService.cancelReserveItem(reserveItemId);

        return "redirect:/";
    }
}