package com.flower.shop.cphpetalstudio;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

public class JwtSecretKeyGenerator {
    public static void main(String[] args) {
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        String base64EncodedKey = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println(base64EncodedKey);
    }
}