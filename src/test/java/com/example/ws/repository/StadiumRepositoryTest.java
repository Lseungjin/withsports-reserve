package com.example.ws.repository;

import com.example.ws.domain.entity.Stadium;
import com.example.ws.domain.entity.Sportstype;
import com.example.ws.dto.stadium.StadiumSimpleInfoDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@SpringBootTest
public class StadiumRepositoryTest {

    @Autowired private StadiumRepository stadiumRepository;


    /**
     * Admin이 등록
     * 경기장이름, 주소, 상세주소, 예약가능날짜(시작-종료), 일일 예약가능인원, 예약가능시간(시작-종료), 시간당 예약가능인원, 스포츠타입 이름&수량 입력
     *
     * 예약가능날짜는 공휴일을 제외하고 List<String> 타입으로 변환
     * 예약가능시간은 시작-종료 시간을 1시간 단위로 나누어서 List<String> 타입으로 변환
     *    예약가능시간은 hh ~ hh로 넘어오게 하고 1시간 단위로 나누어 저장
     *
     */

    @Test
    @DisplayName("모든 경기장의 이름,주소 조회 쿼리 테스트")
    void 경기장이름_주소_조회(){
        List<StadiumSimpleInfoDto> list = stadiumRepository.findAllStadiumNameAndAddress();
        for (StadiumSimpleInfoDto dto : list) {
            System.out.println("dto.getStadiumName() = " + dto.getStadiumName());
            System.out.println("dto.getAddress() = " + dto.getAddress());
        }

        // 3개 등록 하나는 enabled=false
        Assertions.assertThat(list.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("경기장 이름으로 조회 테스트")
    void 경기장이름_조회() {
        Stadium stadium =stadiumRepository.findByStadiumName("올드트래포드")
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("경기장이름없음");
                });
        String stadiumName = stadium.getStadiumName();
        List<Sportstype> sportstypes = stadium.getSportstypes();
        System.out.println("sportstypes.size() = " + sportstypes.size());
    }
}
