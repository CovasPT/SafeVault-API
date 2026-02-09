package com.tiagocovas.safevaultapi.core.security;

public interface EncryptionService {
    String encrypt(String rawData);

    String decrypt(String encryptedData);
}