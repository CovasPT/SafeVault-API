package com.tiagocovas.safevaultapi.Core.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AesEncryptionService implements EncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // Bits
    private static final int IV_LENGTH = 12; // Bytes (Recomendado para GCM)

    private final SecretKey secretKey;

    // Injetamos a chave do application.properties
    // Decodificamos de Base64 para ter os bytes reais
    public AesEncryptionService(@Value("${safevault.security.key}") String base64Key) {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    @Override
    public String encrypt(String rawData) {
        try {
            // 1. Gerar um IV (Initialization Vector) aleatório ÚNICO
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            // 2. Configurar o Cipher
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

            // 3. Encriptar
            byte[] cipherText = cipher.doFinal(rawData.getBytes(StandardCharsets.UTF_8));

            // 4. Juntar IV + CipherText (Precisamos do IV para desencriptar!)
            byte[] encryptedMessage = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, encryptedMessage, 0, iv.length);
            System.arraycopy(cipherText, 0, encryptedMessage, iv.length, cipherText.length);

            // 5. Retornar tudo como Base64 para salvar na BD
            return Base64.getEncoder().encodeToString(encryptedMessage);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao encriptar dados", e);
        }
    }

    @Override
    public String decrypt(String encryptedData) {
        try {
            // 1. Decodificar do formato Base64
            byte[] decodedMessage = Base64.getDecoder().decode(encryptedData);

            // 2. Separar o IV do Texto Cifrado
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, decodedMessage, 0, IV_LENGTH);

            // 3. Configurar o Cipher para desencriptar
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            // 4. Desencriptar (ignorando os primeiros 12 bytes que são o IV)
            byte[] plainText = cipher.doFinal(decodedMessage, IV_LENGTH, decodedMessage.length - IV_LENGTH);

            return new String(plainText, StandardCharsets.UTF_8);

        } catch (Exception e) {
            // Se a chave estiver errada ou os dados corrompidos, cai aqui
            throw new RuntimeException("Erro ao desencriptar dados (Integridade violada?)", e);
        }
    }
}