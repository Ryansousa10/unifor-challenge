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

