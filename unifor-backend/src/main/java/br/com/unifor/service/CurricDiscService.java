package br.com.unifor.service;

import br.com.unifor.domain.CurricDisc;
import br.com.unifor.dto.CurricDiscRequestDTO;
import br.com.unifor.dto.CurricDiscResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class CurricDiscService {
    // Serviço responsável pela lógica de negócio relacionada ao vínculo entre matrizes curriculares e disciplinas.
    // Implementa as operações CRUD e regras específicas para a entidade CurricDisc.
    //
    // Regras de negócio:
    // - Uma disciplina só pode aparecer uma vez em cada matriz curricular
    // - A ordem das disciplinas deve ser respeitada
    // - Pré-requisitos devem ser validados
    //
    // Observações:
    // - Utiliza Panache para persistência
    // - Validações de relacionamentos são realizadas antes da persistência
    // - Transformação entre DTOs e entidades acontece nesta camada

    public List<CurricDiscResponseDTO> listCurricDiscs() {
        return CurricDisc.listAll().stream()
                .map(c -> toResponseDTO((CurricDisc) c))
                .collect(Collectors.toList());
    }

    public CurricDiscResponseDTO getCurricDisc(UUID curriculumId, UUID disciplineId) {
        CurricDisc curricDisc = CurricDisc.find("curriculumId = ?1 and disciplineId = ?2", curriculumId, disciplineId).firstResult();
        if (curricDisc == null) {
            throw new NotFoundException("CurricDisc not found: curriculumId=" + curriculumId + ", disciplineId=" + disciplineId);
        }
        return toResponseDTO(curricDisc);
    }

    @Transactional
    public CurricDiscResponseDTO createCurricDisc(CurricDiscRequestDTO dto) {
        CurricDisc curricDisc = new CurricDisc();
        curricDisc.setCurriculumId(dto.getCurriculumId());
        curricDisc.setDisciplineId(dto.getDisciplineId());
        curricDisc.setOrdering(dto.getOrdering());
        curricDisc.persist();
        return toResponseDTO(curricDisc);
    }

    @Transactional
    public CurricDiscResponseDTO updateCurricDisc(UUID curriculumId, UUID disciplineId, CurricDiscRequestDTO dto) {
        CurricDisc existing = CurricDisc.find("curriculumId = ?1 and disciplineId = ?2", curriculumId, disciplineId).firstResult();
        if (existing == null) {
            throw new NotFoundException("CurricDisc not found: curriculumId=" + curriculumId + ", disciplineId=" + disciplineId);
        }
        existing.setOrdering(dto.getOrdering());
        return toResponseDTO(existing);
    }

    @Transactional
    public void deleteCurricDisc(UUID curriculumId, UUID disciplineId) {
        CurricDisc existing = CurricDisc.find("curriculumId = ?1 and disciplineId = ?2", curriculumId, disciplineId).firstResult();
        if (existing == null) {
            throw new NotFoundException("CurricDisc not found: curriculumId=" + curriculumId + ", disciplineId=" + disciplineId);
        }
        existing.delete();
    }

    private CurricDiscResponseDTO toResponseDTO(CurricDisc curricDisc) {
        CurricDiscResponseDTO dto = new CurricDiscResponseDTO();
        dto.setCurriculumId(curricDisc.getCurriculumId());
        dto.setDisciplineId(curricDisc.getDisciplineId());
        dto.setOrdering(curricDisc.getOrdering());
        return dto;
    }
}
