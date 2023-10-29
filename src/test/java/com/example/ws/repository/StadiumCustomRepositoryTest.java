package com.example.ws.repository;

import com.example.ws.repository.custom.StadiumCustomRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class StadiumCustomRepositoryTest {

    @Autowired
    private StadiumCustomRepositoryImpl stadiumCustomRepository;
}
