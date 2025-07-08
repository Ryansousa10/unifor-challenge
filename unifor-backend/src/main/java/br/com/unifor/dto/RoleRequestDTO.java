package br.com.unifor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleRequestDTO {
    @NotBlank
    @Size(max = 20)
    private String name;
}

