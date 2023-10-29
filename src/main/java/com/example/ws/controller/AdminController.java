package com.example.ws.controller;


import com.example.ws.dto.stadium.*;
import com.example.ws.dto.reserve.ReserveItemWithUsernameDto;
import com.example.ws.dto.security.PrincipalDetails;
import com.example.ws.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 경기장이름으로 경기장 단건 조회
     */
    @GetMapping("/stadium")
    @ResponseBody
    public ResponseEntity<StadiumResponseDto> getStadium(@RequestParam("name") String stadiumName) {
        StadiumResponseDto stadiumResponseDto = adminService.getStadiumInfo(stadiumName);

        return ResponseEntity.ok(stadiumResponseDto);
    }

    /**
     * 경기장 등록 폼 랜더링
     */
    @GetMapping("/stadium/add")
    public String stadiumForm(Model model){
        model.addAttribute("stadiumRequestDto",new StadiumRequestDto());
        return "admin/stadiumRegister";
    }

    /**
     * 현재 어드민이 관리하는 경기장 목록 조회 (경기장이름, 주소만 조회)
     */
    @ResponseBody
    @GetMapping("/stadiums")
    public List<StadiumSimpleInfoDto> asd(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        List<StadiumSimpleInfoDto> stadiums = adminService.getAllSimpleStadiumInfo(principal.getName());
        return stadiums;
    }

    /**
     * 경기장 등록
     * @param authentication 등록되는 경기장에 admin을 추가해주기 위해 현재 인증 객체를 사용
     */
    @PostMapping("/stadium/add")
    public String addStadium(
            Authentication authentication,
            @Validated @ModelAttribute StadiumRequestDto form, BindingResult result, HttpServletRequest request) throws Exception{

        if(result.hasErrors()){
            return "admin/stadiumRegister";
        }

        makeSportstypeInfoMap(form.getSoccer(), form.getBasketball(), form.getFutsal(), form.getSportstypeInfoMap());

        timeParse(form);
        /**
         * /admin/** 으로 접근되었다는 것은 security filter를 지나 인가된 사용자라는 것. (Role = ADMIN)
         * 따라서 경기장 등록시 Authentication에서 얻어온 유저 정보를 그대로 사용 (경기장에 Admin을 넣어주기 위함)
         */
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        log.info("principal.name = {}", principal.getName());

        adminService.addStadium(form, principal.getName());

        return "redirect:/admin/stadium/list";
    }

    /**
     * 경기장 목록
     */
    @GetMapping("/stadium/list")
    public String stadiumList(@AuthenticationPrincipal PrincipalDetails principal, Model model,
                               @RequestParam(defaultValue = "noSearch")String addressSearch) {
        String adminName = principal.getName();
        List<StadiumListDto> stadiumList = adminService.getStadiumList(adminName,addressSearch);
        model.addAttribute("stadiumList", stadiumList);
        return "admin/stadiumList";
    }

    /**
     * 경기장 상세정보 조회
     */
    @GetMapping("/stadium/{stadiumId}")
    public String stadiumInfo(Model model,@PathVariable("stadiumId")Long id){

        StadiumUpdateDto stadiumUpdateDto = adminService.getStadium(id);
        model.addAttribute("stadiumUpdateDto",stadiumUpdateDto);

        return "admin/stadiumDetail";
    }

    /**
     * 경기장 수정
     */
    @PostMapping("/stadium/" +
            "edit/{stadiumId}")
    public String stadiumEdit(@PathVariable Long stadiumId,
                               @Validated @ModelAttribute StadiumUpdateDto stadiumUpdateDto,BindingResult result)
            throws ParseException {
        if(result.hasErrors()){
            return "admin/stadiumDetail";
        }
        stadiumUpdateDto.setId(stadiumId);
        makeSportstypeInfoMap(stadiumUpdateDto.getSoccer(), stadiumUpdateDto.getBasketball(),
                stadiumUpdateDto.getFutsal(), stadiumUpdateDto.getSportstypeInfoMap());
        adminService.stadiumUpdate(stadiumUpdateDto);

        return "redirect:/admin/stadium/list";
    }

    /**
     * 경기장 현황 조회
     */
    @GetMapping("/stadium/reserves/{stadiumId}")
    public String reserveCondition(@PathVariable Long stadiumId, Model model){
        List<ReserveItemWithUsernameDto> reserveItemConditions = adminService.getReserveItemCondition(stadiumId);

        model.addAttribute("reserveItemConditions",reserveItemConditions);
        return "admin/reserveCondition";
    }

    // 시간을 parseInt 되도록 만드는 메서드
    private void timeParse(StadiumRequestDto form) {
        form.setStartTime(form.getStartTime().split(":")[0]);
        form.setEndTime(form.getEndTime().split(":")[0]);
    }

    // sportstypeeInfoMap만드는 메서드
    private void makeSportstypeInfoMap(Integer soccer, Integer basketball, Integer futsal, Map<String,Integer> sportstypeInfoMap) {

        if(soccer !=null && soccer !=0){
            sportstypeInfoMap.put("축구", soccer);
        }
        if(basketball !=null && basketball !=0){
            sportstypeInfoMap.put("농구", basketball);
        }
        if(futsal !=null && futsal !=0){
            sportstypeInfoMap.put("풋살", futsal);
        }
    }

}
