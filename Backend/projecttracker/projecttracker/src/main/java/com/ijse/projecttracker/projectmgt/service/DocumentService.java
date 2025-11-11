package com.ijse.projecttracker.projectmgt.service;

import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.projectmgt.dto.DocumentResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    DocumentResponseDTO uploadDocument(Long projectId, MultipartFile file, UserDetailsImpl currentUser);

    List<DocumentResponseDTO> listProjectDocuments(Long projectId, UserDetailsImpl currentUser);
}
