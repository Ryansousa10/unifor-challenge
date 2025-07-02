package br.com.unifor.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleResponseDTO {
    private UUID id;
    private String name;
}

