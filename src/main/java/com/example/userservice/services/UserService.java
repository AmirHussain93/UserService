package com.example.userservice.services;

import com.example.userservice.models.Token;
import com.example.userservice.models.User;

public interface UserService {
    User signup(String name,  String email, String password);
    Token login(String email, String password);
    User logout(Token token);
    User validateToken(String token);
}
