package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.create.ServerCreateRequestDTO;
import com.sprint.mission.discodeit.dto.update.UpdateServerRequestDTO;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.Valid.InvalidTokenException;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.repository.ServerRepository;
import com.sprint.mission.discodeit.repository.TokenStore;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ServerService;
import com.sprint.mission.discodeit.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BasicServerService implements ServerService {
    private final TokenStore tokenStore;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            serverRepository.reset();
        }
    }

    @CustomLogging
    @Override
    public Server create(ServerCreateRequestDTO serverCreateRequestDTO) {
        String toekn = tokenStore.getToken(serverCreateRequestDTO.userId());
        checkValidToken(toekn);

        User owner = userRepository.findById(serverCreateRequestDTO.userId());
        Server server = new Server(owner.getId(), serverCreateRequestDTO.name());

        serverRepository.save(server, owner);

        return server;
    }

    @Override
    @CustomLogging
    public User join(UUID serverId, UUID userId) {
        Server server = serverRepository.findById(serverId);
        User user = userRepository.findById(userId);
        serverRepository.join(server, user);
        return user;
    }

    @Override
    public User quit(UUID serverId, UUID userId) {
        Server server = serverRepository.findById(serverId);
        User user = userRepository.findById(userId);
        serverRepository.quit(server, user);
        return user;
    }

    @Override
    public Server findById(UUID serverId) {
        return serverRepository.findById(serverId);

    }

    @Override
    public List<Server> findServerAll(UUID userId) {
        String toekn = tokenStore.getToken(userId);
        checkValidToken(toekn);

        List<Server> serverList = serverRepository.findAllByUserId(userId);
        return serverList;

    }

    @Override
    public UUID update(UUID serverId, UUID userId, UpdateServerRequestDTO updateServerRequestDTO) {
        String toekn = tokenStore.getToken(userId);
        checkValidToken(toekn);

        Server server = serverRepository.findById(serverId);
        Server update = serverRepository.update(server, updateServerRequestDTO);
        return update.getServerId();

    }

    @Override
    public void delete(UUID serverId, UUID userId) {
        String toekn = tokenStore.getToken(userId);
        checkValidToken(toekn);

        serverRepository.remove(serverId);
    }

    private void checkValidToken(String token) {
        Boolean validated = jwtUtil.validateToken(token);
        if (!validated) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
    }
}
