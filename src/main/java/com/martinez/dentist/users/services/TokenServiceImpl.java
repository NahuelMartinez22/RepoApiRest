package com.martinez.dentist.users.services;

import com.martinez.dentist.users.models.Token;
import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public String generateAndSaveToken(User user) {
        String tokenValue = UUID.randomUUID().toString();

        Token token = new Token();
        token.setToken(tokenValue);
        token.setUser(user);
        token.setExpirationDate(LocalDateTime.now().plusMinutes(30));

        tokenRepository.save(token);

        return tokenValue;
    }

    @Override
    public boolean isTokenValid(String tokenValue) {
        return tokenRepository.findByToken(tokenValue)
                .filter(t -> t.getExpirationDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}
