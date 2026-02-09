package com.tiagocovas.safevaultapi.controller;

import com.tiagocovas.safevaultapi.domain.Secret;
import com.tiagocovas.safevaultapi.dto.CreateSecretRequest;
import com.tiagocovas.safevaultapi.repository.SecretRepository;
import com.tiagocovas.safevaultapi.core.security.EncryptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/secrets")
public class SecretController {

    private final SecretRepository repository;
    private final EncryptionService encryptionService;

    // Injeção de Dependência via Construtor (Boa prática!)
    public SecretController(SecretRepository repository, EncryptionService encryptionService) {
        this.repository = repository;
        this.encryptionService = encryptionService;
    }

    @PostMapping
    public ResponseEntity<Void> createSecret(@Valid @RequestBody CreateSecretRequest request) {
        // 1. Encriptar o conteúdo recebido
        String encryptedContent = encryptionService.encrypt(request.content());

        // 2. Criar a entidade segura (Imutável!)
        Secret newSecret = new Secret(encryptedContent);

        // 3. Guardar na BD
        Secret savedSecret = repository.save(newSecret);

        // 4. Retornar 201 Created com o ID (Mas NÃO retornamos o segredo!)
        return ResponseEntity
                .created(URI.create("/api/secrets/" + savedSecret.getId()))
                .build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<String> getSecret(@PathVariable UUID id) {
        return repository.findById(id)
                .map(secret -> {
                    // Desencriptar antes de mostrar (apenas para teste agora)
                    String decrypted = encryptionService.decrypt(secret.getEncryptedData());
                    return ResponseEntity.ok(decrypted);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}