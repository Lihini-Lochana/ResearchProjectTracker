package com.ijse.projecttracker.projectmgt.service.impl;

import com.ijse.projecttracker.auth.entity.User;
import com.ijse.projecttracker.auth.entity.UserRoleName;
import com.ijse.projecttracker.auth.repository.UserRepository;
import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.projectmgt.dto.*;
import com.ijse.projecttracker.projectmgt.entity.Batch;
import com.ijse.projecttracker.projectmgt.entity.Project;
import com.ijse.projecttracker.projectmgt.entity.ProjectStatus;
import com.ijse.projecttracker.projectmgt.repository.BatchRepository;
import com.ijse.projecttracker.projectmgt.repository.ProjectRepository;
import com.ijse.projecttracker.projectmgt.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;
    private final BatchRepository batchRepo;

    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO request, UserDetailsImpl currentUser) {
        User user = userRepo.findById(currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setStatus(request.getStatus() != null ? request.getStatus() : ProjectStatus.PLANNED);
        project.setCreatedBy(user);

        if (request.getSupervisorId() != null) {
            User supervisor = userRepo.findById(request.getSupervisorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supervisor not found"));
            project.setSupervisor(supervisor);
        }

        if (request.getBatchId() != null) {
            Batch batch = batchRepo.findById(request.getBatchId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch not found"));
            project.setBatch(batch);
        }

        Project saved = projectRepo.save(project);
        return convertToDTO(saved);
    }

    @Override
    public List<ProjectResponseDTO> getProjectsForSupervisor(UserDetailsImpl currentUser) {
        User supervisor = userRepo.findById(currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supervisor not found"));

        return projectRepo.findBySupervisor(supervisor)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectResponseDTO> getProjectsForStudent(UserDetailsImpl currentUser) {
        User user = userRepo.findByIdWithBatch(currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        boolean isStudent = user.getRoles().stream()
                .anyMatch(r -> r.getName() == UserRoleName.ROLE_STUDENT);

        if (!isStudent) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only students can access their projects");
        }

        if (user.getBatch() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student not assigned to any batch");
        }

        return projectRepo.findByBatch(user.getBatch())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    @Override
    public ProjectResponseDTO getProjectById(Long id, UserDetailsImpl currentUser) {
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return convertToDTO(project);
    }

    @Override
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request, UserDetailsImpl currentUser) {
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (request.getTitle() != null) project.setTitle(request.getTitle());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getStartDate() != null) project.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) project.setEndDate(request.getEndDate());
        if (request.getStatus() != null) project.setStatus(request.getStatus());

        if (request.getSupervisorId() != null) {
            User supervisor = userRepo.findById(request.getSupervisorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supervisor not found"));
            project.setSupervisor(supervisor);
        }

        if (request.getBatchId() != null) {
            Batch batch = batchRepo.findById(request.getBatchId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch not found"));
            project.setBatch(batch);
        }

        return convertToDTO(projectRepo.save(project));
    }

    @Override
    public void deleteProject(Long id, UserDetailsImpl currentUser) {
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        projectRepo.delete(project);
    }

    private ProjectResponseDTO convertToDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setStatus(project.getStatus());

        if (project.getSupervisor() != null) {
            dto.setSupervisorFullName(project.getSupervisor().getFullName());
            dto.setSupervisorEmail(project.getSupervisor().getEmail());
        }

        if (project.getBatch() != null) {
            dto.setBatchName(project.getBatch().getName());
        }

        dto.setMilestones(project.getMilestones() != null
                ? project.getMilestones().stream()
                .map(m -> MilestoneResponseDTO.builder()
                        .id(m.getId())
                        .title(m.getTitle())
                        .description(m.getDescription())
                        .dueDate(m.getDueDate())
                        .build())
                .collect(Collectors.toList())
                : List.of());

        dto.setDocuments(project.getDocuments() != null
                ? project.getDocuments().stream()
                .map(d -> DocumentResponseDTO.builder()
                        .id(d.getId())
                        .filename(d.getFilename())
                        .storagePath(d.getStoragePath())
                        .uploadedAt(d.getUploadedAt())
                        .uploadedById(d.getUploadedBy() != null ? d.getUploadedBy().getId() : null)
                        .projectId(d.getProject() != null ? d.getProject().getId() : null)
                        .build())
                .collect(Collectors.toList())
                : List.of());

        return dto;
    }
}
