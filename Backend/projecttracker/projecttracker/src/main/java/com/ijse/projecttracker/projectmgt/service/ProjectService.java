package com.ijse.projecttracker.projectmgt.service;

import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.projectmgt.dto.AssignBatchRequestDTO;
import com.ijse.projecttracker.projectmgt.dto.AssignSupervisorRequestDTO;
import com.ijse.projecttracker.projectmgt.dto.ProjectRequestDTO;
import com.ijse.projecttracker.projectmgt.dto.ProjectResponseDTO;

import java.util.List;

public interface ProjectService {
    ProjectResponseDTO createProject(ProjectRequestDTO request, UserDetailsImpl currentUser);


    List<ProjectResponseDTO> getProjectsForSupervisor(UserDetailsImpl currentUser);

    List<ProjectResponseDTO> getProjectsForStudent(UserDetailsImpl currentUser);

    ProjectResponseDTO getProjectById(Long id, UserDetailsImpl currentUser);

    ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request, UserDetailsImpl currentUser);

    void deleteProject(Long id, UserDetailsImpl currentUser);

}