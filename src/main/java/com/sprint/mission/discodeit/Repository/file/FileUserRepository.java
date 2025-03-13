package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.Exception.ServerNotFoundException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileUserRepository implements UserRepository {
    private  List<User> registeredUsers = new ArrayList<>();
    private  Map<UUID, List<Server>> serverList = new ConcurrentHashMap<>();
    private final Path userPath =  Paths.get(System.getProperty("user.dir"), "data", "UserList.ser");
    private final Path serverPath =  Paths.get(System.getProperty("user.dir"), "data", "ServerList.ser");

    public FileUserRepository() {
        loadUserList();
        loadServerList();
    }

    // 서버 리스트를 저장할 디렉토리가 있는지 확인
    private void init() {
        Path directory = serverPath.getParent();
        if (!Files.exists(directory)) {
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
        if (Files.exists(serverPath)) {
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

    private void loadUserList() {
        if (Files.exists(serverPath)) {
            try (FileInputStream fis = new FileInputStream(userPath.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                List<User> list = (List<User>) ois.readObject();
                for (User user : list) {
                    User u = new User(user.getId(), user.getCreatedAt(), user.getName(), user.getPassword());
                }


            } catch (IOException | ClassNotFoundException e) {
                System.out.println("서버 리스트 로드 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void saveUserList() {
        init();
        try (FileOutputStream fos = new FileOutputStream(userPath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(registeredUsers);

        } catch (IOException e) {
            System.out.println("서버 리스트 저장 실패");
            throw new RuntimeException(e);
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
    public UUID saveUser(User user) {
        registeredUsers.add(user);

        saveUserList();

        return user.getId();
    }


    @Override
    public UUID saveServer(User user, Server server) {
        serverList.computeIfAbsent(user.getId(), k -> new ArrayList<>()).add(server);

        saveServerList();

        return server.getServerId();
    }


    @Override
    public User findUser(User targetUser) {
        User user = registeredUsers.stream()
                .filter(u -> u.getId().equals(targetUser.getId()))
                .findFirst()
                .orElseThrow(()->new UserNotFoundException("해당 유저는 존재하지 않습니다." + targetUser.getId()));
        return user;
    }

    @Override
    public User findUserByUserId(UUID userId) {
        User user = registeredUsers.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(()->new UserNotFoundException("해당 유저는 존재하지 않습니다." + userId));
        return user;
    }

    @Override
    public List<Server> findServerListByOwner(User owner) {
        List<Server> list = Optional.ofNullable(serverList.get(owner.getId())).orElseThrow(() -> new ServerNotFoundException("서버 리스트가 비어있습니다."));
        return list;
    }

    @Override
    public Server findServerByServerId(User owner, UUID serverId) {
        List<Server> servers = findServerListByOwner(owner);
        Server findServer = servers.stream().filter(s -> s.getServerId().equals(serverId))
                .findFirst().orElseThrow(() -> new ServerNotFoundException("해당 서버는 존재하지 않습니다." + serverId));
        return findServer;
    }

    @Override
    public Server findServerByOwner(User owner, Server targetServer) {
        return findServerByServerId(owner, targetServer.getServerId());
    }

    @Override
    public UUID updateUserName(User user, String replaceName) {
        User targetUser = findUser(user);
        targetUser.setName(replaceName);
        saveUserList();
        return targetUser.getId();
    }

    @Override
    public UUID updateServerName(User owner, Server server, String replaceName) {
        List<Server> serverListByOwner = findServerListByOwner(owner);
        Server targetServer = findServerByOwner(owner, server);

        targetServer.setName(replaceName);
        serverList.put(owner.getId(), serverListByOwner);
        saveServerList();
        return targetServer.getServerId();
    }

    @Override
    public UUID removeUser(User user) {
        User targetUser = findUser(user);
        registeredUsers.remove(targetUser);
        saveUserList();
        return targetUser.getId();
    }

    @Override
    public UUID removeServer(User owner, Server server) {
        List<Server> serverListByOwner = findServerListByOwner(owner);
        Server targetServer = findServerByOwner(owner, server);

        serverListByOwner.remove(targetServer);
        serverList.put(owner.getId(), serverListByOwner);
        saveServerList();
        return targetServer.getServerId();
    }
}

