package br.com.DataPilots.Fileflow.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class ShareTokenGenerator {
    private static final String SALT = "FileFlowShare2024";
    
    public static String generateSeed() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
    
    public static String generateToken(String seed) {
        try {
            String input = seed + SALT;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            return token.substring(0, 32); 
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }
    
    public static boolean validateToken(String seed, String token) {
        return generateToken(seed).equals(token);
    }
} 