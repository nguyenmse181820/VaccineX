package com.sba301.vaccinex.service.spec;

import com.sba301.vaccinex.dto.response.AppointmentVerificationResponse;

import java.time.LocalDateTime;

public interface AppointmentVerificationService {
    AppointmentVerificationResponse verifyAppointmentAvailability(Integer vaccineId, LocalDateTime appointmentDate);
}