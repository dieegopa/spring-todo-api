package com.dieegopa.todoapi.entities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.SecretKey;
import java.util.Date;

@Getter
@Setter
public class Jwt {
    private final Claims claims;
    private final SecretKey key;

    public Jwt(Claims claims, SecretKey key) {
        this.claims = claims;
        this.key = key;
    }

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    public String toString() {
        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }
}
