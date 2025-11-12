package com.ijse.projecttracker.projectmgt.dto;


import com.ijse.projecttracker.projectmgt.entity.ProjectStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private String piFullName;
    private String piEmail;
    private String batchName;
    private List<MilestoneResponseDTO> milestones;
    private List<DocumentResponseDTO> documents;
}

