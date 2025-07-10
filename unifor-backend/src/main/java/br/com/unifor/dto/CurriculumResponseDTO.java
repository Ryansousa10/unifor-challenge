package br.com.unifor.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CurriculumResponseDTO {
    private UUID id;
    private UUID courseId;
    private UUID semesterId;
    private String name;
    private String description;
    private Boolean active;
    private List<CurricDiscResponseDTO> disciplines;
}
