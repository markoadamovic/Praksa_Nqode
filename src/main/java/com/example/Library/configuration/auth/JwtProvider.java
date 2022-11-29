package com.example.Library.configuration.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static final String BEARER_HEADER = "Bearer ";

    @Value("Library")
    private String APP_NAME;

    @Value("secret")
    public String SECRET;

    @Value("1")
    private Long EXPIRES_IN;

    @Value("Authorization")
    private String AUTH_HEADER;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String getEmailFromToken(String token) {
        String email;
        try {
            final Claims claims = getAllClaimsFromToken(token);
            email = claims.getSubject();
        }catch (Exception e) {
            email = null;
        }
        return email;
    }

    public String removeBearerFromToken(String token) {
        return token.replace(BEARER_HEADER, "");
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(email)
                .setExpiration(generateExpiratonDate())
                .claim("roles", role)
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try{
            if (!isNull(token) && token.startsWith(BEARER_HEADER)) {
                token = removeBearerFromToken(token);
            }
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            claims = null;
        }
        return claims;
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = this.getIssuedAtDateFromToken(token);
        return expiration.before(new Date());

    }

    private Date getIssuedAtDateFromToken(String token) {
        Date issuedAt;
        try{
            final Claims claims = this.getAllClaimsFromToken(token);
            issuedAt = claims.getIssuedAt();
        }catch (Exception e){
            issuedAt = null;
        }
        return issuedAt;
    }

    public String getToken(HttpServletRequest request){
        String authHeader = getAuthHeaderFromHeader(request);
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }

    private String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

    private Date generateExpiratonDate() {

        Date expiration = Date.from(LocalDateTime.now().plusMinutes(EXPIRES_IN)
                .atZone(ZoneId.systemDefault()).toInstant());
        return expiration;
    }

}

