package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

public interface UserRepository {
     void save(Server server);

     List<Server> getServerList();

     void updateServerList(List<Server> serverList);

     Map<UUID, Queue<Message>> getMessageList();

     void updateMessageList(Map<UUID, Queue<Message>> messageList);


}
