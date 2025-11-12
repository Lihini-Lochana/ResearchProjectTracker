package com.ijse.projecttracker.projectmgt.repository;


import com.ijse.projecttracker.projectmgt.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

}

