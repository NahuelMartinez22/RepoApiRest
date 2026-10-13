package com.martinez.dentist.javamail;

import com.martinez.dentist.users.models.Token;
import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.repositories.TokenRepository;
import com.martinez.dentist.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Verifica el formato de los pedidos HTTP, incluido el que usa el frontend anterior.
@ExtendWith(MockitoExtension.class)
class EmailControllerHttpTest {

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

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void aceptaElCampoDestinatarioDelFrontendAnterior() throws Exception {
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("usuario@clinica.com");
        when(userRepository.findByEmail("usuario@clinica.com")).thenReturn(Optional.of(user));

        mvc.perform(post("/api/enviar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"destinatario\":\"usuario@clinica.com\"}"))
                .andExpect(status().isOk());

        verify(emailService).send(any(EmailDTO.class));
    }

    @Test
    void ignoraAsuntoYCuerpoEnviadosPorElCliente() throws Exception {
        when(userRepository.findByEmail("victima@ejemplo.com")).thenReturn(Optional.empty());

        mvc.perform(post("/api/enviar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recipient\":\"victima@ejemplo.com\",\"email\":\"victima@ejemplo.com\",\"subject\":\"Spam\",\"body\":\"<a href='x'>phishing</a>\"}"))
                .andExpect(status().isOk());

        verify(emailService, never()).send(any());
    }

    @Test
    void cambiaLaContrasenaConCuerpoJson() throws Exception {
        Token token = tokenValido();
        when(tokenRepository.findByToken("abc")).thenReturn(Optional.of(token));
        when(passwordEncoder.encode("NuevaClave1")).thenReturn("hash");

        mvc.perform(post("/api/cambiar-contrasena")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"abc\",\"newPassword\":\"NuevaClave1\"}"))
                .andExpect(status().isOk());

        verify(token.getUser()).updatePassword("hash");
    }

    @Test
    void siguePermitiendoLosParametrosDelFrontendAnterior() throws Exception {
        Token token = tokenValido();
        when(tokenRepository.findByToken("abc")).thenReturn(Optional.of(token));
        when(passwordEncoder.encode("NuevaClave1")).thenReturn("hash");

        mvc.perform(post("/api/cambiar-contrasena")
                        .param("token", "abc")
                        .param("newPassword", "NuevaClave1"))
                .andExpect(status().isOk());

        verify(token.getUser()).updatePassword("hash");
    }

    private Token tokenValido() {
        Token token = new Token();
        token.setToken("abc");
        token.setUser(mock(User.class));
        token.setExpirationDate(LocalDateTime.now().plusMinutes(30));
        return token;
    }
}
