package com.ijse.projecttracker.projectmgt.dto;


import com.ijse.projecttracker.projectmgt.entity.MilestoneStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneRequestDTO {
    @NotBlank
    private String title;
    private String description;
    private LocalDate dueDate;
    private MilestoneStatus status;
}

