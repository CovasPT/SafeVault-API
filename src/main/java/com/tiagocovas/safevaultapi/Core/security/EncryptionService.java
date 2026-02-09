package com.tiagocovas.safevaultapi.Core.Security;

public interface EncryptionService {
    String encrypt(String rawData);

    String decrypt(String encryptedData);
}