package br.com.unifor.service;

import br.com.unifor.domain.Discipline;
import br.com.unifor.dto.DisciplineRequestDTO;
import br.com.unifor.dto.DisciplineResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class DisciplineService {
    // Serviço responsável pela lógica de negócio relacionada a disciplinas.
    // Implementa as operações CRUD e regras específicas para a entidade Discipline.
    //
    // Regras de negócio:
    // - Código da disciplina deve ser único no sistema
    // - Nome e carga horária são obrigatórios
    // - Uma disciplina pode ter pré-requisitos
    //
    // Observações:
    // - Utiliza Panache para persistência
    // - Validações de unicidade são realizadas antes da persistência
    // - Transformação entre DTOs e entidades acontece nesta camada

    public List<DisciplineResponseDTO> listDisciplines() {
        return Discipline.listAll().stream()
                .map(d -> toResponseDTO((Discipline) d))
                .collect(Collectors.toList());
    }

    public DisciplineResponseDTO getDiscipline(UUID id) {
        Discipline discipline = Discipline.findById(id);
        if (discipline == null) {
            throw new NotFoundException("Discipline not found: " + id);
        }
        return toResponseDTO(discipline);
    }

    @Transactional
    public DisciplineResponseDTO createDiscipline(DisciplineRequestDTO dto) {
        // Validação de duplicidade de código da disciplina
        if (Discipline.find("code", dto.getCode()).firstResult() != null) {
            throw new br.com.unifor.exception.DuplicateResourceException("Código de disciplina já cadastrado: " + dto.getCode());
        }
        // Validação de payload: nome e créditos obrigatórios
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new br.com.unifor.exception.InvalidPayloadException("Nome da disciplina é obrigatório");
        }
        if (dto.getCredits() == null || dto.getCredits() <= 0) {
            throw new br.com.unifor.exception.InvalidPayloadException("Créditos da disciplina devem ser positivos");
        }
        Discipline discipline = new Discipline();
        discipline.setCode(dto.getCode());
        discipline.setName(dto.getName());
        discipline.setCredits(dto.getCredits());
        discipline.setDescription(dto.getDescription());
        discipline.persist();
        return toResponseDTO(discipline);
    }

    @Transactional
    public DisciplineResponseDTO updateDiscipline(UUID id, DisciplineRequestDTO dto) {
        Discipline existing = Discipline.findById(id);
        if (existing == null) {
            throw new NotFoundException("Discipline not found: " + id);
        }
        // Validação de duplicidade de código (exceto a própria disciplina)
        if (Discipline.find("code = ?1 and id <> ?2", dto.getCode(), id).firstResult() != null) {
            throw new br.com.unifor.exception.DuplicateResourceException("Código de disciplina já cadastrado: " + dto.getCode());
        }
        // Validação de payload: nome e créditos obrigatórios
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new br.com.unifor.exception.InvalidPayloadException("Nome da disciplina é obrigatório");
        }
        if (dto.getCredits() == null || dto.getCredits() <= 0) {
            throw new br.com.unifor.exception.InvalidPayloadException("Créditos da disciplina devem ser positivos");
        }
        existing.setCode(dto.getCode());
        existing.setName(dto.getName());
        existing.setCredits(dto.getCredits());
        existing.setDescription(dto.getDescription());
        return toResponseDTO(existing);
    }

    @Transactional
    public void deleteDiscipline(UUID id) {
        boolean deleted = Discipline.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Discipline not found: " + id);
        }
    }

    private DisciplineResponseDTO toResponseDTO(Discipline discipline) {
        DisciplineResponseDTO dto = new DisciplineResponseDTO();
        dto.setId(discipline.getId());
        dto.setCode(discipline.getCode());
        dto.setName(discipline.getName());
        dto.setCredits(discipline.getCredits());
        dto.setDescription(discipline.getDescription());
        return dto;
    }
}
