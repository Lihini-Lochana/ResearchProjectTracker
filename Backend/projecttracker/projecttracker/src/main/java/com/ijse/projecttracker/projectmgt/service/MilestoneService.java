package com.ijse.projecttracker.projectmgt.service;

import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.projectmgt.dto.MilestoneRequestDTO;
import com.ijse.projecttracker.projectmgt.dto.MilestoneResponseDTO;

import java.util.List;

public interface MilestoneService {

    MilestoneResponseDTO addMilestone(Long projectId, MilestoneRequestDTO req, UserDetailsImpl currentUser);

    List<MilestoneResponseDTO> listMilestones(Long projectId, UserDetailsImpl currentUser);

    MilestoneResponseDTO updateMilestone(Long milestoneId, MilestoneRequestDTO req, UserDetailsImpl currentUser);

    void deleteMilestone(Long milestoneId, UserDetailsImpl currentUser);
}
