package com.ijse.projecttracker.projectmgt.controller;

import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.projectmgt.dto.DocumentResponseDTO;
import com.ijse.projecttracker.projectmgt.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{projectId}")
    public DocumentResponseDTO uploadDocument(@PathVariable Long projectId,
                                              @RequestParam("file") MultipartFile file,
                                              @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return documentService.uploadDocument(projectId, file, currentUser);
    }

    @GetMapping("/{projectId}")
    public List<DocumentResponseDTO> listProjectDocuments(@PathVariable Long projectId,
                                                          @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return documentService.listProjectDocuments(projectId, currentUser);
    }
}
