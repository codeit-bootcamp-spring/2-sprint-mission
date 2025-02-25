package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.Factory.CreateChannalFactory;
import com.sprint.mission.discodeit.Factory.CreateServerFactory;
import com.sprint.mission.discodeit.Factory.Factory;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.UserService;

public class JCFUserService implements UserService {
    private static JCFUserService instance;

    private JCFUserService() {
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }
        return instance;
    }

    public Server createServer(String name) {
        Factory<Server> factory = CreateServerFactory.getInstance();
        Server server = factory.create(name);
        System.out.println("서버 생성 성공");
        return server;
    }

    public Channel createChannel(String name) {
        Factory<Channel> factory = CreateChannalFactory.getInstance();
        Channel channel = factory.create(name);
        System.out.println("채널 생성 성공");
        return channel;
    }

    public void printServer() {
    }

    public void printChannel() {
    }

    public void removeServer(String targetName) {
    }

    public void removeChannel(String targetName) {
    }

    public void updateServer(String targetName, String replaceName) {
    }

    public void updateChannel(String targetName, String replaceName) {
    }

}
