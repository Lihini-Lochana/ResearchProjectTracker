package com.ijse.projecttracker.projectmgt.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponseDTO {
    private Long id;
    private String filename;
    private String storagePath;
    private Instant uploadedAt;
    private Long uploadedById;
    private Long projectId;
}

