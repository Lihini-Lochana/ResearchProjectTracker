package com.ijse.projecttracker.projectmgt.service.impl;

import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.projectmgt.dto.MilestoneRequestDTO;
import com.ijse.projecttracker.projectmgt.dto.MilestoneResponseDTO;
import com.ijse.projecttracker.projectmgt.entity.Milestone;
import com.ijse.projecttracker.projectmgt.entity.Project;
import com.ijse.projecttracker.projectmgt.repository.MilestoneRepository;
import com.ijse.projecttracker.projectmgt.repository.ProjectRepository;
import com.ijse.projecttracker.projectmgt.service.MilestoneService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MilestoneServiceImpl implements MilestoneService {

    private final MilestoneRepository milestoneRepo;
    private final ProjectRepository projectRepo;
    private final ModelMapper modelMapper;

    @Override
    public MilestoneResponseDTO addMilestone(Long projectId, MilestoneRequestDTO req, UserDetailsImpl currentUser) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        Milestone milestone = modelMapper.map(req, Milestone.class);
        milestone.setProject(project);

        Milestone saved = milestoneRepo.save(milestone);
        return convertToDTO(saved);
    }

    @Override
    public List<MilestoneResponseDTO> listMilestones(Long projectId, UserDetailsImpl currentUser) {
        if (!projectRepo.existsById(projectId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");

        return milestoneRepo.findByProjectId(projectId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MilestoneResponseDTO updateMilestone(Long milestoneId, MilestoneRequestDTO req, UserDetailsImpl currentUser) {
        Milestone milestone = milestoneRepo.findById(milestoneId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Milestone not found"));

        modelMapper.map(req, milestone);
        return convertToDTO(milestoneRepo.save(milestone));
    }

    @Override
    public void deleteMilestone(Long milestoneId, UserDetailsImpl currentUser) {
        Milestone milestone = milestoneRepo.findById(milestoneId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Milestone not found"));

        milestoneRepo.delete(milestone);
    }

    private MilestoneResponseDTO convertToDTO(Milestone milestone) {
        MilestoneResponseDTO dto = modelMapper.map(milestone, MilestoneResponseDTO.class);
        dto.setProjectId(milestone.getProject().getId());
        return dto;
    }
}
