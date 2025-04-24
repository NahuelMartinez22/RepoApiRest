package com.martinez.dentist.users.services;

import com.martinez.dentist.users.models.User;

public interface TokenService {
    String generateAndSaveToken(User user);
    boolean isTokenValid(String token);
}
