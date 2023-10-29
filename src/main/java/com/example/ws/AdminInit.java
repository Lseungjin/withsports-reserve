package com.example.ws;

import com.example.ws.domain.entity.*;
import com.example.ws.domain.value.Role;
import com.example.ws.dto.sportstype.SportsTypeDto;
import com.example.ws.repository.AdminRepository;
import com.example.ws.repository.StadiumRepository;
import com.example.ws.service.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
//import javax.annotation.PostConstruct;


import java.util.ArrayList;
import java.util.List;

/**
 * 초기 admin 데이터 저장 클래스, DI(의존 관계) 주입 후 바로 실행 될 메서드 정의
 */
@Component
@RequiredArgsConstructor
public class AdminInit {

    private final UserService userService;
    private final AdminRepository adminRepository;
    private final StadiumRepository stadiumRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void init(){
        User user = User.createUser()
                .email("admin")
                .password(bCryptPasswordEncoder.encode("admin"))
                .name("admin")
                .age(0)
                .address("admin")
                .detailAddress("admin")
                .role(Role.ROLE_ADMIN)
                .build();

        User savedUser = userService.createUser(user);
        Admin admin = Admin.createAdmin()
                .name(savedUser.getName())
                .build();
        adminRepository.save(admin);

        Stadium stadium = Stadium.createStadium()
                .stadiumName("올드트래포드")
                .address("서울특별시 강서구")
                .detailAddress("A빌딩")
                .dateAccept(100)
                .timeAccept(10)
                .build();
        stadium.setAdmin(admin);

        Sportstype soccer = Sportstype.createSportsType()
                .sportstypeName("축구")
                .quantity(40)
                .build();
        Sportstype basketball = Sportstype.createSportsType()
                .sportstypeName("농구")
                .quantity(6)
                .build();
        Sportstype futsal = Sportstype.createSportsType()
                .sportstypeName("풋살")
                .quantity(18)
                .build();

        soccer.addStadium(stadium);
        basketball.addStadium(stadium);
        futsal.addStadium(stadium);

        List<String> dateList=new ArrayList<>();
        dateList.add("2023.10.28");
        dateList.add("2023.10.29");
        dateList.add("2023.10.30");
        dateList.add("2023.10.31");

        List<Integer> timeList=new ArrayList<>();
        timeList.add(9);
        timeList.add(10);
        timeList.add(11);
        timeList.add(13);

        for (String date : dateList) {
            AvailableDate availableDate= AvailableDate.createAvailableDate()
                    .date(date)
                    .acceptCount(100)
                    .build();
            for (Integer time : timeList) {
                AvailableTime availableTime = AvailableTime.createAvailableTime()
                        .time(time)
                        .acceptCount(10)
                        .build();
                availableTime.addAvailableDate(availableDate);
            }
            availableDate.addStadium(stadium);
        }
        stadium.setTotalSportstypeQuantity(64);

        stadiumRepository.save(stadium);
    }
}