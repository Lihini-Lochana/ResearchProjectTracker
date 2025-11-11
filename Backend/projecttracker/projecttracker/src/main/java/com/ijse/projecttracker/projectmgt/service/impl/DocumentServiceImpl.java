package com.ijse.projecttracker.projectmgt.service.impl;

import com.ijse.projecttracker.auth.entity.User;
import com.ijse.projecttracker.auth.repository.UserRepository;
import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.projectmgt.dto.DocumentResponseDTO;
import com.ijse.projecttracker.projectmgt.entity.Document;
import com.ijse.projecttracker.projectmgt.entity.Project;
import com.ijse.projecttracker.projectmgt.repository.DocumentRepository;
import com.ijse.projecttracker.projectmgt.repository.ProjectRepository;
import com.ijse.projecttracker.projectmgt.service.DocumentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepo;
    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public DocumentResponseDTO uploadDocument(Long projectId, MultipartFile file, UserDetailsImpl currentUser) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        User uploader = userRepo.findById(currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String filePath = uploadDir + "/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dest = new File(filePath);
        dest.getParentFile().mkdirs();
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file", e);
        }

        Document document = Document.builder()
                .filename(file.getOriginalFilename())
                .storagePath(filePath)
                .uploadedAt(Instant.now())
                .uploadedBy(uploader)
                .project(project)
                .build();

        Document saved = documentRepo.save(document);
        return convertToDTO(saved);
    }

    @Override
    public List<DocumentResponseDTO> listProjectDocuments(Long projectId, UserDetailsImpl currentUser) {
        if (!projectRepo.existsById(projectId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");

        return documentRepo.findByProjectId(projectId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DocumentResponseDTO convertToDTO(Document document) {
        DocumentResponseDTO dto = modelMapper.map(document, DocumentResponseDTO.class);
        dto.setProjectId(document.getProject().getId());
        dto.setUploadedById(document.getUploadedBy().getId());
        return dto;
    }
}
