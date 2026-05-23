package com.giuli.api_financeira.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "minha-chave-super-secreta-com-mais-de-32-bytes";

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String gerarToken(Long userId, Long empresaId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("empresaId", empresaId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extrairClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getEmpresaId(String token) {
        return extrairClaims(token).get("empresaId", Long.class);
    }

    public Long getUserId(String token) {
        String subject = extrairClaims(token).getSubject();
        return Long.parseLong(subject);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extrairClaims(token);

            Date expiration = claims.getExpiration();

            return expiration != null && expiration.after(new Date());

        } catch (Exception e) {
            return false;
        }
    }

}
