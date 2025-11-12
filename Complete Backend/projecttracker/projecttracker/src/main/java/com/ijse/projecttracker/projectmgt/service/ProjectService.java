package com.ijse.projecttracker.projectmgt.service;

import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.projectmgt.dto.ProjectRequestDTO;
import com.ijse.projecttracker.projectmgt.dto.ProjectResponseDTO;

import java.util.List;

public interface ProjectService {
    ProjectResponseDTO createProject(ProjectRequestDTO request, UserDetailsImpl currentUser);

    List<ProjectResponseDTO> getAllProjects(UserDetailsImpl currentUser);

    List<ProjectResponseDTO> getProjectsForPi(UserDetailsImpl currentUser);

    List<ProjectResponseDTO> getProjectsForMember(UserDetailsImpl currentUser);

    ProjectResponseDTO getProjectById(Long id, UserDetailsImpl currentUser);

    ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request, UserDetailsImpl currentUser);

    void deleteProject(Long id, UserDetailsImpl currentUser);

}