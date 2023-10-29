package com.example.ws.service.admin;

import com.example.ws.domain.entity.*;

import com.example.ws.service.admin.AdminService;
import com.example.ws.dto.stadium.*;
import com.example.ws.dto.reserve.ReserveItemWithUsernameDto;
import com.example.ws.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import com.example.ws.service.Holiday;

@RequiredArgsConstructor
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final StadiumRepository stadiumRepository;
    private final AdminRepository adminRepository;
    private final Holiday holiday;
    private final AvailableTimeRepository availableTimeRepository;
    private final AvailableDateRepository availableDateRepository;
    private final ReserveItemRepository reserveItemRepository;

    /**
     * 경기장정보 등록
     */
    @Transactional
    @Override
    public Long addStadium(StadiumRequestDto stadiumRequestDto,String adminName) throws Exception{

        // 경기장 엔티티 생성
        Stadium stadium =stadiumRequestDto.toStadiumEntity();
        /**
         * 현재 Authentication 객체로부터 받은 adminName을 등록하는 경기장의 admin으로 설정하는 방식
         */
        Admin admin = adminRepository.findByName(adminName).get();
        stadium.setAdmin(admin);
        // 총 스포츠타입(풋살,축구,농구) 수량 (종류 상관X)
        Integer total = 0;
        // 스포츠타입 엔티티 생성 및 경기장엔티티에 add
        Map<String, Integer> sportstypeInfoMap = stadiumRequestDto.getSportstypeInfoMap();
        for (String key : sportstypeInfoMap.keySet()) {
            Sportstype sportstype = Sportstype.createSportsType()
                    .sportstypeName(key)
                    .quantity(sportstypeInfoMap.get(key))
                    .build();
            sportstype.addStadium(stadium);
            total +=sportstypeInfoMap.get(key);
        }
        stadium.setTotalSportstypeQuantity(total);

        /**
         * 예약 가능 날짜를 생성 (휴일제외)
         */
        // 예약가능시간
        List<Integer> availableTimeList = getAvailableTimes(stadiumRequestDto.getStartTime(),stadiumRequestDto.getEndTime());

        // 예약가능날짜
        List<String> holidays = holiday.holidayList(stadiumRequestDto.getStartDate(),stadiumRequestDto.getEndDate());
        List<String> availableDateList = holiday.availableDateList(stadiumRequestDto.getStartDate(), stadiumRequestDto.getEndDate(), holidays);

        for (String date : availableDateList) {
            AvailableDate availableDate= AvailableDate.createAvailableDate()
                    .date(date)
                    .acceptCount(stadiumRequestDto.getDateAccept())
                    .build();
            for (Integer time : availableTimeList) {
                AvailableTime availableTime= AvailableTime.createAvailableTime()
                        .time(time)
                        .acceptCount(stadiumRequestDto.getTimeAccept())
                        .build();
                availableTime.addAvailableDate(availableDate);
            }
            availableDate.addStadium(stadium);
        }


        Stadium savedStadium = stadiumRepository.save(stadium);

        return savedStadium.getId();
    }

    /**
     * 예약가능시간 처리 메서드
     */
    private List<Integer> getAvailableTimes(String startTime, String endTime) {
        int start = Integer.parseInt(startTime);
        int end = Integer.parseInt(endTime);
        List<Integer> availableTimes = new ArrayList<>();
        for (int i=start; i<=end;i++) {
            availableTimes.add(i);
        }
        return availableTimes;
    }

    /**
     * 경기장이름으로 경기장 정보 얻어오기
     */
    @Transactional(readOnly = true)
    @Override
    public StadiumResponseDto getStadiumInfo(String stadiumName) {
        Stadium findStadium = stadiumRepository.findByStadiumName(stadiumName)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("존재하지 않는 경기장입니다.");
                });
        List<Sportstype> sportstypes = findStadium.getSportstypes();
        Map<String, Integer> map = new HashMap<>();
        for (Sportstype sportstype : sportstypes) {
            map.put(sportstype.getSportstypeName(), sportstype.getQuantity());
        }

        List<AvailableDate> availableDates = findStadium.getAvailableDates();
        // 리턴 고쳐야 함
        StadiumResponseDto responseDto = new StadiumResponseDto();
        return responseDto.createDto(findStadium.getStadiumName(), availableDates, findStadium.getAddress(), findStadium.getDetailAddress(), map);
    }

    /**
     * 어드민이 관리하는 경기장 리스트를 보여주기 위한 메서드
     */
    @Override
    public List<StadiumSimpleInfoDto> getAllSimpleStadiumInfo(String name) {
        Admin admin = adminRepository.findByName(name).get();
        return stadiumRepository.findAllByAdmin(admin);
    }

    @Override
    public List<StadiumListDto> getStadiumList(String name,String address) {
        Admin admin = adminRepository.findByName(name).get();
        if(address.equals("noSearch"))
            return stadiumRepository.findAllStadiumInfo(admin.getId());

        return stadiumRepository.findStadiumListByAddressAndAdmin(address, admin.getId());
    }

    /**
     * 경기장 상세 정보 조회 후 dto로 변환
     */
    @Override
    public StadiumUpdateDto getStadium(Long id) {
        Optional<Stadium> stdiumDetail = stadiumRepository.findStadiumDetail(id);
        Stadium stadium = stdiumDetail.stream().findFirst().orElse(null);

        List<AvailableDate> availableDates = stadium.getAvailableDates();
        List<AvailableTime> availableTimes = availableDates.get(0).getAvailableTimes();
        List<Sportstype> sportstypes = stadium.getSportstypes();

        Map<String,Integer> sportstypeMap=new HashMap<>();

        for (Sportstype sportstype : sportstypes) {
            sportstypeMap.put(sportstype.getSportstypeName(),sportstype.getQuantity());
        }

        return StadiumUpdateDto.createstadiumUpdateDto()
                .id(stadium.getId())
                .stadiumName(stadium.getStadiumName())
                .address(stadium.getAddress())
                .detailAddress(stadium.getDetailAddress())
                .dateAccept(stadium.getDateAccept())
                .timeAccept(stadium.getTimeAccept())
                .startDate(availableDates.get(0).getDate())
                .endDate(availableDates.get(availableDates.size()-1).getDate())
                .startTime(String.valueOf(availableTimes.get(0).getTime()))
                .endTime(String.valueOf(availableTimes.get(availableTimes.size()-1).getTime()))
                .soccer(sportstypePresent(sportstypeMap,"축구"))
                .basketball(sportstypePresent(sportstypeMap,"농구"))
                .futsal(sportstypePresent(sportstypeMap,"풋살"))
                .build();
    }

    @Override
    @Transactional
    public Long stadiumUpdate(StadiumUpdateDto dto) throws ParseException {
        Optional<Stadium> stadiumDetail = stadiumRepository.findStadiumDetail(dto.getId());
        Stadium stadium = stadiumDetail.stream().findFirst().orElse(null);

        //수정 목록
        List<Sportstype> sportstypes = stadium.getSportstypes();

        //==스포츠타입 수정==//
        Map<String, Integer> sportstypeInfoMap = dto.getSportstypeInfoMap();

        Integer total = 0;

        //스포츠타입 이름 리스트. 추가된 스포츠, 수량이 0이된 스포츠확인 위해
        List<String> sportstypeNames=new ArrayList<>();
        for (Sportstype sportstype : sportstypes) {
            sportstypeNames.add(sportstype.getSportstypeName());
        }

        for(String key:sportstypeInfoMap.keySet()){
            total+=sportstypeInfoMap.get(key);
            //추가된 스포츠(농구,축구,풋살)가 있는 지 확인
            if(!sportstypeNames.contains(key)){
                Sportstype aditionalSportstype = Sportstype.createSportsType()
                        .sportstypeName(key)
                        .quantity(sportstypeInfoMap.get(key))
                        .build();
                aditionalSportstype.addStadium(stadium);
            }
            // 기존의 스포츠타입의 수량이 바뀌었는지 확인
            else {
                for (Sportstype sportstype : sportstypes) {
                    if (sportstype.getSportstypeName().equals(key)) {
                        //수량 수정 시, 0을 입력하면 dto로 전달이 안되기 때문에 확인을 위한 과정
                        sportstypes.remove(key);
                        //이미 있는 스포츠타입이라면 수량이 같으면 update 필요 x 수량이 다르면 update
                        if (sportstype.getQuantity() != sportstypeInfoMap.get(key)) {
                            sportstype.updateSportsTypeQty(sportstypeInfoMap.get(key));
                            sportstype.setEnabled(true);
                        }
                        break;
                    }
                }
            }
        }
        // 비어있지 않다면, 수정 폼에서 0으로 설정되었다는 뜻. 수량을 0으로 설정하자
        if(!sportstypeNames.isEmpty()){
            for (String sportstypeName : sportstypeNames) {
                Sportstype sportstype = sportstypes.stream().filter(v -> v.getSportstypeName().equals(sportstypeName)).findFirst().orElse(null);
                if(sportstype!=null){
                    sportstype.updateSportsTypeQty(0);
                    sportstype.setEnabled(false);
                }
            }
        }

        //총 수량의 합이 같다면 update x
        if(total!=stadium.getTotalQuantity()) {
            //원래 0이었다면 false 였으니
            if(stadium.getTotalQuantity()==0)
                stadium.setEnabled(true);

            stadium.setTotalSportstypeQuantity(total);

            if(stadium.getTotalQuantity()==0)
                stadium.setEnabled(false);
        }

        //경기장 예약가능 날짜 리스트
        List<AvailableDate> availableDates = stadium.getAvailableDates();

        //==dateAccept수정부분==//
        Integer dateAcceptCount = dto.getDateAccept();
        Integer originDateAccept = stadium.getDateAccept();
        //dateAccept가 수정되었다면
        if(originDateAccept != dateAcceptCount){
            stadium.updateDateAccept(dateAcceptCount);
            int updateDateAcceptCount = dateAcceptCount - originDateAccept;

            List<Long> availableDateIds=new ArrayList<>();

            //수정된 dateAccept 적용 시, 0보다 작거나 같아질 경우
            boolean flag=false;
            for (AvailableDate availableDate : availableDates) {
                if(availableDate.getAcceptCount()+updateDateAcceptCount<=0){
                    availableDateIds.add(availableDate.getId());
                    flag=true;
                }
            }
            availableDateRepository.updateAvailableDateAcceptCount(updateDateAcceptCount
                    ,stadium.getId());
            if(flag)
            {
                availableDateRepository.updateAvailableDateAcceptCountToZero(availableDateIds);
            }
        }

        //==timeAccept수정부분==//
        Integer timeAcceptCount = dto.getTimeAccept();
        Integer originTimeAccept = stadium.getTimeAccept();

        //timeAccept가 수정되었다면
        if(originTimeAccept !=timeAcceptCount){
            int updateAcceptCount = timeAcceptCount - originTimeAccept;

            stadium.updateTimeAccept(timeAcceptCount);

            List<Long> availableDateIds=new ArrayList<>();
            List<Long> availableTimeIds=new ArrayList<>();

            boolean flag=false;
            for (AvailableDate availableDate : availableDates) {
                availableDateIds.add(availableDate.getId());
                List<AvailableTime> availableTimes = availableDate.getAvailableTimes();

                //수량이 0보다 작거나 같아지는 것이 있으면
                for (AvailableTime availableTime : availableTimes) {
                    if(availableTime.getAcceptCount()+updateAcceptCount<=0){
                        availableTimeIds.add(availableTime.getId());
                        flag=true;
                    }
                }
            }
            availableTimeRepository.updateAvailableTimeAcceptCount(updateAcceptCount
                    ,availableDateIds);
            if(flag){
                availableTimeRepository.updateAvailableTimeAcceptCountToZero(availableTimeIds);
            }
        }

        return stadium.getId();
    }

    /**
     * 예약 현황 정보 얻어오기
     */
    @Transactional(readOnly = true)
    @Override
    public List<ReserveItemWithUsernameDto> getReserveItemCondition(Long stadiumId) {
        List<ReserveItem> reserveItems = reserveItemRepository.findAllReserveItem(stadiumId);
        if(reserveItems.isEmpty()) {
            return null;
        }

        return reserveItems.stream()
                .map(ri->new ReserveItemWithUsernameDto(ri))
                .collect(Collectors.toList());
    }

    /**
     * 경기장 정보 조회 시 , 해당 스포츠타입이 존재하는 지에 대한 여부
     */
    private Integer sportstypePresent(Map<String, Integer> sportstypeMap,String key){
        Integer sportstypeQty = sportstypeMap.get(key);

        if(sportstypeQty ==null)
            return 0;
        return sportstypeQty;
    }
}