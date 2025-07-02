package br.com.unifor.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DisciplineResponseDTO {
    private UUID id;
    private String code;
    private String name;
    private Integer credits;
    private String description;
}

