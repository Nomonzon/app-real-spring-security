package com.example.apparticle.repository;

import com.example.apparticle.entity.Role;
import com.example.apparticle.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName roleName);
}
