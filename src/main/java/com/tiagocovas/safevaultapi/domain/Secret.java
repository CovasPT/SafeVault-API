package com.tiagocovas.safevaultapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "secrets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Required by JPA for reflection
public class Secret {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String encryptedData;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Public constructor for creating new secrets.
     * This enforces immutability from the outside as there are no setters.
     * 
     * @param encryptedData The encrypted content of the secret.
     */
    public Secret(String encryptedData) {
        if (encryptedData == null || encryptedData.isBlank()) {
            throw new IllegalArgumentException("Encrypted data cannot be empty");
        }
        this.encryptedData = encryptedData;
        this.createdAt = LocalDateTime.now();
    }
}
