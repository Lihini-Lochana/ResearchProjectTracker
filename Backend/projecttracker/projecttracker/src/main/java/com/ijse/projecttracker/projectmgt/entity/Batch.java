package com.ijse.projecttracker.projectmgt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ijse.projecttracker.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> members;

}
