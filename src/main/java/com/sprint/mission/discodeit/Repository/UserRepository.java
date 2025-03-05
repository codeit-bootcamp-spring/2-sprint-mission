package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

public interface UserRepository {
    public void save(Server server);

    public List<Server> getServerList();

    public void setServerList(List<Server> serverList);

    public Map<UUID, Queue<Message>> getMessageList();

    public void setMessageList(Map<UUID, Queue<Message>> messageList);


}
