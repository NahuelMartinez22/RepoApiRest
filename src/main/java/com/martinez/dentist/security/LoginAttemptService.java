package com.martinez.dentist.security;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Frena los ataques de prueba de contraseñas:
// - 5 intentos fallidos del mismo usuario desde la misma IP bloquean ese usuario en esa IP por 15 minutos;
// - 20 intentos fallidos desde una IP, con cualquier usuario, bloquean la IP por 15 minutos.
// Los intentos cuentan dentro de una ventana de 15 minutos: los fallos viejos se olvidan solos.
// Todo vive en memoria y se limpia solo, para que no crezca sin límite.
@Service
public class LoginAttemptService {

    static final int MAX_ATTEMPTS = 5;
    static final int MAX_ATTEMPTS_PER_IP = 20;
    static final Duration BLOCK_DURATION = Duration.ofMinutes(15);
    static final Duration WINDOW = Duration.ofMinutes(15);
    // Cantidad de registros a partir de la cual se borran los vencidos
    static final int CLEANUP_THRESHOLD = 5_000;
    // Tope duro de memoria: si se supera, se descartan los registros que no están bloqueados
    static final int MAX_ENTRIES = 50_000;

    private final Map<String, Attempts> byUser = new ConcurrentHashMap<>();
    private final Map<String, Attempts> byIp = new ConcurrentHashMap<>();
    private final Clock clock;

    public LoginAttemptService() {
        this(Clock.systemUTC());
    }

    LoginAttemptService(Clock clock) {
        this.clock = clock;
    }

    public boolean isBlocked(String username, String ip) {
        Instant now = clock.instant();
        return isBlocked(byUser, userKey(username, ip), now) || isBlocked(byIp, ipKey(ip), now);
    }

    public void loginFailed(String username, String ip) {
        Instant now = clock.instant();
        registerFailure(byUser, userKey(username, ip), MAX_ATTEMPTS, now);
        registerFailure(byIp, ipKey(ip), MAX_ATTEMPTS_PER_IP, now);
        cleanupIfNeeded(now);
    }

    public void loginSucceeded(String username, String ip) {
        // Solo se reinicia el contador de ese usuario: el de la IP sigue contando intentos contra otros usuarios
        byUser.remove(userKey(username, ip));
    }

    int trackedEntries() {
        return byUser.size() + byIp.size();
    }

    private boolean isBlocked(Map<String, Attempts> map, String key, Instant now) {
        Attempts current = map.get(key);
        if (current == null) {
            return false;
        }
        if (current.isBlockedAt(now)) {
            return true;
        }
        if (current.isExpired(now)) {
            map.remove(key, current);
        }
        return false;
    }

    private void registerFailure(Map<String, Attempts> map, String key, int max, Instant now) {
        map.compute(key, (k, current) -> {
            Attempts next = current == null || current.isExpired(now) ? new Attempts(now) : current;
            next.failures++;
            if (next.failures >= max && next.blockedUntil == null) {
                next.blockedUntil = now.plus(BLOCK_DURATION);
            }
            return next;
        });
    }

    private void cleanupIfNeeded(Instant now) {
        if (trackedEntries() < CLEANUP_THRESHOLD) {
            return;
        }
        byUser.entrySet().removeIf(e -> e.getValue().isExpired(now));
        byIp.entrySet().removeIf(e -> e.getValue().isExpired(now));
        if (trackedEntries() > MAX_ENTRIES) {
            byUser.entrySet().removeIf(e -> !e.getValue().isBlockedAt(now));
            byIp.entrySet().removeIf(e -> !e.getValue().isBlockedAt(now));
        }
    }

    private String userKey(String username, String ip) {
        String user = username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
        return user + "|" + ip;
    }

    private String ipKey(String ip) {
        return ip == null ? "" : ip;
    }

    private static class Attempts {
        final Instant firstFailure;
        int failures;
        Instant blockedUntil;

        Attempts(Instant firstFailure) {
            this.firstFailure = firstFailure;
        }

        boolean isBlockedAt(Instant now) {
            return blockedUntil != null && now.isBefore(blockedUntil);
        }

        // Vencido: el bloqueo ya terminó, o pasó la ventana sin llegar a bloquearse
        boolean isExpired(Instant now) {
            if (blockedUntil != null) {
                return !now.isBefore(blockedUntil);
            }
            return !now.isBefore(firstFailure.plus(WINDOW));
        }
    }
}
