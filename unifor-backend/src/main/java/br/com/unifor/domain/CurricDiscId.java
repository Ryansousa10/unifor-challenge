package br.com.unifor.domain;

import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurricDiscId implements Serializable {

    // Classe que representa a chave composta da entidade CurricDisc.
    //
    // Atributos:
    // - curriculumId: identificador da matriz curricular
    // - disciplineId: identificador da disciplina
    //
    // Finalidade:
    // - Compor a chave primária da relação N:N entre matriz curricular e disciplina
    // - Garantir unicidade na associação entre matriz e disciplina
    //
    // Regras:
    // - Ambos os IDs são obrigatórios
    // - A combinação dos IDs deve ser única
    //
    // Observações:
    // - Implementa Serializable conforme requisito JPA
    // - Utilizada com @IdClass em CurricDisc

    private UUID curriculumId;
    private UUID disciplineId;
}