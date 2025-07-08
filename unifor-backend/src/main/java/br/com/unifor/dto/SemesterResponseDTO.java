package br.com.unifor.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class SemesterResponseDTO {
    private UUID id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}

