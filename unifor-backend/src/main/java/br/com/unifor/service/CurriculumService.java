package br.com.unifor.service;

import br.com.unifor.domain.Curriculum;
import br.com.unifor.domain.Course;
import br.com.unifor.domain.Semester;
import br.com.unifor.dto.CurriculumRequestDTO;
import br.com.unifor.dto.CurriculumResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class CurriculumService {
    // Serviço responsável pela lógica de negócio relacionada a matrizes curriculares.
    // Implementa as operações CRUD e regras específicas para a entidade Curriculum.
    //
    // Regras de negócio:
    // - Uma matriz curricular deve estar vinculada a um curso
    // - Uma matriz curricular deve estar vinculada a um semestre
    // - Não pode existir mais de uma matriz ativa para o mesmo curso/semestre
    //
    // Observações:
    // - Utiliza Panache para persistência
    // - Validações de relacionamentos são realizadas antes da persistência
    // - Transformação entre DTOs e entidades acontece nesta camada

    public List<CurriculumResponseDTO> listCurriculums() {
        return Curriculum.listAll().stream()
                .map(c -> toResponseDTO((Curriculum) c))
                .collect(Collectors.toList());
    }

    public CurriculumResponseDTO getCurriculum(UUID id) {
        Curriculum curriculum = Curriculum.findById(id);
        if (curriculum == null) {
            throw new NotFoundException("Curriculum not found: " + id);
        }
        return toResponseDTO(curriculum);
    }

    @Transactional
    public CurriculumResponseDTO createCurriculum(CurriculumRequestDTO dto) {
        Course course = Course.findById(dto.getCourseId());
        if (course == null) {
            throw new NotFoundException("Course not found: " + dto.getCourseId());
        }
        Semester semester = Semester.findById(dto.getSemesterId());
        if (semester == null) {
            throw new NotFoundException("Semester not found: " + dto.getSemesterId());
        }
        // Validação de duplicidade: não pode existir mais de uma matriz para o mesmo curso/semestre
        if (br.com.unifor.domain.Curriculum.find("course = ?1 and semester = ?2", course, semester).firstResult() != null) {
            throw new br.com.unifor.exception.DuplicateResourceException("Já existe uma matriz curricular para este curso e semestre");
        }
        Curriculum curriculum = new Curriculum();
        curriculum.setCourse(course);
        curriculum.setSemester(semester);
        curriculum.persist();
        return toResponseDTO(curriculum);
    }

    @Transactional
    public CurriculumResponseDTO updateCurriculum(UUID id, CurriculumRequestDTO dto) {
        Curriculum existing = Curriculum.findById(id);
        if (existing == null) {
            throw new NotFoundException("Curriculum not found: " + id);
        }
        Course course = Course.findById(dto.getCourseId());
        if (course == null) {
            throw new NotFoundException("Course not found: " + dto.getCourseId());
        }
        Semester semester = Semester.findById(dto.getSemesterId());
        if (semester == null) {
            throw new NotFoundException("Semester not found: " + dto.getSemesterId());
        }
        // Validação de duplicidade: não pode existir mais de uma matriz para o mesmo curso/semestre (exceto a própria)
        if (br.com.unifor.domain.Curriculum.find("course = ?1 and semester = ?2 and id <> ?3", course, semester, id).firstResult() != null) {
            throw new br.com.unifor.exception.DuplicateResourceException("Já existe uma matriz curricular para este curso e semestre");
        }
        existing.setCourse(course);
        existing.setSemester(semester);
        return toResponseDTO(existing);
    }

    @Transactional
    public void deleteCurriculum(UUID id) {
        boolean deleted = Curriculum.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Curriculum not found: " + id);
        }
    }

    private CurriculumResponseDTO toResponseDTO(Curriculum curriculum) {
        CurriculumResponseDTO dto = new CurriculumResponseDTO();
        dto.setId(curriculum.getId());
        dto.setCourseId(curriculum.getCourse() != null ? curriculum.getCourse().getId() : null);
        dto.setSemesterId(curriculum.getSemester() != null ? curriculum.getSemester().getId() : null);
        return dto;
    }
}
