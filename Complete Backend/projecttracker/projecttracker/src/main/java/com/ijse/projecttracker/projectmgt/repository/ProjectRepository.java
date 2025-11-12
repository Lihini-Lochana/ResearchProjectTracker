package com.ijse.projecttracker.projectmgt.repository;


import com.ijse.projecttracker.projectmgt.entity.Project;
import com.ijse.projecttracker.auth.entity.User;
import com.ijse.projecttracker.projectmgt.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByPi(User pi);
    List<Project> findByBatch(Batch batch);
}

