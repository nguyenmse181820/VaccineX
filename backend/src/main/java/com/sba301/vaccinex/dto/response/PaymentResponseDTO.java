package com.sba301.vaccinex.dto.response;

import com.sba301.vaccinex.pojo.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PaymentResponseDTO {
    Integer id;
    PaymentMethod paymentMethod;
    LocalDateTime date;
    Double amount;
}
