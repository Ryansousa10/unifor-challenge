package br.com.unifor.service;

import br.com.unifor.domain.Course;
import br.com.unifor.dto.CourseRequestDTO;
import br.com.unifor.dto.CourseResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class CourseService {
    public List<CourseResponseDTO> listCourses() {
        return Course.listAll().stream()
                .map(c -> toResponseDTO((Course) c))
                .collect(Collectors.toList());
    }

    public CourseResponseDTO getCourse(UUID id) {
        Course course = Course.findById(id);
        if (course == null) {
            throw new NotFoundException("Course not found: " + id);
        }
        return toResponseDTO(course);
    }

    @Transactional
    public CourseResponseDTO createCourse(CourseRequestDTO dto) {
        Course course = new Course();
        course.setCode(dto.getCode());
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.persist();
        return toResponseDTO(course);
    }

    @Transactional
    public CourseResponseDTO updateCourse(UUID id, CourseRequestDTO dto) {
        Course existing = Course.findById(id);
        if (existing == null) {
            throw new NotFoundException("Course not found: " + id);
        }
        existing.setCode(dto.getCode());
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        return toResponseDTO(existing);
    }

    @Transactional
    public void deleteCourse(UUID id) {
        boolean deleted = Course.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Course not found: " + id);
        }
    }

    private CourseResponseDTO toResponseDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setCode(course.getCode());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        return dto;
    }
}

