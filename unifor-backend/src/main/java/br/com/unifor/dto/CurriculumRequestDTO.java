package br.com.unifor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CurriculumRequestDTO {
    @NotNull
    private UUID courseId;
    @NotNull
    private UUID semesterId;
}

