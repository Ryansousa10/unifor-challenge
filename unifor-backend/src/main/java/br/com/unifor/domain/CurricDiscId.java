package br.com.unifor.domain;

import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurricDiscId implements Serializable {
    private UUID curriculumId;
    private UUID disciplineId;
}