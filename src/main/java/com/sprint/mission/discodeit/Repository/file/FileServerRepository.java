package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.Server.ServerUpdateDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileServerRepository implements ServerRepository {
    private Map<UUID, List<Server>> serverList = new ConcurrentHashMap<>();
    private final Path serverPath =  Paths.get(System.getProperty("user.dir"), "data", "ServerList.ser");

    public FileServerRepository() {
        loadServerList();
    }

    private void init() {
        Path directory = serverPath.getParent();
        if (Files.exists(directory) == false) {
            try {
                Files.createDirectories(directory);
                System.out.println("디렉토리 생성 완료: " + directory);
            } catch (IOException e) {
                System.out.println("디렉토리 생성 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void loadServerList() {
        init();
        if (Files.exists(serverPath) == true) {
            try (FileInputStream fis = new FileInputStream(serverPath.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                Map<UUID, List<Server>> list = (Map<UUID, List<Server>>) ois.readObject();
                serverList = list;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("서버 리스트 로드 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void saveServerList() {
        init();
        try (FileOutputStream fos = new FileOutputStream(serverPath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(serverList);

        } catch (IOException e) {
            System.out.println("서버 리스트 저장 실패");
            throw new RuntimeException(e);
        }
    }


    @Override
    public void reset() {
        init();
        try {
            Files.deleteIfExists(serverPath);

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

        saveServerList();
        return server.getServerId();
    }

    @Override
    public UUID join(User user , Server server) {
        List<User> users = server.getUserList();
        users.add(user);
        saveServerList();
        return user.getId();
    }


    @Override
    public UUID quit(User user, Server server) {
        List<User> users = server.getUserList();
        if (users.isEmpty()) {
            throw CommonExceptions.EMPTY_USER_LIST;
        }
        users.remove(user);
        saveServerList();
        return server.getServerId();
    }

    @Override
    public Server find(UUID serverId) {
        Server server = serverList.values().stream().flatMap(List::stream)
                .filter(s -> s.getServerId().equals(serverId))
                .findFirst()
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
    public UUID update(Server targetServer, ServerUpdateDTO serverUpdateDTO) {
        if (serverUpdateDTO.replaceServerId() != null) {
            targetServer.setServerId(serverUpdateDTO.replaceServerId());
        }
        if (serverUpdateDTO.replaceOwnerId() != null) {
            targetServer.setUserOwnerId(serverUpdateDTO.replaceOwnerId());
        }
        if (serverUpdateDTO.replaceName() != null) {
            targetServer.setName(serverUpdateDTO.replaceName());
        }
        return targetServer.getServerId();
    }


    @Override
    public void remove(User owner, Server server) {
        List<Server> list = findAllByUserId(owner.getId());

        saveServerList();
        list.remove(server);
    }
}
