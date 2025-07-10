package br.com.unifor.service;

import br.com.unifor.domain.Semester;
import br.com.unifor.dto.SemesterRequestDTO;
import br.com.unifor.dto.SemesterResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class SemesterService {
    // Serviço responsável pela lógica de negócio relacionada a semestres letivos.
    // Implementa as operações CRUD e regras específicas para a entidade Semester.
    //
    // Regras de negócio:
    // - Nome do semestre deve ser único (ex: 2023.1)
    // - Data de início e fim são obrigatórias
    // - Data de início deve ser anterior à data de fim
    //
    // Observações:
    // - Utiliza Panache para persistência
    // - Validações de datas são realizadas antes da persistência
    // - Transformação entre DTOs e entidades acontece nesta camada

    public List<SemesterResponseDTO> listSemesters() {
        return Semester.listAll().stream()
                .map(s -> toResponseDTO((Semester) s))
                .collect(Collectors.toList());
    }

    public SemesterResponseDTO getSemester(UUID id) {
        Semester semester = Semester.findById(id);
        if (semester == null) {
            throw new NotFoundException("Semester not found: " + id);
        }
        return toResponseDTO(semester);
    }

    @Transactional
    public SemesterResponseDTO createSemester(SemesterRequestDTO dto) {
        // Validação de duplicidade de nome do semestre
        if (Semester.find("name", dto.getName()).firstResult() != null) {
            throw new br.com.unifor.exception.DuplicateResourceException("Nome de semestre já cadastrado: " + dto.getName());
        }
        // Validação de payload: nome e datas obrigatórias
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new br.com.unifor.exception.InvalidPayloadException("Nome do semestre é obrigatório");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new br.com.unifor.exception.InvalidPayloadException("Datas de início e fim são obrigatórias");
        }
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new br.com.unifor.exception.InvalidPayloadException("Data de início deve ser anterior à data de fim");
        }
        Semester semester = new Semester();
        semester.setName(dto.getName());
        semester.setStartDate(dto.getStartDate());
        semester.setEndDate(dto.getEndDate());
        semester.persist();
        return toResponseDTO(semester);
    }

    @Transactional
    public SemesterResponseDTO updateSemester(UUID id, SemesterRequestDTO dto) {
        Semester existing = Semester.findById(id);
        if (existing == null) {
            throw new NotFoundException("Semester not found: " + id);
        }
        // Validação de duplicidade de nome (exceto o próprio semestre)
        if (Semester.find("name = ?1 and id <> ?2", dto.getName(), id).firstResult() != null) {
            throw new br.com.unifor.exception.DuplicateResourceException("Nome de semestre já cadastrado: " + dto.getName());
        }
        // Validação de payload: nome e datas obrigatórias
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new br.com.unifor.exception.InvalidPayloadException("Nome do semestre é obrigatório");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new br.com.unifor.exception.InvalidPayloadException("Datas de início e fim são obrigatórias");
        }
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new br.com.unifor.exception.InvalidPayloadException("Data de início deve ser anterior à data de fim");
        }
        existing.setName(dto.getName());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        return toResponseDTO(existing);
    }

    @Transactional
    public void deleteSemester(UUID id) {
        boolean deleted = Semester.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Semester not found: " + id);
        }
    }

    private SemesterResponseDTO toResponseDTO(Semester semester) {
        SemesterResponseDTO dto = new SemesterResponseDTO();
        dto.setId(semester.getId());
        dto.setName(semester.getName());
        dto.setStartDate(semester.getStartDate());
        dto.setEndDate(semester.getEndDate());
        return dto;
    }
}
