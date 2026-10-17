package com.dexturn.jobportal.dto;

import jakarta.validation.constraints.NotNull;

public record ApplyJobRequestDto(
        @NotNull(message = "Job Id is required")
        Long jobId,
        String coverLetter
) {
}
