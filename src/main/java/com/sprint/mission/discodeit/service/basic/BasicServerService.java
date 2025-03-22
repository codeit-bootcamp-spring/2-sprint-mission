package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreateServerRequestDTO;
import com.sprint.mission.discodeit.dto.request.UpdateServerRequestDTO;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.repository.ServerRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BasicServerService implements ServerService {
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
    public Server create(CreateServerRequestDTO createServerRequestDTO) {

        User owner = userRepository.findById(createServerRequestDTO.userId());
        Server server = new Server(owner.getId(), createServerRequestDTO.name());
        serverRepository.save(server, owner);
        serverRepository.join(server, owner);

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
        Server server = serverRepository.findById(serverId);
        return server;

    }

    @Override
    public List<Server> findServerAll(UUID ownerId) {
        List<Server> serverList = serverRepository.findAllByUserId(ownerId);
        return serverList;

    }

    @Override
    public UUID update(UUID serverId, UpdateServerRequestDTO updateServerRequestDTO) {

        Server server = serverRepository.findById(serverId);
        Server update = serverRepository.update(server, updateServerRequestDTO);
        return update.getServerId();

    }

    @Override
    public void delete(UUID serverId) {
        serverRepository.remove(serverId);
    }

}
