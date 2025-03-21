package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.legacy.Server.ServerCRUDDTO;
import com.sprint.mission.discodeit.Exception.Empty.EmptyServerListException;
import com.sprint.mission.discodeit.Exception.Empty.EmptyUserListException;
import com.sprint.mission.discodeit.Exception.NotFound.SaveFileNotFoundException;
import com.sprint.mission.discodeit.Exception.NotFound.ServerNotFoundException;
import com.sprint.mission.discodeit.Exception.NotFound.UserNotFoundException;
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
        try {
            this.serverList = fileRepository.load();
        } catch (SaveFileNotFoundException e) {
            System.out.println("FileServerRepository init");
        }
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
    public Server save(Server server, User user) {
        List<Server> servers = serverList.getOrDefault(user.getId(), new ArrayList<>());
        servers.add(server);
        serverList.put(user.getId(), servers);

        fileRepository.save(serverList);
        return server;
    }

    @Override
    public User join(Server server, User user) {
        List<User> users = server.getUserList();
        users.add(user);
        fileRepository.save(serverList);
        return user;
    }


    @Override
    public User quit(Server server, User user) {
        List<User> users = server.getUserList();
        if (users.isEmpty()) {
            throw new EmptyUserListException("서버 내 유저 리스트가 비어있습니다.");
        }
        users.remove(user);
        fileRepository.save(serverList);
        return user;
    }

    @Override
    public Server find(UUID serverId) {
        List<Server> list = serverList.values().stream().flatMap(List::stream).toList();
        Server server = CommonUtils.findById(list, serverId, Server::getServerId)
                .orElseThrow(() -> new ServerNotFoundException("서버를 찾을 수 없습니다."));

        return server;
    }

    @Override
    public Server findByOwnerId(UUID userId) {
        List<Server> servers = findAllByUserId(userId);
        Server server = servers.stream().filter(s -> s.getOwnerId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("서버장을 찾을 수 없습니다."));

        return server;
    }

    @Override
    public List<Server> findAllByUserId(UUID userId) {
        if (serverList.isEmpty()) {
            throw new EmptyServerListException("Repository 에 저장된 서버 리스트가 없습니다.");
        }

        List<Server> list = serverList.get(userId);

        if (list.isEmpty()) {
            throw new EmptyServerListException("해당 서버에 저장된 채널 리스트가 없습니다.");
        }
        return list;
    }

    @Override
    public Server update(Server targetServer, ServerCRUDDTO serverCRUDDTO) {
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
        return targetServer;
    }


    @Override
    public void remove(UUID serverId) {
        Server server = find(serverId);
        UUID ownerId = server.getOwnerId();
        List<Server> list = findAllByUserId(ownerId);

        fileRepository.save(serverList);
    }
}
