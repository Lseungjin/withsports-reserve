package com.example.ws.domain;

import com.example.ws.domain.entity.User;
import com.example.ws.domain.value.Role;
import com.example.ws.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback(false)
    void saveTest() {
        User user = User.createUser()
                .email("test@naver.com")
                .password("1234")
                .name("이승진")
                .age(26)
                .address("경기도 부천시")
                .detailAddress("좋은아파트 105-1304")
                .role(Role.ROLE_USER)
                .build();
        User savedUser = userRepository.save(user);


        Assertions.assertThat(user.getEmail()).isEqualTo("test@naver.com");
    }
}