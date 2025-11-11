package com.ijse.projecttracker.projectmgt.controller;

import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.projectmgt.entity.Batch;
import com.ijse.projecttracker.projectmgt.service.BatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/batches")
@CrossOrigin(origins = "*")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping
    public ResponseEntity<List<Batch>> getAllBatches(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        return ResponseEntity.ok(batchService.getAllBatches(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Batch> getBatchById(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return ResponseEntity.ok(batchService.getBatchById(id, currentUser));
    }

    @PostMapping
    public ResponseEntity<Batch> createBatch(@RequestBody Batch batch,
                                             @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return ResponseEntity.ok(batchService.createBatch(batch, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Batch> updateBatch(@PathVariable Long id,
                                             @RequestBody Batch batch,
                                             @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return ResponseEntity.ok(batchService.updateBatch(id, batch, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id,
                                            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        batchService.deleteBatch(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}