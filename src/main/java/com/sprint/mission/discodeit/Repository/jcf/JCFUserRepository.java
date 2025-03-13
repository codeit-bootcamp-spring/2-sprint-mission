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
    private final List<User> userList = new ArrayList<>();
    private final Map<UUID, List<Server>> serverList = new ConcurrentHashMap<>();

    @Override
    public UUID saveUser(User user) {
        userList.add(user);
        return user.getId();
    }

    @Override
    public UUID saveServer(User user, Server server) {
        serverList.computeIfAbsent(user.getId(), k -> new ArrayList<>()).add(server);

        return server.getServerId();
    }

    @Override
    public User findUser(User targetUser) {
        User user = userList.stream()
                .filter(u -> u.getId().equals(targetUser.getId()))
                .findFirst()
                .orElseThrow(()->new UserNotFoundException("해당 유저는 존재하지 않습니다." + targetUser.getId()));
        return user;
    }

    @Override
    public User findUserByUserId(UUID userId) {
        User user = userList.stream()
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
        userList.remove(targetUser);
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
}
