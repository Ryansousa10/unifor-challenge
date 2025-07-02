package br.com.unifor.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Set<UUID> roles;
}
