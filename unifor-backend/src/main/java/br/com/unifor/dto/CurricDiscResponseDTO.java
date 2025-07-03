package br.com.unifor.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CurricDiscResponseDTO {
    private UUID curriculumId;
    private UUID disciplineId;
    private Integer ordering;
}

