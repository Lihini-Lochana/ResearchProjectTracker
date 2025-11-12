package com.ijse.projecttracker.projectmgt.controller;

import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.projectmgt.dto.MilestoneRequestDTO;
import com.ijse.projecttracker.projectmgt.dto.MilestoneResponseDTO;
import com.ijse.projecttracker.projectmgt.service.MilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/milestones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneService milestoneService;

    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/{projectId}")
    public MilestoneResponseDTO addMilestone(@PathVariable Long projectId,
                                             @RequestBody MilestoneRequestDTO request,
                                             @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return milestoneService.addMilestone(projectId, request, currentUser);
    }

    @GetMapping("/{projectId}")
    public List<MilestoneResponseDTO> listMilestones(@PathVariable Long projectId,
                                                     @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return milestoneService.listMilestones(projectId, currentUser);
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PutMapping("/{milestoneId}")
    public MilestoneResponseDTO updateMilestone(@PathVariable Long milestoneId,
                                                @RequestBody MilestoneRequestDTO request,
                                                @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return milestoneService.updateMilestone(milestoneId, request, currentUser);
    }

    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/{milestoneId}")
    public void deleteMilestone(@PathVariable Long milestoneId,
                                @AuthenticationPrincipal UserDetailsImpl currentUser) {
        milestoneService.deleteMilestone(milestoneId, currentUser);
    }
}
