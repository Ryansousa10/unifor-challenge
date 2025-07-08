package br.com.unifor.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class UserResponseDTO {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Instant createdAt;
    private Set<String> roles; // nomes dos roles
}