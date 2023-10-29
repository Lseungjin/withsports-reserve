package com.example.ws.service.reserve;

import com.example.ws.domain.entity.*;
import com.example.ws.domain.value.ReserveStatus;
import com.example.ws.dto.stadium.StadiumListDto;
import com.example.ws.dto.reserve.AvailableDateDto;
import com.example.ws.dto.reserve.AvailableTimeDto;
import com.example.ws.dto.reserve.ReserveItemSimpleDto;
import com.example.ws.dto.sportstype.SportsTypeReserveDto;
import com.example.ws.repository.*;

import com.example.ws.repository.custom.SportsTypeCustomRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReserveItemServiceImpl implements ReserveItemService{

    private final SportsTypeCustomRepositoryImpl sportstypeCustomRepository;
    private final StadiumRepository stadiumRepository;
    private final AvailableDateRepository availableDateRepository;
    private final AvailableTimeRepository availableTimeRepository;
    private final UserRepository userRepository;
    private final ReserveItemRepository reserveItemRepository;


    /**
     * 유저가 예약하기 버튼을 눌렀을 때 모든 경기장의 간단한 정보 (경기장이름, 주소, 스포츠타입) 보여주기
     */
    @Override
    public List<StadiumListDto> getAllStadiumInfo(int offset, int limit) {
        return stadiumRepository.findStadiumListPaging(offset, limit);
    }

    @Override
    public List<StadiumListDto> getAllStadiumInfoSearchByAddress(String address, int offset, int limit) {

        return stadiumRepository.findStadiumListByAddressPaging(offset, limit, address);
    }

    /**
     * 경기장 이름으로 예약가능날짜 조회
     */
    @Override
    public List<AvailableDateDto> getAvailableDates(Long StadiumId) {

        return reserveItemRepository.findAvailableDatesByStadiumId(StadiumId)
                .stream().map( m -> new AvailableDateDto(m.getId(), m.getDate())).collect(Collectors.toList());
    }

    /**
     * 예약가능시간 조회
     */
    public List<AvailableTimeDto> getAvailableTimes(Long id) {

        return reserveItemRepository.findAvailableTimesByAvailableDateId(id)
                .stream().map(t -> new AvailableTimeDto(t.getId(), t.getTime())).collect(Collectors.toList());
    }

    /**
     * 예약가능스포츠타입 조회
     */
    @Override
    public List<SportsTypeReserveDto> getAvailableSportstypeNameList(Long stadiumId) {
        return reserveItemRepository.findAvailableSportstypes(stadiumId)
                .stream().map(v -> new SportsTypeReserveDto(v.getId(), v.getSportstypeName())).collect(Collectors.toList());
    }

    /**
     * 예약처리
     */
    @Override
    public Long reserve(String username, Long stadiumId, String sportstypeName, Long dateId, Long timeId) {
        Stadium stadium = stadiumRepository.findById(stadiumId).orElseThrow(
                () -> {
                    throw new IllegalArgumentException("존재하지 않는 경기장입니다.");
                }
        );
        Sportstype sportstype = sportstypeCustomRepository.findSportstype(stadiumId, sportstypeName);
        AvailableTime time = availableTimeRepository.findAvailableTimeById(timeId);

        time.decreaseCount();
        if (time.getAcceptCount() <= 0) time.setEnabled(false);

        stadium.removeStock();

        sportstype.removeStock();

        AvailableDate availableDate = availableDateRepository.findById(dateId).get();

        User user = userRepository.findByEmail(username).get();

        ReserveItem reserveItem = ReserveItem.createReserveItem()
                .stadium(stadium)
                .reserveDate(availableDate.getDate())
                .reserveTime(time.getTime())
                .status(ReserveStatus.COMP)
                .user(user)
                .sportstypeName(sportstypeName)
                .build();
        ReserveItem savedReserveItem = reserveItemRepository.save(reserveItem);

        return user.getId();
    }

    /**
     * 예약서 조회
     */
    @Override
    public ReserveItemSimpleDto getReserveResult(String username) {
        log.info("getReserveResult username = {}", username);
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> {
                    throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
                }
        );
        return reserveItemRepository.findByUserId(user.getId()).orElseGet(
                () -> { return new ReserveItemSimpleDto(); });
    }

    /**
     * 이미 예약한 회원인지 확인.
     */
    @Override
    public void validateDuplicateUser(String username){
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> {
                    throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
                }
        );
        Optional<ReserveItemSimpleDto> reserveItemByUserId = reserveItemRepository.findByUserId(user.getId());
        if(!reserveItemByUserId.isEmpty()){
            throw new IllegalStateException("이미 예약한 회원 입니다.");
        }

    }

    /**
     * 예약취소
     */
    @Override
    public void cancelReserveItem(Long reserveItemId) {
        ReserveItem reserveItem = reserveItemRepository.findById(reserveItemId).get();

        Stadium stadium = reserveItem.getStadium();
        stadium.cancel();

        Sportstype sportstype =sportstypeCustomRepository.findSportstypeDisabled(stadium.getId(), reserveItem.getSportstypeName());
        sportstype.cancel();

        AvailableDate date = availableDateRepository.findAvailableDateByStadiumIdAndDate(stadium.getId(), reserveItem.getReserveDate());
        AvailableTime time = availableTimeRepository.findAvailableTimeByTimeAndDateId(reserveItem.getReserveTime(), date.getId());
        time.cancel();

        reserveItemRepository.deleteById(reserveItemId);
    }
}