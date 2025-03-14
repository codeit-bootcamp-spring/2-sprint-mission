package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.Exception.ServerNotFoundException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JCFUserRepository implements UserRepository {
    private  List<User> registeredUsers = new ArrayList<>();
    private  Map<UUID, List<Server>> serverList = new ConcurrentHashMap<>();
    private  Map<UUID, List<User>> serverUser = new ConcurrentHashMap<>();

    @Override
    public UUID saveUser(User user) {
        registeredUsers.add(user);

        System.out.println("🔍 saveUser: 요청된 userId: " + user.getId());
        System.out.println("🔍 saveUser: 현재 저장된 유저 목록: " + registeredUsers);

        return user.getId();
    }

    @Override
    public UUID saveServer(User user, Server server) {
        List<Server> servers = serverList.getOrDefault(user.getId(), new ArrayList<>());
        servers.add(server);
        serverList.put(user.getId(), servers);

        System.out.println("✅ saveServer 서버 저장됨: " + server);
        System.out.println("✅ saveServer 현재 저장된 서버 목록: " + serverList);

        return server.getServerId();
    }

    @Override
    public UUID joinServer(User user, User owner, Server server) {
        List<User> users = serverUser.getOrDefault(user.getId(), new ArrayList<>());
        users.add(user);
        serverUser.put(server.getServerId(), users);

        System.out.println("✅ joinServer 서버내 유저 저장됨: " + server);
        System.out.println("✅ joinServer 현재 저장된 서버 목록: " + serverUser);

        return user.getId();
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
        System.out.println("🔍 findUserByUserId: 요청된 userId: " + userId);
        System.out.println("🔍 findUserByUserId: 현재 저장된 유저 목록: " + registeredUsers);

        User user = registeredUsers.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("해당 유저는 존재하지 않습니다." + userId));
        return user;
    }

    @Override
    public List<User> findUserList() {
        return registeredUsers;
    }

    @Override
    public List<Server> findServerListByOwner(User owner) {
        System.out.println("🔍 요청된 userId (서버 검색): " + owner.getId());
        System.out.println("🔍 현재 저장된 서버 목록: " + serverList);

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
        return targetUser.getId();
    }

    @Override
    public UUID updateServerName(User owner, Server server, String replaceName) {
        List<Server> serverListByOwner = findServerListByOwner(owner);
        Server targetServer = findServerByOwner(owner, server);

        targetServer.setName(replaceName);
        serverList.put(owner.getId(), serverListByOwner);

        return targetServer.getServerId();
    }

    @Override
    public UUID removeUser(User user) {
        User targetUser = findUser(user);
        registeredUsers.remove(targetUser);
        return targetUser.getId();
    }

    @Override
    public UUID removeServer(User owner, Server server) {
        List<Server> serverListByOwner = findServerListByOwner(owner);
        Server targetServer = findServerByOwner(owner, server);

        serverListByOwner.remove(targetServer);
        serverList.put(owner.getId(), serverListByOwner);

        return targetServer.getServerId();
    }

    @Override
    public UUID quitServer(User user, Server server) {
        List<User> users = serverUser.get(server.getServerId());
        users.remove(user);

        return server.getServerId();
    }
}
