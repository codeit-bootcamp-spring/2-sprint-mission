package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.Server.ServerCRUDDTO;
import com.sprint.mission.discodeit.Exception.NotFoundExceptions;
import com.sprint.mission.discodeit.Exception.EmptyExceptions;
import com.sprint.mission.discodeit.Exception.EmptyUserListException;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFServerRepository implements ServerRepository {
    private Map<UUID, List<Server>> serverList = new ConcurrentHashMap<>();

    @Override
    public void reset() {
        serverList = new ConcurrentHashMap<>();
    }

    @Override
    public Server save(Server server, User user) {
        List<Server> servers = serverList.getOrDefault(user.getId(), new ArrayList<>());
        servers.add(server);
        serverList.put(user.getId(), servers);

        return server;
    }

    @Override
    public User join(Server server, User user) {
        List<User> users = server.getUserList();
        users.add(user);

        return user;
    }


    @Override
    public User quit(Server server, User user) {
        List<User> users = server.getUserList();
        if (users.isEmpty()) {
            throw new EmptyUserListException("서버 내 유저 리스트가 비어있습니다.");
        }
        users.remove(user);
        return user;
    }

    @Override
    public Server find(UUID serverId) {
        List<Server> list = serverList.values().stream().flatMap(List::stream).toList();
        Server server = CommonUtils.findById(list, serverId, Server::getServerId)
                .orElseThrow(() -> NotFoundExceptions.SERVER_NOT_FOUND);
        return server;
    }

    @Override
    public List<Server> findAllByUserId(UUID userId) {
        if (serverList.isEmpty()) {
            throw EmptyExceptions.EMPTY_SERVER_LIST;
        }
        List<Server> list = serverList.get(userId);
        if (list.isEmpty()) {
            throw EmptyExceptions.EMPTY_SERVER_LIST;
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
        return targetServer;
    }


    @Override
    public void remove(Server server, User user) {
        List<Server> list = findAllByUserId(user.getId());
        list.remove(server);
    }

}
