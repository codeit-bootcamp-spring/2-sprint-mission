package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.impl.LinkedListUserRepository;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;
    JCFUserService userService = JCFUserService.getInstance();
    private final Map<UUID, UserRepository> userTable = new HashMap<>();

    private JCFMessageService() {
    }

    public static JCFMessageService getInstance() {
        if (instance == null) {
            instance = new JCFMessageService();
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
    public void send(UUID id, UUID targetId, String str) {
        Message message = userService.write(id, targetId, str);
        System.out.println("전송중");
        UserRepository userRepository = getUserRepository(targetId);
        Map<UUID, Queue<Message>> messageList = userRepository.getMessageList();
        Queue<Message> messages = messageList.get(targetId);
        messages.add(message);
        System.out.println("전송완료");
    }

    @Override
    public void send(UUID id, UUID targetId, Message message) {
        System.out.println("전송중");
        UserRepository userRepository = getUserRepository(targetId);
        Map<UUID, Queue<Message>> messageList = userRepository.getMessageList();
        Queue<Message> messages = messageList.get(targetId);
        messages.add(message);
        System.out.println("전송완료");
    }
}
