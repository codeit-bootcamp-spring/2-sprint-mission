package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Factory.CreateServerFactory;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.impl.LinkedListUserRepository;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

/**
 * <h3>유저 서비스 구현체 </h3><p>
 * 서버와 채널을 생성, 삭제, 추가, 업데이트 하는 기능을 수행한다. <br>
 * 유저마다 동일한 기능을 수행해야 한다. <br>
 * 유저마다 유저 레포지토리는 달라야한다.<br>
 * 이를 위해서 Map<유저 ID,유저 레포지토리>를 생성한다.<br>
 * </p>
 *
 * @version 1
 * @JongwonLee
 */
public class JCFUserService implements UserService {
    private static JCFUserService instance;
    private final Map<UUID, UserRepository> userTable = new HashMap<>();

    private JCFUserService() {
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }
        return instance;
    }


    //레포지토리 생성
    private UserRepository getUserRepository(UUID id) {
        UserRepository userRepository = userTable.get(id);
        if (userRepository == null) {
            userTable.put(id, new LinkedListUserRepository());
        }
        return userRepository;
    }

    @Override
    public Message write(UUID id, UUID targetId, String str) {
        UserRepository userRepository = getUserRepository(id);
        Map<UUID, Queue<Message>> messageList = userRepository.getMessageList();
        Queue<Message> messages = messageList.get(targetId);
        Message message = new Message(str);
        messages.add(message);
        return message;
    }

    @Override
    public Message write(UUID id, UUID targetId, Message message) {
        UserRepository userRepository = getUserRepository(id);
        Map<UUID, Queue<Message>> messageList = userRepository.getMessageList();
        Queue<Message> messages = messageList.get(targetId);
        messages.add(message);
        return message;
    }

    public Server createServer(String name) {
        return CreateServerFactory.getInstance().create(name);
    }

    @Override
    public void addServer(UUID id, String name) {
        UserRepository userRepository = getUserRepository(id);
        Server server = createServer(name);
        userRepository.add(server);

        //로그
        System.out.println(server.getName() + " 서버 추가 성공");
    }

    @Override
    public void addServer(UUID id, Server server) {
        UserRepository userRepository = getUserRepository(id);
        userRepository.add(server);

        //로그
        System.out.println(server.getName() + " 서버 추가 성공");
    }

    @Override
    public Server getServer(UUID id, String name) {
        UserRepository userRepository = getUserRepository(id);
        List<Server> list = userRepository.getServerList();
        for (Server server : list) {
            if (server.getName().equals(name)) {
                //로그
                System.out.println(server.getName() + " 이(가) 반환됩니다.");
                return server;
            }
        }
        //로그
        System.out.println("존재하지 않습니다.");
        return null;
    }

    @Override
    public void printServer(UUID id) {
        UserRepository userRepository = getUserRepository(id);
        List<Server> list = userRepository.getServerList();
        printServer(list);
    }

    @Override
    public void printServer(List<Server> list) {
        System.out.println("=========서버 목록==========");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " : " + list.get(i).getName());
        }
        System.out.println("=========================");
    }

    @Override
    public boolean removeServer(UUID id, String targetName) {
        UserRepository userRepository = getUserRepository(id);
        List<Server> list = userRepository.getServerList();
        for (Server server : list) {
            if (server.getName().equals(targetName)) {
                //로그
                System.out.println(server.getName() + " 이(가) 삭제됩니다.");
                list.remove(server);
                return true;
            }
        }
        System.out.println("존재하지 않습니다.");
        return false;
    }

    @Override
    public boolean updateServer(UUID id, String targetName, String replaceName) {
        UserRepository userRepository = getUserRepository(id);
        List<Server> list = userRepository.getServerList();
        for (Server server : list) {
            if (server.getName().equals(targetName)) {
                server.setName(replaceName);
                //로그
                System.out.println(targetName + " 이름이 " + server.getName() + " 이(가) 됩니다.");
                return true;
            }
        }
        System.out.println("존재하지 않습니다.");
        return false;
    }


}
