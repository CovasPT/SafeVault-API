package com.tiagocovas.safevaultapi.repository;

import com.tiagocovas.safevaultapi.domain.Secret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SecretRepository extends JpaRepository<Secret, UUID> {
}