package com.martinez.dentist.security;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class LoginAttemptServiceTest {

    private static class MutableClock extends Clock {
        Instant now = Instant.parse("2026-10-13T12:00:00Z");
        @Override public ZoneOffset getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(java.time.ZoneId zone) { return this; }
        @Override public Instant instant() { return now; }
    }

    private final MutableClock clock = new MutableClock();
    private final LoginAttemptService service = new LoginAttemptService(clock);

    @Test
    void bloqueaDespuesDeCincoIntentosFallidos() {
        for (int i = 0; i < 4; i++) {
            service.loginFailed("admin", "1.2.3.4");
        }
        assertThat(service.isBlocked("admin", "1.2.3.4")).isFalse();

        service.loginFailed("admin", "1.2.3.4");

        assertThat(service.isBlocked("admin", "1.2.3.4")).isTrue();
    }

    @Test
    void desbloqueaPasadosQuinceMinutos() {
        for (int i = 0; i < 5; i++) {
            service.loginFailed("admin", "1.2.3.4");
        }
        clock.now = clock.now.plus(Duration.ofMinutes(15)).plusSeconds(1);

        assertThat(service.isBlocked("admin", "1.2.3.4")).isFalse();
    }

    @Test
    void unLoginCorrectoReiniciaElContador() {
        for (int i = 0; i < 4; i++) {
            service.loginFailed("admin", "1.2.3.4");
        }
        service.loginSucceeded("admin", "1.2.3.4");
        service.loginFailed("admin", "1.2.3.4");

        assertThat(service.isBlocked("admin", "1.2.3.4")).isFalse();
    }

    @Test
    void noBloqueaAlMismoUsuarioDesdeOtraIp() {
        for (int i = 0; i < 5; i++) {
            service.loginFailed("admin", "1.2.3.4");
        }

        assertThat(service.isBlocked("admin", "5.6.7.8")).isFalse();
        assertThat(service.isBlocked("ADMIN ", "1.2.3.4")).isTrue();
    }

    @Test
    void losFallosViejosSeOlvidanPasadaLaVentana() {
        for (int i = 0; i < 4; i++) {
            service.loginFailed("admin", "1.2.3.4");
        }
        clock.now = clock.now.plus(Duration.ofMinutes(16));
        service.loginFailed("admin", "1.2.3.4");

        assertThat(service.isBlocked("admin", "1.2.3.4")).isFalse();
    }

    @Test
    void bloqueaLaIpQuePruebaMuchosUsuarios() {
        for (int i = 0; i < 20; i++) {
            service.loginFailed("usuario" + i, "1.2.3.4");
        }

        assertThat(service.isBlocked("otro", "1.2.3.4")).isTrue();
        assertThat(service.isBlocked("otro", "5.6.7.8")).isFalse();
    }

    @Test
    void unLoginCorrectoNoDesbloqueaLaIp() {
        for (int i = 0; i < 20; i++) {
            service.loginFailed("usuario" + i, "1.2.3.4");
        }
        service.loginSucceeded("usuario0", "1.2.3.4");

        assertThat(service.isBlocked("usuario0", "1.2.3.4")).isTrue();
    }

    @Test
    void noAcumulaRegistrosSinLimite() {
        for (int i = 0; i < LoginAttemptService.CLEANUP_THRESHOLD; i++) {
            service.loginFailed("u" + i, "10.0." + (i / 250) + "." + (i % 250));
        }
        clock.now = clock.now.plus(Duration.ofMinutes(16));
        service.loginFailed("ultimo", "1.2.3.4");

        // Se borraron los vencidos; quedan solo los del último intento
        assertThat(service.trackedEntries()).isEqualTo(2);
    }
}
