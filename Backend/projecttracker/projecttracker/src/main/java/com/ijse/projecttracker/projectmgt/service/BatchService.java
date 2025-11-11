package com.ijse.projecttracker.projectmgt.service;

import com.ijse.projecttracker.auth.repository.UserRepository;
import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import com.ijse.projecttracker.projectmgt.entity.Batch;
import com.ijse.projecttracker.projectmgt.repository.BatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BatchService {

    private final BatchRepository batchRepository;
    private final UserRepository userRepository;

    public BatchService(BatchRepository batchRepository, UserRepository userRepository) {
        this.batchRepository = batchRepository;
        this.userRepository = userRepository;
    }

    public List<Batch> getAllBatches(UserDetailsImpl currentUser) {
        return batchRepository.findAll();
    }

    public Batch getBatchById(Long id, UserDetailsImpl currentUser) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
    }

    public Batch createBatch(Batch batch, UserDetailsImpl currentUser) {
        return batchRepository.save(batch);
    }

    public Batch updateBatch(Long id, Batch batchDetails, UserDetailsImpl currentUser) {
        Batch batch = getBatchById(id, currentUser);
        batch.setName(batchDetails.getName());
        return batchRepository.save(batch);
    }

    public void deleteBatch(Long id, UserDetailsImpl currentUser) {
        batchRepository.deleteById(id);
    }
}

