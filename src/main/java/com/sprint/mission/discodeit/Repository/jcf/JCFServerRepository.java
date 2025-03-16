package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.Server.ServerUpdateDTO;
import com.sprint.mission.discodeit.Exception.ServerNotFoundException;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;
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
        users.remove(user);

        return server.getServerId();
    }

    @Override
    public Server find(UUID serverId) {
        Server server = serverList.values().stream().flatMap(List::stream)
                .filter(s -> s.getServerId().equals(serverId))
                .findFirst()
                .orElseThrow(() -> new ServerNotFoundException("해당 ID를 가지는 서버를 찾을 수 없습니다."));
        return server;
    }

    @Override
    public List<Server> findAllByUserId(UUID userId) {
        List<Server> list = serverList.get(userId);
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
        List<Server> list = serverList.get(owner.getId());
        list.remove(server);
    }

}
