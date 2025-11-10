package com.ijse.projecttracker.auth.repository;


import com.ijse.projecttracker.auth.entity.Role;
import com.ijse.projecttracker.auth.entity.UserRoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(UserRoleName name);
    boolean existsByName(UserRoleName name);
}
