package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private List<Server> serverList;
    private Map<UUID, Queue<Message>> messageList;

    public JCFUserRepository() {
        serverList = new LinkedList<>();
        messageList = new HashMap<>();
    }

    public List<Server> getServerList() {
        return serverList;
    }

    public void updateServerList(List<Server> serverList) {
        this.serverList = serverList;
    }

    public Map<UUID, Queue<Message>> getMessageList() {
        return messageList;
    }

    public void updateMessageList(Map<UUID, Queue<Message>> messageList) {
        this.messageList = messageList;
    }

    public void save(Server server) {
        serverList.add(server);
    }
}
