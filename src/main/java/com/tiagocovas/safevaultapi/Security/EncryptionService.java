package com.tiagocovas.safevaultapi.Security;

public interface EncryptionService {
    String encrypt(String rawData);

    String decrypt(String encryptedData);
}