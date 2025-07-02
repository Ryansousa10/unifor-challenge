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
