package com.sba301.vaccinex.dto.request;

import lombok.Builder;

@Builder
public record ReactionCreateRequest(
        String reaction, String reportedBy
) {
}
