package com.ijse.projecttracker.projectmgt.dto;


import com.ijse.projecttracker.projectmgt.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDTO {
    @NotBlank
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private Long supervisorId;
    private Long batchId;
}


