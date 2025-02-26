package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

/**
 * <h3>유저 레포지토리 </h3><p>
 * 유저마다 서버 리스트를 갖는다. <br>
 * 유저는 개인 메시지함을 받는다.<br>
 * </p>
 *
 * @version 1
 * @JongwonLee
 */
public abstract class UserRepository {
    private List<Server> serverList;
    private Map<UUID, Queue<Message>> messageList;


    public List<Server> getServerList() {
        return serverList;
    }

    public void setServerList(List<Server> serverList) {
        this.serverList = serverList;
    }

    public Map<UUID, Queue<Message>> getMessageList() {
        return messageList;
    }

    public void setMessageList(Map<UUID, Queue<Message>> messageList) {
        this.messageList = messageList;
    }

    public void add(Server server) {
        serverList.add(server);
    }
}
