package com.martinez.dentist.patients.services;

import com.martinez.dentist.patients.controllers.clinicalHistory.ClinicalFileDTO;
import com.martinez.dentist.patients.controllers.clinicalHistory.ClinicalHistoryResponseDTO;
import com.martinez.dentist.patients.models.ClinicalHistory;
import com.martinez.dentist.patients.models.Patient;
import com.martinez.dentist.patients.repositories.ClinicalFileRepository;
import com.martinez.dentist.patients.repositories.ClinicalHistoryRepository;
import com.martinez.dentist.patients.repositories.PatientRepository;
import com.martinez.dentist.professionals.models.Professional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicalHistoryServiceTest {

    @Mock
    private ClinicalHistoryRepository clinicalHistoryRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ClinicalFileRepository clinicalFileRepository;

    @InjectMocks
    private ClinicalHistoryService service;

    @Test
    void armaLosArchivosDesdeElResumenSinLeerSuContenido() {
        Patient patient = mock(Patient.class);
        when(patient.getId()).thenReturn(5L);
        when(patient.getFullName()).thenReturn("Ana Perez");
        when(patientRepository.findByDocumentNumber("123")).thenReturn(Optional.of(patient));

        Professional professional = mock(Professional.class);
        when(professional.getFullName()).thenReturn("Dra. Gomez");

        ClinicalHistory history = mock(ClinicalHistory.class);
        when(history.getId()).thenReturn(10L);
        when(history.getPatient()).thenReturn(patient);
        when(history.getProfessional()).thenReturn(professional);
        when(history.getProcedures()).thenReturn(List.of());
        when(clinicalHistoryRepository.findByPatientId(5L)).thenReturn(List.of(history));

        List<Object[]> rows = new ArrayList<>();
        rows.add(new Object[]{1L, "rx.png", "image/png", 1500L, 10L});
        when(clinicalFileRepository.findFileSummariesByHistoryIds(List.of(10L))).thenReturn(rows);

        List<ClinicalHistoryResponseDTO> result = service.getClinicalHistoryByPatient("123");

        assertThat(result).hasSize(1);
        List<ClinicalFileDTO> files = result.get(0).getFiles();
        assertThat(files).hasSize(1);
        assertThat(files.get(0).getId()).isEqualTo(1L);
        assertThat(files.get(0).getSize()).isEqualTo(1500L);
        verify(history, never()).getFiles();
    }
}
