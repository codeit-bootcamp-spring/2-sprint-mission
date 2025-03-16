package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.Server.ServerCreateDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerDeleteDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerJoinDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerUpdateDTO;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
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

    }

    @Override
    public UUID create(ServerCreateDTO serverCreateDTO) {
        UUID userId = UUID.fromString(serverCreateDTO.ownerId());
        User owner;

        owner = userRepository.find(userId);
        Server server = new Server(userId, serverCreateDTO.name());
        serverRepository.save(owner, server);

        return server.getServerId();
    }


    @Override
    public UUID join(ServerJoinDTO serverJoinDTO) {
        UUID userId = UUID.fromString(serverJoinDTO.userId());
        UUID serverId = UUID.fromString(serverJoinDTO.serverId());
        User user = userRepository.find(userId);
        Server server = serverRepository.find(serverId);
        serverRepository.join(user, server);

        return server.getServerId();
    }

    @Override
    public Server find(String serverId) {
        UUID targetServerId = UUID.fromString(serverId);
        Server server = serverRepository.find(targetServerId);
        return server;
    }

    @Override
    public List<Server> findServerAll(String ownerId) {
        UUID userId = UUID.fromString(ownerId);
        List<Server> serverList = serverRepository.findAllByUserId(userId);
        return serverList;
    }

    @Override
    public void print(String userId) {
        List<Server> servers = findServerAll(userId);
        servers.forEach(System.out::println);
    }

    @Override
    public boolean delete(ServerDeleteDTO serverDeleteDTO) {
        UUID userId = UUID.fromString(serverDeleteDTO.ownerId());
        UUID serverId = UUID.fromString(serverDeleteDTO.serverId());
        User user = userRepository.find(userId);
        Server server = serverRepository.find(serverId);
        serverRepository.remove(user, server);
        return true;
    }

    @Override
    public boolean update(String serverId, ServerUpdateDTO serverUpdateDTO) {
        UUID targetServerId = UUID.fromString(serverId);
        Server server = serverRepository.find(targetServerId);
        serverRepository.update(server, serverUpdateDTO);
        return true;
    }

}
