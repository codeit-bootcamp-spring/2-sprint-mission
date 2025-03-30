package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.LoginResponseDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final String secretKey = "yourSecretKey";

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequestDTO.getUsername());
        User user = optionalUser
                .filter(u -> u.getPassword().equals(loginRequestDTO.getPassword()))
                .orElseThrow(() -> new NoSuchElementException("Invalid username or password"));

        String token = generateToken(user);
        return new LoginResponseDTO(user.getUsername(), token);
    }

    private String generateToken(User user) {
        Date now = new Date();
        // 한시간 후 마료된다.
        Date expiryDate = new Date(now.getTime() + 1000 * 60 * 60 * 24);

        // 이 부분은 공부가 필요하다 (chatGPT 활용)
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}
