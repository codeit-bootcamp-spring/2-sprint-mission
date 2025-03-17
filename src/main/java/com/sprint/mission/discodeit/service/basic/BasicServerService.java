package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.Server.*;
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
    public UUID create(ServerDTO serverDTO) {
        ServerDTO serverCreateDTO = ServerDTO.create(serverDTO.userId(), serverDTO.name());
        UUID userId = serverCreateDTO.userId();
        User owner;

        owner = userRepository.find(userId);
        Server server = new Server(userId, serverCreateDTO.name());
        serverRepository.save(owner, server);

        return server.getServerId();
    }


    @Override
    public UUID join(ServerDTO serverDTO) {
        ServerDTO serverJoinDTO = ServerDTO.join(serverDTO.serverId(), serverDTO.userId());

        UUID serverId = serverJoinDTO.serverId();
        UUID userId = serverJoinDTO.userId();

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
    public boolean delete(ServerDTO serverDTO) {
        ServerDTO serverDeleteDTO = ServerDTO.delete(serverDTO.serverId(), serverDTO.userId());

        UUID serverId = serverDeleteDTO.serverId();
        UUID userId = serverDeleteDTO.userId();


        User user = userRepository.find(userId);
        Server server = serverRepository.find(serverId);
        serverRepository.remove(user, server);
        return true;
    }

    @Override
    public boolean update(String serverId, ServerDTO serverDTO) {
        ServerDTO serverUpdateDTO = ServerDTO.update(serverDTO.serverId(), serverDTO.userId(), serverDTO.name());
        UUID targetServerId = serverUpdateDTO.serverId();

        Server server = serverRepository.find(targetServerId);
        serverRepository.update(server, serverUpdateDTO);
        return true;
    }

}
