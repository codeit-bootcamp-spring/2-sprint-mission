package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.Server.ServerDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Exception.EmptyUserListException;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JCFServerRepository implements ServerRepository {
    private Map<UUID, List<Server>> serverList = new ConcurrentHashMap<>();

    @Override
    public void reset() {
        serverList = new ConcurrentHashMap<>();
    }

    @Override
    public UUID save(User user, Server server) {
        List<Server> servers = serverList.getOrDefault(user.getId(), new ArrayList<>());
        servers.add(server);
        serverList.put(user.getId(), servers);

        return server.getServerId();
    }

    @Override
    public UUID join(User user , Server server) {
        List<User> users = server.getUserList();
        users.add(user);

        return user.getId();
    }


    @Override
    public UUID quit(User user, Server server) {
        List<User> users = server.getUserList();
        if (users.isEmpty()) {
            throw new EmptyUserListException("서버 내 유저 리스트가 비어있습니다.");
        }
        users.remove(user);

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
    public UUID update(Server targetServer, ServerDTO serverDTO) {
        if (serverDTO.serverId() != null) {
            targetServer.setServerId(serverDTO.serverId());
        }
        if (serverDTO.userId() != null) {
            targetServer.setOwnerId(serverDTO.userId());
        }
        if (serverDTO.name() != null) {
            targetServer.setName(serverDTO.name());
        }
        return targetServer.getServerId();
    }


    @Override
    public void remove(User owner, Server server) {
        List<Server> list = findAllByUserId(owner.getId());
        list.remove(server);
    }

}
