package br.com.unifor.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CourseResponseDTO {
    private UUID id;
    private String code;
    private String name;
    private String description;
}

