package com.sprint.mission.discodeit.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JwtBlacklist {

  private final Map<String, Date> blacklist = new ConcurrentHashMap<>();
  private final SecretKey secretKey;

  public JwtBlacklist(@Value("${jwt.secret}") String secretString) {
    this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
  }

  public void addBlacklist(String accessToken) {
    Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(accessToken)
        .getPayload();
    blacklist.put(accessToken, claims.getExpiration());
  }

  public boolean isBlacklisted(String accessToken) {
    return blacklist.containsKey(accessToken);
  }

  @Scheduled(cron = "0 0 * * * ?")
  public void cleanup() {
    Date now = new Date();
    blacklist.entrySet().removeIf(entry -> entry.getValue().before(now));
  }
}
