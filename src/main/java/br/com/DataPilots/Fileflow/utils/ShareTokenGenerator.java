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
        if (seed == null || seed.isEmpty()) {
            throw new IllegalArgumentException("Seed não pode ser nulo ou vazio");
        }
        
        try {
            String input = seed + SALT;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash).substring(0, 32);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }
    
    public static boolean validateToken(String seed, String token) {
        if (seed == null || token == null) {
            return false;
        }
        try {
            String generatedToken = generateToken(seed);
            return generatedToken.equals(token);
        } catch (Exception e) {
            return false;
        }
    }
} 