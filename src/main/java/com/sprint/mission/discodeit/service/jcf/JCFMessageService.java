package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(String text, UUID userId, UUID channelId) {
        channelService.searchChannel(channelId);
        userService.searchUser(userId);

        Message message = new Message(text, userId, channelId);
        data.put(message.getId(), message);
        System.out.println("메세지가 생성되었습니다: \n" + message);
        return message;
    }

    @Override
    public Message searchMessage(UUID messageId) {
        Message message = findMessage(messageId);
        System.out.println("MESSAGE: " + data.get(messageId));
        return message;
    }

    @Override
    public List<Message> searchAllMessages() {
        if (data.isEmpty()) {
            throw new NoSuchElementException("등록된 메세지가 존재하지 않습니다.");
        }
        List<Message> messages = new ArrayList<>(data.values());
        for (Message message : messages) {
            System.out.println("MESSAGE: " + message);
        }
        return messages;
    }

    @Override
    public Message updateMessage(UUID messageId, String text) {
        Message message = findMessage(messageId);
        message.updateText(text);
        System.out.println(messageId + " 메세지 업데이트 완료되었습니다.");
        return message;
    }

    @Override
    public void deleteMessage(UUID messageId) {
        findMessage(messageId);
        data.remove(messageId);
        System.out.println(messageId + " 메세지 삭제 완료되었습니다.");

    }

    private Message findMessage(UUID messageId) {
        Message message = data.get(messageId);
        if (message == null) {
            throw new NoSuchElementException("해당 메세지가 존재하지 않습니다.");
        }
        return message;
    }
}
