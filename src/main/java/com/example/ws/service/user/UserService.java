package com.example.ws.service.user;

import com.example.ws.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    User createUser(User user);
    User getUserByEmail(String email);
}
