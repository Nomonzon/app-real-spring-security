package com.example.apparticle.repository;

import com.example.apparticle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);

    Optional<User> findByEmail(String email);

}
