package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;
    private final Map<UUID, Message> data;

    private JCFUserService userservice;
    private JCFChannelService channelservice;

    public static JCFMessageService getInstance(JCFUserService userService, JCFChannelService channelService) {
        if (instance == null) {
            instance = new JCFMessageService(userService, channelService);
        }
        return instance;
    }

    private JCFMessageService(JCFUserService userservice, JCFChannelService channelservice) {
        this.userservice = userservice;
        this.channelservice = channelservice;
        data = new HashMap<>();
    }


    @Override
    public UUID createMessage(UUID userId, UUID channelId) {
        if (!userservice.existUser(userId)) {
            throw new IllegalArgumentException("존재하지 않는 사용자 입니다. 메세지를 생성할 수 없습니다.");

        }
        if (!channelservice.existChannel(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널 입니다. 메세지를 생성할 수 없습니다.");
        }
        Message message = new Message(userId, channelId);
        data.put(message.getId(), message);
        System.out.println("메세지가 생성되었습니다: \n" + message);
        return message.getId();
    }

    @Override
    public void searchMessage(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("조회하신 메세지가 존재하지 않습니다.");
            return;
        }
        System.out.println("MESSAGE: " + data.get(id));
    }

    @Override
    public void searchAllMessages() {
        if (data.isEmpty()) {
            System.out.println("등록된 메세지가 존재하지 않습니다.");
            return;
        }
        for (Message message : data.values()) {
            System.out.println("MESSAGE: " + message);
        }
    }

    @Override
    public void updateMessage(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("업데이트할 메세지가 존재하지 않습니다.");
            return;
        }
        data.get(id).updateTime(System.currentTimeMillis());
        System.out.println(id + " 메세지 업데이트 완료되었습니다.");
    }

    @Override
    public void deleteMessage(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("삭제할 메세지가 존재하지 않습니다.");
            return;
        }
        data.remove(id);
        System.out.println(id + " 메세지 삭제 완료되었습니다.");

    }
}
