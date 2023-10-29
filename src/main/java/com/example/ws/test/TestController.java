package com.example.ws.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {


    // Map<String, Integer> 형태로 데이터 받아보기
    @PostMapping("/listmap")
    public String listmapTest(@RequestBody TestSportstype testSportstype) {
        if (testSportstype != null) {
            List<String> sportstypeNames = testSportstype.getSportstypeNames();
            List<Integer> quantities = testSportstype.getQuantities();
            Map<String, Integer> sportstypeInfoMap = testSportstype.getSportstypeInfoMap();
            for (int i=0;i<sportstypeNames.size();i++) {
                sportstypeInfoMap.put(sportstypeNames.get(i), quantities.get(i));
            }

            for (String s : sportstypeInfoMap.keySet()) {
                log.info("sportstypeName = {}, quantity = {}", s, sportstypeInfoMap.get(s));
            }
        }
        return "ok";
    }

    @GetMapping("/test/ttt")
    public String adasd() {
        return "signUp_complete";
    }

    @Data
    static class TestSportstype{
        private List<String>sportstypeNames = new ArrayList<>();
        private List<Integer> quantities = new ArrayList<>();
        private Map<String, Integer> sportstypeInfoMap= new HashMap<>();
    }
}
