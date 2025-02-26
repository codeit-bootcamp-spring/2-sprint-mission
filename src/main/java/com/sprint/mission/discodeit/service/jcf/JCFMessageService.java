package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final JCFUserService userService;
    private  final JCFChannelService channelService;
    Map<UUID, Message> messagesRepository = new HashMap<>();

    public JCFMessageService(JCFUserService userService, JCFChannelService channelService) {
        this.userService= userService;
        this.channelService = channelService;
    }

    @Override
    public boolean sendMessage(Message message) {
        if(message.getUser().getChannel().getId().equals(message.getReceiver().getChannel().getId())){
            if(message.getContent()==null) {
                return false;
            }
            messagesRepository.put(message.getMessageId(),message);
            return true;
        }
        System.out.println("not same server");
        return false;

    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        if(messagesRepository.containsKey(messageId)) {
            messagesRepository.remove(messageId);
            return true;
        }
        return false;
    }


    @Override
    public List<Message> findAllMessages() {
        return new ArrayList<>(messagesRepository.values());
    }

    @Override
    public String createAllMessageContents() {
        StringBuilder result = new StringBuilder();
        for (Message message : findAllMessages()) {
            result.append(message.getContent()).append(" id=(").append(message.getMessageId()).append(")").append("\n");
        }
        return result.toString();
    }

    @Override
    public String findOneMessage(UUID messageId) {
        String result = "";
        Message oneMessage =messagesRepository.get(messageId);
        result += oneMessage.getContent() + " id=(" + oneMessage.getMessageId() + ")" + "\n";
        return result;
    }

    @Override
    public void editMessage(UUID messageId, String content) {
        if(messagesRepository.containsKey(messageId)) {
            messagesRepository.get(messageId).setContent(content);
        }
    }

    @Override
    public String displayEditmessages() {//출력 안됨
        StringBuilder result = new StringBuilder();
        for (Message message : findEditedMessages()) {
            result.append(message.getContent()).append(" id=(").append(message.getMessageId()).append(")").append("\n");
        }
        return result.toString();
    }

    @Override
    public List<Message> findEditedMessages() {
        return messagesRepository.values().stream()
                .filter(message -> message.getCreatedAt() != message.getUpdatedAt())
                .toList();
    }
}
