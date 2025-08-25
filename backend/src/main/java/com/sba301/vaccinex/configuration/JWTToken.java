package com.sba301.vaccinex.configuration;

import com.sba301.vaccinex.exception.AuthenticationException;
import com.sba301.vaccinex.pojo.enums.EnumTokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTToken {

    @Value("${jwt.expiration}")
    private int JWT_EXPIRATION;

    @Value("${jwt.refresh.expiration}")
    private int JWT_REFRESH_EXPIRATION;

    @Value("${jwt.secret}")
    private String sceretString;

    @Value("${jwt.refresh.secret}")
    private String refreshSecretString;

    @Value("${jwt.algorithms}")
    private String algorithm;

    private SecretKey getSecretKey(EnumTokenType type) {
        byte[] keyBytes;
        SecretKey SCRET_KEY;
        if(type == EnumTokenType.TOKEN) {
            keyBytes = Base64.getDecoder().decode(sceretString.getBytes(StandardCharsets.UTF_8));
        } else {
            keyBytes = Base64.getDecoder().decode(refreshSecretString.getBytes(StandardCharsets.UTF_8));
        }
        SCRET_KEY = new SecretKeySpec(keyBytes, algorithm);
        return SCRET_KEY;
    }

    public String generatedToken(CustomAccountDetail customAccountDetail) {
        Date date = new Date(System.currentTimeMillis());

        Date exp = new Date(System.currentTimeMillis() + JWT_EXPIRATION);

        return Jwts.builder()
                .subject(customAccountDetail.getUsername())
                .issuedAt(date)
                .expiration(exp)
                .claim("id", customAccountDetail.getId())
                .claim("firstName", customAccountDetail.getFirstName())
                .claim("lastName", customAccountDetail.getLastName())
                .claim("role", customAccountDetail.getGrantedAuthorities())
                .signWith(getSecretKey(EnumTokenType.TOKEN))
                .compact();
    }

    public String generatedRefreshToken(CustomAccountDetail customAccountDetail) {
        Date date = new Date(System.currentTimeMillis());

        Date exp = new Date(System.currentTimeMillis() + JWT_REFRESH_EXPIRATION);

        return Jwts.builder()
                .subject(customAccountDetail.getUsername())
                .issuedAt(date)
                .expiration(exp)
                .claim("id", customAccountDetail.getId())
                .claim("firstName", customAccountDetail.getFirstName())
                .claim("lastName", customAccountDetail.getLastName())
                .claim("role", customAccountDetail.getGrantedAuthorities())
                .signWith(getSecretKey(EnumTokenType.REFRESH_TOKEN))
                .compact();
    }

    public String getEmailFromJwt(String token, EnumTokenType type) {
        return getClaims(token, Claims::getSubject, type);
    }

    private <T> T getClaims(String token, Function<Claims, T> claimsTFunction, EnumTokenType type) {
        try {
            return claimsTFunction.apply(
                    Jwts.parser().verifyWith(getSecretKey(type)).build().parseSignedClaims(token).getPayload());
        } catch (Exception e) {
            throw new AuthenticationException("Bạn không có quyền truy cập token này.");
        }
    }

    public boolean validate(String token, EnumTokenType type) {
        try {
            return getEmailFromJwt(token, type) != null && !isExpired(token, type);
        } catch (Exception e) {
            throw new AuthenticationException("Bạn không có quyền truy cập token này");
        }
    }

    public boolean isExpired(String token, EnumTokenType type) {
        try {
            return getClaims(token, Claims::getExpiration, type).before(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            throw new AuthenticationException("Bạn không có quyền truy cập token này");
        }
    }

}
