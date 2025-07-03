package br.com.unifor.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CurriculumResponseDTO {
    private UUID id;
    private UUID courseId;
    private UUID semesterId;
}

