package br.com.unifor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CurricDiscRequestDTO {
    @NotNull
    private UUID curriculumId;
    @NotNull
    private UUID disciplineId;
    @NotNull
    private Integer ordering;
}

