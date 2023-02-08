package com.rs.elasticsearchservice.util.jwt;


import com.rs.elasticsearchservice.model.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.token.validity}")
    public Long tokenValidity;

    @Value("${jwt.signing.key}")
    public String signingKey;

    @Value("${spring.application.name}")
    public String appName;


    private final SecureRandom secureRandom = new SecureRandom();

    public static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    public String extractUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractJWTSecurity(String token) {
        var claims = this.getAllClaimsFromToken(token);
        return claims.get("jwt-security", String.class);
    }

    public Date extractExpirationDateFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final var claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final var expiration = extractExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Authentication authentication, String jwtSecurity) {
        String jwtSecurityHash = this.generateJWTSecurityHash(jwtSecurity);
        var user = (Employee) authentication.getPrincipal();


        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer(appName)
                .setSubject(authentication.getName())
                .claim("jwt-security", jwtSecurityHash)
                .claim("roles", user.getAuthority().getName())
                .claim("userId", user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(signatureAlgorithm, signingKey)
                .compact();
    }

    public String generateJWTSecurity() {
        // Generisanje random string-a koji ce predstavljati fingerprint za korisnika
        byte[] randomFgp = new byte[50];
        this.secureRandom.nextBytes(randomFgp);
        return DatatypeConverter.printHexBinary(randomFgp);
    }

    private String generateJWTSecurityHash(String jwtSecurity) {
        // Generisanje hash-a za fingerprint koji stavljamo u token (sprecavamo XSS da procita fingerprint i sam postavi ocekivani cookie)
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] userFingerprintDigest = digest.digest(jwtSecurity.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(userFingerprintDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }


    public boolean checkAlgHeaderParam(String token) {
        var algorithm = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getHeader()
                .getAlgorithm();
        return algorithm.equals(signatureAlgorithm.getValue());
    }

    public Boolean validateToken(String token, UserDetails userDetails, String cookieValue) {
        final String username = extractUsernameFromToken(token);
        String jwtSecurityHash = this.generateJWTSecurityHash(cookieValue);
        String jwtSecurity = this.extractJWTSecurity(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)) && jwtSecurity.equals(jwtSecurityHash);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
