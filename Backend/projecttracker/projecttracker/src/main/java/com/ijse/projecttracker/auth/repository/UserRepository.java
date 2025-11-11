package com.ijse.projecttracker.auth.repository;

import com.ijse.projecttracker.auth.entity.User;
import com.ijse.projecttracker.auth.entity.UserRoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") UserRoleName roleName);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.batch WHERE u.id = :id")
    Optional<User> findByIdWithBatch(@Param("id") Long id);


}
