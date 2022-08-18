package com.example.todo.security;

import com.example.todo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "Q4NSl604sgyHJj1qwEkR3ycUeR4uUAt7WJraD7EN3O9DVM4yyYuHxMEbSF4XXyYJkal13eqgB0F7Bq4H";
    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // jwt라이브러리를 사용해 jwt토큰을 생성한다
    public String create(UserEntity userEntity){
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setSubject(userEntity.getId())
                .setIssuer("demo app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    // 토큰을 디코딩 및 파싱하고 토큰의 위조 여부를 확인한다.
    /*
    ParseClaimsJws가 base64로 디코딩 및 파싱
    헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명한 후 token 의 서명과 비교
    위조되지 않았다면 페이로드(claims) 리턴, 위조라면 예외를 날린다
    그 중 userId가 필요하므로 getBody를 사용한다.
    */
    public String validateAndGetUserId(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
