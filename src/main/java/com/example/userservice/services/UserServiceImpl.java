package com.example.userservice.services;

import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repositories.TokenRepository;
import com.example.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Override
    public User signup(String name, String email, String password) {

        if (userRepository.findByEmail(email).isPresent()) {
            //throw an exception
            return null;
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public Token login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {

            //throw an exception
            return null;
        }
        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {

            //throw an ecxeption
            return null;
        }

        Token token = new Token();
        token.setUser(user);
//        token.setTokenValue(UUID.randomUUID().toString());
        token.setTokenValue(RandomStringUtils.randomAlphanumeric(128));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 30);
        Date date = calendar.getTime();

        token.setExpiryAt(date);

        return tokenRepository.save(token);
    }

    @Override
    public User logout(Token token) {
        return null;
    }

    @Override
    public User validateToken(String tokenValue) {

        Optional<Token> tokenOptional = tokenRepository.findByTokenValueAndDeletedAndExpiryAtGreaterThan(tokenValue, false, new Date());

        if (tokenOptional.isEmpty()) {

            //throw an exception
            return null;
        }

        Token token = tokenOptional.get();

        return token.getUser();
    }
}
