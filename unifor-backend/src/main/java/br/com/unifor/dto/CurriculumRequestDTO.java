package br.com.unifor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CurriculumRequestDTO {
    @NotNull
    private UUID courseId;
    @NotNull
    private UUID semesterId;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean active;
    private List<CurricDiscRequestDTO> disciplines;
}
