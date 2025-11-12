package com.ijse.projecttracker.projectmgt.controller;

import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.projectmgt.dto.*;
import com.ijse.projecttracker.projectmgt.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/projects")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProjectResponseDTO createProject(@RequestBody ProjectRequestDTO request,
                                            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return projectService.createProject(request, currentUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ProjectResponseDTO> getAllProjects(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        return projectService.getAllProjects(currentUser);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProjectResponseDTO updateProject(@PathVariable Long id,
                                            @RequestBody ProjectRequestDTO request,
                                            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return projectService.updateProject(id, request, currentUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id,
                              @AuthenticationPrincipal UserDetailsImpl currentUser) {
        projectService.deleteProject(id, currentUser);
    }

    @GetMapping("/{id}")
    public ProjectResponseDTO getProjectById(@PathVariable Long id,
                                             @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return projectService.getProjectById(id, currentUser);
    }

    @PreAuthorize("hasRole('PI')")
    @GetMapping("/pi")
    public List<ProjectResponseDTO> getProjectsForPi(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        return projectService.getProjectsForPi(currentUser);
    }

    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/member")
    public List<ProjectResponseDTO> getProjectsForMember(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        return projectService.getProjectsForMember(currentUser);
    }


}
