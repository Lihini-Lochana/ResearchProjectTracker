package com.ijse.projecttracker.auth.seed;


import com.ijse.projecttracker.auth.entity.Role;
import com.ijse.projecttracker.auth.entity.UserRoleName;
import com.ijse.projecttracker.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Value("${app.seed.roles:true}")
    private boolean seedRoles;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!seedRoles) {
            System.out.println("Role seeding skipped (app.seed.roles=false).");
            return;
        }
        for (UserRoleName r : UserRoleName.values()) {
            if (!roleRepository.existsByName(r)) {
                roleRepository.save(new Role(r));
                System.out.println("Seeded role: " + r);
            }
        }
    }
}

