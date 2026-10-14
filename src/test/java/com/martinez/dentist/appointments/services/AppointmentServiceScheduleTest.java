package com.martinez.dentist.appointments.services;

import com.martinez.dentist.appointments.controllers.AppointmentRequestDTO;
import com.martinez.dentist.appointments.models.Appointment;
import com.martinez.dentist.appointments.models.AppointmentState;
import com.martinez.dentist.appointments.repositories.AppointmentRepository;
import com.martinez.dentist.javamail.EmailService;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.repositories.PatientRepository;
import com.martinez.dentist.practices.models.Practice;
import com.martinez.dentist.practices.repositories.PracticeRepository;
import com.martinez.dentist.professionals.models.Professional;
import com.martinez.dentist.professionals.models.ProfessionalState;
import com.martinez.dentist.professionals.repositories.ProfessionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppointmentServiceScheduleTest {

    private static final LocalDateTime NUEVE = LocalDateTime.of(2026, 10, 14, 9, 0);

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private ProfessionalRepository professionalRepository;
    @Mock private EmailService emailService;
    @Mock private PracticeRepository practiceRepository;

    @InjectMocks
    private AppointmentService service;

    private Professional professional;

    @BeforeEach
    void setUp() {
        professional = mock(Professional.class);
        when(professional.getId()).thenReturn(3L);
        when(professional.getProfessionalState()).thenReturn(ProfessionalState.ACTIVE);
        when(professional.trabajaEsteDiaYHorario(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(true);
        when(professionalRepository.findById(3L)).thenReturn(Optional.of(professional));

        Patient patient = mock(Patient.class);
        when(patient.getDocumentNumber()).thenReturn("123");
        when(patientRepository.findByDocumentNumber("123")).thenReturn(Optional.of(patient));

        when(practiceRepository.findById(1L)).thenReturn(Optional.of(mock(Practice.class)));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0));
        when(appointmentRepository.findByProfessionalIdAndDateTimeBetweenOrderByDateTime(anyLong(), any(), any())).thenReturn(List.of());
        when(appointmentRepository.findByPatientDniAndDateTimeBetween(anyString(), any(), any())).thenReturn(List.of());
    }

    private AppointmentRequestDTO pedido(LocalDateTime start, LocalDateTime end) {
        AppointmentRequestDTO dto = new AppointmentRequestDTO();
        dto.setPatientDni("123");
        dto.setProfessionalId(3L);
        dto.setDateTime(start);
        dto.setEndDateTime(end);
        dto.setState(AppointmentState.PENDIENTE);
        return dto;
    }

    @Test
    void guardaUnTurnoDeTreintaMinutos() {
        service.createAppointment(pedido(NUEVE, NUEVE.plusMinutes(30)));

        ArgumentCaptor<Appointment> saved = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository).save(saved.capture());
        assertThat(saved.getValue().getEndDateTime()).isEqualTo(NUEVE.plusMinutes(30));
    }

    @Test
    void sinDuracionGuardaQuinceMinutos() {
        service.createAppointment(pedido(NUEVE, null));

        ArgumentCaptor<Appointment> saved = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository).save(saved.capture());
        assertThat(saved.getValue().getEndDateTime()).isEqualTo(NUEVE.plusMinutes(15));
    }

    @Test
    void rechazaSiPisaOtroTurnoDelProfesional() {
        Appointment existente = mock(Appointment.class);
        when(existente.getDateTime()).thenReturn(NUEVE.plusMinutes(15));
        when(existente.getEndDateTime()).thenReturn(NUEVE.plusMinutes(45));
        when(existente.getState()).thenReturn(AppointmentState.CONFIRMADO);
        when(appointmentRepository.findByProfessionalIdAndDateTimeBetweenOrderByDateTime(eq(3L), any(), any()))
                .thenReturn(List.of(existente));

        assertThatThrownBy(() -> service.createAppointment(pedido(NUEVE, NUEVE.plusMinutes(30))))
                .hasMessageContaining("El profesional ya tiene un turno");
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void rechazaSiElProfesionalEstaDeLicencia() {
        when(professional.estaDeLicencia(LocalDate.of(2026, 10, 14))).thenReturn(true);

        assertThatThrownBy(() -> service.createAppointment(pedido(NUEVE, NUEVE.plusMinutes(30))))
                .hasMessageContaining("licencia");
    }

    @Test
    void alEditarValidaSuperposicionIgnorandoElMismoTurno() {
        Appointment actual = mock(Appointment.class);
        when(actual.getId()).thenReturn(10L);
        when(actual.getDateTime()).thenReturn(NUEVE);
        when(actual.getEndDateTime()).thenReturn(NUEVE.plusMinutes(15));
        when(actual.getState()).thenReturn(AppointmentState.PENDIENTE);
        when(actual.getProfessional()).thenReturn(professional);
        when(actual.getPatientDni()).thenReturn("123");
        when(appointmentRepository.findById(10L)).thenReturn(Optional.of(actual));

        Appointment otro = mock(Appointment.class);
        when(otro.getId()).thenReturn(11L);
        when(otro.getDateTime()).thenReturn(NUEVE.plusMinutes(15));
        when(otro.getEndDateTime()).thenReturn(NUEVE.plusMinutes(30));
        when(otro.getState()).thenReturn(AppointmentState.PENDIENTE);
        when(appointmentRepository.findByProfessionalIdAndDateTimeBetweenOrderByDateTime(eq(3L), any(), any()))
                .thenReturn(List.of(actual, otro));

        // Extenderlo a 30 minutos pisa el turno de las 9:15
        assertThatThrownBy(() -> service.updateAppointment(10L, pedido(NUEVE, NUEVE.plusMinutes(30))))
                .hasMessageContaining("El profesional ya tiene un turno");
    }

    @Test
    void rangoDeFechasInvalido() {
        assertThatThrownBy(() -> service.getAppointmentsByProfessionalAndRange(3L,
                LocalDate.of(2026, 10, 31), LocalDate.of(2026, 10, 1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
