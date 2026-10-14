package com.martinez.dentist.security;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Bloquea temporalmente el login después de varios intentos fallidos con el mismo usuario
// desde la misma IP, para frenar ataques de prueba de contraseñas.
@Service
public class LoginAttemptService {

    static final int MAX_ATTEMPTS = 5;
    static final Duration BLOCK_DURATION = Duration.ofMinutes(15);

    private final Map<String, Attempts> attempts = new ConcurrentHashMap<>();
    private final Clock clock;

    public LoginAttemptService() {
        this(Clock.systemUTC());
    }

    LoginAttemptService(Clock clock) {
        this.clock = clock;
    }

    public boolean isBlocked(String username, String ip) {
        Attempts current = attempts.get(key(username, ip));
        if (current == null || current.blockedUntil == null) {
            return false;
        }
        if (clock.instant().isBefore(current.blockedUntil)) {
            return true;
        }
        attempts.remove(key(username, ip));
        return false;
    }

    public void loginFailed(String username, String ip) {
        attempts.compute(key(username, ip), (k, current) -> {
            Attempts next = current == null ? new Attempts() : current;
            next.failures++;
            if (next.failures >= MAX_ATTEMPTS) {
                next.blockedUntil = clock.instant().plus(BLOCK_DURATION);
            }
            return next;
        });
    }

    public void loginSucceeded(String username, String ip) {
        attempts.remove(key(username, ip));
    }

    private String key(String username, String ip) {
        String user = username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
        return user + "|" + ip;
    }

    private static class Attempts {
        int failures;
        Instant blockedUntil;
    }
}
