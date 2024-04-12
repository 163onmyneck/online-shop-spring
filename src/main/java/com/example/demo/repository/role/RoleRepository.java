package com.example.demo.repository.role;

import com.example.demo.model.Role;
import com.example.demo.model.Role.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "SELECT r FROM Role r WHERE r.roleName = :roleName")
    Optional<Role> findByRoleName(RoleName roleName);
}
