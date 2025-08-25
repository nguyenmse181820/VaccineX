package com.sba301.vaccinex.thirdparty.kafka;

import com.sba301.vaccinex.dto.internal.kafka.ScheduleMailReminderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, ScheduleMailReminderDTO> kafkaTemplate;

    public void sendReminder(ScheduleMailReminderDTO reminderDTO) {
        kafkaTemplate.send("vaccine-topic", reminderDTO);
    }

}
