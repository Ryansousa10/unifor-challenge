package br.com.unifor.domain;

import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurricDiscId implements Serializable {

    // Classe auxiliar para compor a chave primária da entidade CurricDisc (relação N:N entre matriz curricular e disciplina).
    // Necessária para o uso de chave composta com JPA (@IdClass).
    // Implementa Serializable para garantir compatibilidade com o JPA.
    // Os campos curriculumId e disciplineId representam as chaves das entidades associadas.
    //
    // Para mais detalhes sobre as decisões, consulte o README.

    private UUID curriculumId;
    private UUID disciplineId;
}