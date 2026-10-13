package com.martinez.dentist.security;

import com.martinez.dentist.users.controllers.LoginDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private AuthenticationController controller;

    private MockHttpServletRequest requestFrom(String ip) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Real-IP", ip);
        request.addHeader("X-Forwarded-For", "9.9.9.9");
        return request;
    }

    @Test
    void respondeTooManyRequestsSinVerificarLaContrasenaSiEstaBloqueado() {
        when(loginAttemptService.isBlocked("admin", "1.2.3.4")).thenReturn(true);

        ResponseEntity<?> response = controller.login(new LoginDTO("admin", "x"), requestFrom("1.2.3.4"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        verifyNoInteractions(authenticationManager);
    }

    @Test
    void registraElIntentoFallidoConLaIpReal() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("mal"));

        assertThatThrownBy(() -> controller.login(new LoginDTO("admin", "mal"), requestFrom("1.2.3.4")))
                .isInstanceOf(BadCredentialsException.class);

        verify(loginAttemptService).loginFailed("admin", "1.2.3.4");
        verify(loginAttemptService, never()).loginSucceeded(any(), any());
    }
}
