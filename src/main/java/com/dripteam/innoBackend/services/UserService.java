package com.dripteam.innoBackend.services;


import com.dripteam.innoBackend.entities.UserEntity;
import com.dripteam.innoBackend.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    @NonNull
    private UserRepository repository;

    @Value("${jwt.secret}")
    private String secretKey;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();


    public Optional<UserEntity> findUserById(UUID id) {
        return repository.findById(id);
    }

    public Optional<UserEntity> findUserByEmail(String email) {
        return repository.findUserEntityByEmail(email);
    }

    public UserEntity addUser(UserEntity user) {
        repository.save(user);
        return user;
    }

    public String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }

    public String fromDataToJwt(String uuid) {
        return Jwts.builder()
                .setSubject(uuid).setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }

    public UUID fromJwtToId(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(jwt)
                .getBody();
        return UUID.fromString(claims.getSubject());
    }

    public Date fromJwtToTimeStamp(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getIssuedAt();
    }


}
