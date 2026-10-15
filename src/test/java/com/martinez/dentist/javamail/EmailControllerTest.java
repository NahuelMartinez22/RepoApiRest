package com.martinez.dentist.javamail;

import com.martinez.dentist.users.models.Token;
import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.repositories.TokenRepository;
import com.martinez.dentist.users.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController controller;

    @Test
    void noEnviaCorreoSiElEmailNoEsDeUnUsuario() {
        when(userRepository.findByEmail("desconocido@ejemplo.com")).thenReturn(Optional.empty());

        ResponseEntity<String> response = controller.enviarCorreo(new PasswordRecoveryRequest("desconocido@ejemplo.com"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(emailService, never()).send(any());
        verify(tokenRepository, never()).save(any());
    }

    @Test
    void enviaSoloElEnlaceDeRecuperacionAlUsuarioRegistrado() {
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("usuario@clinica.com");
        when(userRepository.findByEmail("usuario@clinica.com")).thenReturn(Optional.of(user));

        ResponseEntity<String> response = controller.enviarCorreo(new PasswordRecoveryRequest("usuario@clinica.com"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ArgumentCaptor<EmailDTO> sent = ArgumentCaptor.forClass(EmailDTO.class);
        verify(emailService).send(sent.capture());
        assertThat(sent.getValue().getRecipient()).isEqualTo("usuario@clinica.com");
        assertThat(sent.getValue().getSubject()).isEqualTo("Recuperación de contraseña");
        assertThat(sent.getValue().getBody()).contains("/cambiar-contrasena?token=");
        verify(tokenRepository).deleteExpiredOrOwnedBy(any(LocalDateTime.class), eq(user));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void armaElEnlaceConLaDireccionDelFrontendConfigurada() {
        ReflectionTestUtils.setField(controller, "frontendUrl", "http://localhost:3000/");
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("usuario@clinica.com");
        when(userRepository.findByEmail("usuario@clinica.com")).thenReturn(Optional.of(user));

        controller.enviarCorreo(new PasswordRecoveryRequest("usuario@clinica.com"));

        ArgumentCaptor<EmailDTO> sent = ArgumentCaptor.forClass(EmailDTO.class);
        verify(emailService).send(sent.capture());
        assertThat(sent.getValue().getBody()).contains("http://localhost:3000/cambiar-contrasena?token=");
    }

    @Test
    void rechazaEmailVacio() {
        ResponseEntity<String> response = controller.enviarCorreo(new PasswordRecoveryRequest("  "));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(emailService);
    }

    @Test
    void cambiaLaContrasenaConTokenValidoEnElCuerpo() {
        User user = mock(User.class);
        Token token = new Token();
        token.setToken("abc");
        token.setUser(user);
        token.setExpirationDate(LocalDateTime.now().plusMinutes(30));
        when(tokenRepository.findByToken("abc")).thenReturn(Optional.of(token));
        when(passwordEncoder.encode("NuevaClave1")).thenReturn("hash");

        ResponseEntity<String> response = controller.cambiarPassword(new ResetPasswordRequest("abc", "NuevaClave1"), null, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(user).updatePassword("hash");
        verify(tokenRepository).delete(token);
    }

    @Test
    void rechazaTokenVencido() {
        Token token = new Token();
        token.setToken("viejo");
        token.setExpirationDate(LocalDateTime.now().minusMinutes(1));
        when(tokenRepository.findByToken("viejo")).thenReturn(Optional.of(token));

        ResponseEntity<String> response = controller.cambiarPassword(new ResetPasswordRequest("viejo", "x"), null, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(passwordEncoder, never()).encode(any());
    }
}
