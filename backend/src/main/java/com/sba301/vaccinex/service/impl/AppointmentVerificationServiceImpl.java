package com.sba301.vaccinex.service.impl;

import com.sba301.vaccinex.dto.response.AppointmentVerificationResponse;
import com.sba301.vaccinex.dto.response.BatchAvailabilityContext;
import com.sba301.vaccinex.exception.BadRequestException;
import com.sba301.vaccinex.exception.EntityNotFoundException;
import com.sba301.vaccinex.pojo.Batch;
import com.sba301.vaccinex.pojo.Vaccine;
import com.sba301.vaccinex.pojo.VaccineSchedule;
import com.sba301.vaccinex.pojo.enums.VaccineScheduleStatus;
import com.sba301.vaccinex.repository.BatchRepository;
import com.sba301.vaccinex.repository.VaccineRepository;
import com.sba301.vaccinex.repository.VaccineScheduleRepository;
import com.sba301.vaccinex.service.spec.AppointmentVerificationService;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AppointmentVerificationServiceImpl implements AppointmentVerificationService {

    private final VaccineRepository vaccineRepository;
    private final VaccineScheduleRepository vaccineScheduleRepository;
    private final BatchRepository batchRepository;

    // Configurable constants
    private static final int MIN_DAYS_FOR_RESTOCK = 7;
    private static final int MAX_ADVANCE_BOOKING_DAYS = 90000;

    @Override
    public AppointmentVerificationResponse verifyAppointmentAvailability(
            Integer vaccineId,
            LocalDateTime appointmentDate
    ) {
        // Validate input parameters
        validateAppointmentRequest(vaccineId, appointmentDate);

        // Find the vaccine
        Vaccine vaccine = findVaccineWithValidation(vaccineId);

        // Calculate availability based on batch quantities
        BatchAvailabilityContext availabilityContext = calculateBatchAvailability(
                vaccine,
                appointmentDate
        );

        // Build and return verification response
        return buildAppointmentVerificationResponse(
                vaccine,
                appointmentDate,
                availabilityContext
        );
    }

    private void validateAppointmentRequest(Integer vaccineId, LocalDateTime appointmentDate) {
        LocalDateTime now = LocalDateTime.now();

        // Basic input validation
        if (vaccineId == null) {
            throw new BadRequestException("ID vaccine không được để trống");
        }

        // Prevent booking in the past
        if (appointmentDate.isBefore(now)) {
            throw new BadRequestException("Không thể đặt lịch hẹn trong quá khứ");
        }

        // Prevent booking too far in advance
        if (appointmentDate.isAfter(now.plusDays(MAX_ADVANCE_BOOKING_DAYS))) {
            throw new BadRequestException(
                    "Không thể đặt lịch hẹn trước " +
                            MAX_ADVANCE_BOOKING_DAYS + " ngày"
            );
        }
    }

    private Vaccine findVaccineWithValidation(Integer vaccineId) {
        return vaccineRepository.findById(vaccineId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy vaccine với ID: " + vaccineId
                ));
    }

    private BatchAvailabilityContext calculateBatchAvailability(
            Vaccine vaccine,
            LocalDateTime appointmentDate
    ) {
        LocalDate currentDate = LocalDate.now();
        LocalDate scheduledDate = appointmentDate.toLocalDate();

        // Trường hợp 1: Đặt lịch trên 14 ngày → available = true (luôn luôn chấp nhận)
        if (scheduledDate.isAfter(currentDate.plusDays(14))) {
            return BatchAvailabilityContext.builder()
                    .totalAvailable(Integer.MAX_VALUE)
                    .requiredVaccines(1)
                    .requiredVaccinesNext7Days(0)
                    .isAvailable(true)
                    .canBeRescheduled(true)
                    .availableBatches(Collections.emptyList())
                    .build();
        }

        // Find non-expired batches for the specific vaccine
        List<Batch> availableBatches = batchRepository.findByVaccineIdAndExpirationAfter(
                vaccine.getId(),
                appointmentDate
        );

        // Calculate total available quantity from batches
        int totalAvailable = availableBatches.stream()
                .mapToInt(Batch::getQuantity)
                .sum();

        // Lấy tất cả lịch hẹn PLANNED cho vaccine này
        List<VaccineSchedule> allPlannedVaccineSchedules = vaccineScheduleRepository.findAll().stream()
                .filter(schedule ->
                        vaccine.getId().equals(schedule.getVaccine().getId()) &&
                                VaccineScheduleStatus.PLANNED.equals(schedule.getStatus()) &&
                                !schedule.isDeleted()
                )
                .toList();

        // Số lịch hẹn đã đặt + 1 (cho lịch hẹn mới)
        int requiredVaccinesTotal = allPlannedVaccineSchedules.size() + 1;

        // Kiểm tra có đủ vaccine không
        boolean isAvailable = totalAvailable >= requiredVaccinesTotal;

        // Nếu không đủ vaccine, kiểm tra có thể dời lịch không
        boolean canBeRescheduled = false;
        int requiredVaccinesNext7Days = 0;

        if (!isAvailable) {
            // Đếm số lịch hẹn trong 7 ngày tới
            LocalDate nextWeekDate = currentDate.plusDays(MIN_DAYS_FOR_RESTOCK);

            List<VaccineSchedule> schedulesNext7Days = allPlannedVaccineSchedules.stream()
                    .filter(schedule -> {
                        LocalDate scheduleDate = schedule.getDate().toLocalDate();
                        return !scheduleDate.isAfter(nextWeekDate) && !scheduleDate.isBefore(currentDate);
                    })
                    .toList();

            // Số lượng vaccine cần trong 7 ngày tới
            requiredVaccinesNext7Days = schedulesNext7Days.size();

            // Thêm 1 nếu lịch hẹn này nằm trong 7 ngày tới
            if (!scheduledDate.isAfter(nextWeekDate)) {
                requiredVaccinesNext7Days++;
            }

            // Kiểm tra xem số lượng vaccine có thể đáp ứng cho 7 ngày tới không
            // Nếu totalAvailable >= requiredVaccinesNext7Days + 1 thì có thể dời lịch
            canBeRescheduled = totalAvailable >= requiredVaccinesNext7Days + 1;
        }

        return BatchAvailabilityContext.builder()
                .totalAvailable(totalAvailable)
                .requiredVaccines(requiredVaccinesTotal)
                .requiredVaccinesNext7Days(requiredVaccinesNext7Days)
                .isAvailable(isAvailable)
                .canBeRescheduled(canBeRescheduled)
                .availableBatches(availableBatches)
                .build();
    }

    private AppointmentVerificationResponse buildAppointmentVerificationResponse(
            Vaccine vaccine,
            LocalDateTime appointmentDate,
            BatchAvailabilityContext context
    ) {
        return AppointmentVerificationResponse.builder()
                .vaccineId(vaccine.getId())
                .vaccineName(vaccine.getName())
                .appointmentDate(appointmentDate)
                .isAvailable(context.isAvailable())
                .canBeRescheduled(context.canBeRescheduled())
                .availableQuantity(context.getTotalAvailable())
                .requiredQuantity(context.getRequiredVaccines())
                .requiredQuantityNext7Days(context.getRequiredVaccinesNext7Days())
                .message(generateAppointmentMessage(context))
                .build();
    }

    private String generateAppointmentMessage(BatchAvailabilityContext context) {
        if (context.isAvailable()) {
            return "Có thể đặt lịch hẹn.";
        }

        if (context.canBeRescheduled()) {
            return "Không đủ vaccine để đáp ứng tất cả các lịch hẹn. Lịch hẹn có thể được sắp xếp lại trong 7 ngày tới.";
        }

        return "Không đủ vaccine cho các lịch hẹn trong 7 ngày tới. Vui lòng chọn ngày khác.";
    }
}