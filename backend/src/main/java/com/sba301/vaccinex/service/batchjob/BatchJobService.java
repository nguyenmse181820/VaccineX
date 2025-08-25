package com.sba301.vaccinex.service.batchjob;

import com.sba301.vaccinex.dto.internal.kafka.ScheduleMailReminderDTO;
import com.sba301.vaccinex.pojo.Batch;
import com.sba301.vaccinex.pojo.Notification;
import com.sba301.vaccinex.pojo.VaccineSchedule;
import com.sba301.vaccinex.pojo.enums.VaccineScheduleStatus;
import com.sba301.vaccinex.repository.BatchRepository;
import com.sba301.vaccinex.repository.NotificationRepository;
import com.sba301.vaccinex.repository.VaccineScheduleRepository;
import com.sba301.vaccinex.thirdparty.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchJobService {

    private final BatchRepository batchRepository;
    private final VaccineScheduleRepository vaccineScheduleRepository;
    private final KafkaProducerService kafkaProducerService;
    private final NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 2 * * *")
    public void assignBatchToSchedules() {
        LocalDateTime now = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endDate = now.plusDays(14).toLocalDate().atTime(23, 59, 59);
        List<VaccineSchedule> vaccineSchedules = vaccineScheduleRepository.findByDeletedIsFalseAndStatusAndDateIsBetweenOrderByDateAsc(VaccineScheduleStatus.PLANNED, now, endDate);
        for (VaccineSchedule vaccineSchedule : vaccineSchedules) {
            List<Batch> viableBatches = batchRepository.findByVaccineIdAndExpirationBeforeAndDeletedIsFalseOrderByExpirationAsc(vaccineSchedule.getVaccine().getId(), vaccineSchedule.getDate());
            int selectedBatch = 0;
            while (selectedBatch < viableBatches.size() && vaccineScheduleRepository.countBatch(viableBatches.get(selectedBatch).getId()) >= viableBatches.get(selectedBatch).getQuantity()) {
                selectedBatch++;
            }
            if (selectedBatch == viableBatches.size()) {
                System.out.println("Not enough batches for schedule" + vaccineSchedule.getId());
            } else {
                System.out.println("Assigned batch id " + viableBatches.get(selectedBatch).getId() + " to schedule id " + vaccineSchedule.getId());
                vaccineSchedule.setBatch(viableBatches.get(selectedBatch));
                vaccineScheduleRepository.save(vaccineSchedule);
            }
        }
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void remindVaccineSchedules() {
        LocalDate threeDays = LocalDate.now().plusDays(3);
        LocalDate oneDay = LocalDate.now().plusDays(1);
        LocalDate today = LocalDate.now();
        List<VaccineSchedule> schedules = vaccineScheduleRepository
                .findByDeletedIsFalseAndStatus(VaccineScheduleStatus.PLANNED)
                .stream().filter(
                        s -> s.getDate().toLocalDate().isEqual(threeDays)
                                || s.getDate().toLocalDate().equals(oneDay)
                                || s.getDate().toLocalDate().equals(today)
                ).toList();
        for (VaccineSchedule vaccineSchedule : schedules) {
            kafkaProducerService.sendReminder(ScheduleMailReminderDTO.fromEntity(vaccineSchedule));
            Notification notification = Notification.builder()
                    .date(LocalDateTime.now())
                    .schedule(vaccineSchedule)
                    .build();
            if (vaccineSchedule.getDate().toLocalDate().isEqual(threeDays)) {
                notification.setMessage("Thông báo lịch tiêm trước 3 ngày");
            } else if (vaccineSchedule.getDate().toLocalDate().isEqual(today.plusDays(2))) {
                notification.setMessage("Thông báo lịch tiêm trước 2 ngày");
            } else if (vaccineSchedule.getDate().toLocalDate().equals(oneDay)) {
                notification.setMessage("Thông báo lịch tiêm trước 1 ngày");
            } else {
                notification.setMessage("Thông báo lịch tiêm hôm nay");
            }
            notificationRepository.save(notification);
        }
    }
}
