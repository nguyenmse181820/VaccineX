package com.sba301.vaccinex.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReactionCreateResponse(
    Integer id, String reaction, String reportedBy, LocalDateTime date
) {
}
