package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.Server.ServerCRUDDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Exception.EmptyUserListException;
import com.sprint.mission.discodeit.Repository.FileRepositoryImpl;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileServerRepository implements ServerRepository {
    private final FileRepositoryImpl<Map<UUID, List<Server>>> fileRepository;
    private Map<UUID, List<Server>> serverList = new ConcurrentHashMap<>();
    private final Path path =  Paths.get(System.getProperty("user.dir"), "data", "ServerList.ser");

    public FileServerRepository() {
        this.fileRepository = new FileRepositoryImpl<>(path);
        fileRepository.load();
    }

    @Override
    public void reset() {
        fileRepository.init();
        try {
            Files.deleteIfExists(path);

            serverList = new ConcurrentHashMap<>();

        } catch (IOException e) {
            System.out.println("리스트 초기화 실패");
        }
    }

    @Override
    public UUID save(User user, Server server) {
        List<Server> servers = serverList.getOrDefault(user.getId(), new ArrayList<>());
        servers.add(server);
        serverList.put(user.getId(), servers);

        fileRepository.save(serverList);
        return server.getServerId();
    }

    @Override
    public UUID join(User user , Server server) {
        List<User> users = server.getUserList();
        users.add(user);
        fileRepository.save(serverList);
        return user.getId();
    }


    @Override
    public UUID quit(User user, Server server) {
        List<User> users = server.getUserList();
        if (users.isEmpty()) {
            throw new EmptyUserListException("서버 내 유저 리스트가 비어있습니다.");
        }
        users.remove(user);
        fileRepository.save(serverList);
        return server.getServerId();
    }

    @Override
    public Server find(UUID serverId) {
        List<Server> list = serverList.values().stream().flatMap(List::stream).toList();
        Server server = CommonUtils.findById(list, serverId, Server::getServerId)
                .orElseThrow(() -> CommonExceptions.SERVER_NOT_FOUND);

        return server;
    }

    @Override
    public List<Server> findAllByUserId(UUID userId) {
        if (serverList.isEmpty()) {
            throw CommonExceptions.EMPTY_SERVER_LIST;
        }

        List<Server> list = serverList.get(userId);

        if (list.isEmpty()) {
            throw CommonExceptions.EMPTY_SERVER_LIST;
        }
        return list;
    }

    @Override
    public UUID update(Server targetServer, ServerCRUDDTO serverCRUDDTO) {
        if (serverCRUDDTO.serverId() != null) {
            targetServer.setServerId(serverCRUDDTO.serverId());
        }
        if (serverCRUDDTO.userId() != null) {
            targetServer.setOwnerId(serverCRUDDTO.userId());
        }
        if (serverCRUDDTO.name() != null) {
            targetServer.setName(serverCRUDDTO.name());
        }
        fileRepository.save(serverList);
        return targetServer.getServerId();
    }


    @Override
    public void remove(User owner, Server server) {
        List<Server> list = findAllByUserId(owner.getId());

        fileRepository.save(serverList);
        list.remove(server);
    }
}
