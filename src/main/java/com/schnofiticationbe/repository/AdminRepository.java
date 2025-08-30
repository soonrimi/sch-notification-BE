package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUserId(String userId);
    boolean existsByUserId(String userId);

}