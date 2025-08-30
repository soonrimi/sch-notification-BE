package com.schnofiticationbe.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.schnofiticationbe.Utils.RsaKeyLoader;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.springframework.stereotype.Component;

import java.util.Date;



@Component
public class JwtProvider {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtProvider() {
        try {
            this.privateKey = RsaKeyLoader.loadPrivateKey("src/main/resources/keys/private.pem");
            this.publicKey = RsaKeyLoader.loadPublicKey("src/main/resources/keys/public.pem");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load RSA keys", e);
        }
    }

    public String createToken(String username, String role) {
            return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public boolean validateToken(String token) {
            try {
                Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
                return true;
            } catch (JwtException e) {
                // 로그를 남기면 디버깅에 도움
                return false;
            }
    }

    public String getUsername(String token) {
            Claims claims = (Claims) Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return claims.getSubject();
    }

    public String getRole(String token) {
            Claims claims = (Claims) Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return (String) claims.get("role");
    }
}