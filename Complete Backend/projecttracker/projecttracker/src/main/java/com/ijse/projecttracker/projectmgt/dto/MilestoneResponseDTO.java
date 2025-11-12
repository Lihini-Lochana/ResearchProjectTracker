package com.ijse.projecttracker.projectmgt.dto;

import com.ijse.projecttracker.projectmgt.entity.MilestoneStatus;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilestoneResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private MilestoneStatus status;
    private Long projectId;
}

