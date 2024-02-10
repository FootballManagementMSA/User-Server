package sejong.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class TokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    private final RedisTemplate<String, String> redisTemplate;

    public TokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String createAccessToken(String studentId){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .setSubject(studentId)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }


    public String createRefreshToken(String studentId){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTokenExpiration);

        String refreshToken = Jwts.builder()
                .setSubject(studentId)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        // redis에 저장
        redisTemplate.opsForValue().set(
                studentId,
                refreshToken,
                refreshTokenExpiration,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }
}
