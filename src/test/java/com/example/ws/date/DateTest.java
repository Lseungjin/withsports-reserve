package com.example.ws.date;

import com.example.ws.service.date.HolidayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
public class DateTest {

    @Autowired
    private HolidayService holidayService;

    @Test
    void 예약가능시간() {
        String input_start = "6";
        String input_end = "22";

        int start = Integer.parseInt(input_start);
        int end = Integer.parseInt(input_end);
        List<Integer> availableTimes = new ArrayList<>();
        for (int i=start; i<=end;i++) {
            availableTimes.add(i);
        }

        for (Integer availableTime : availableTimes) {
            System.out.println("availableTime = " + availableTime);
        }
    }

    // 시작날짜 - 종료날짜 사이의 모든 날짜
    @Test
    void 예약가능날짜() throws ParseException {
        String s1 = "20231028";
        String s2 = "20231035";

        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        Date d1 = df.parse(s1);
        Date d2 = df.parse(s2);

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime( d1 );
        c2.setTime( d2 );


        while( c1.compareTo( c2 ) !=1 ){

            //출력
            System.out.printf("%tF\n",c1.getTime());

            //시작날짜 + 1 일
            c1.add(Calendar.DATE, 1);
        }
    }

    // 공휴일 구해오기
    @Test
    void 공휴일() {
        Set<String> holidays = holidayService.holidayArray("2023");
        for (String holiday : holidays) {
            System.out.println("holiday = " + holiday);
        }
    }
}