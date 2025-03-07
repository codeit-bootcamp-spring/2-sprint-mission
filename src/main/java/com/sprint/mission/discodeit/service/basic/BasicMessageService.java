package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public BasicMessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @Override
    public Message create(Message message) {
        User user = userService.getUser(message.getSender());
        if (user == null) {
            System.out.println("등록된 사용자가 없습니다.");
            return null;
        } else {
            messageRepository.save(message);
            return message;
        }
    }


    @Override
    public List<Message> getMessage(String sender) {
        return find(sender);
    }

    private List<Message> find(String sender) {
        return messageRepository.load().stream()
                .filter(message -> message.getSender().equals(sender))
                .toList();
    }


    @Override
    public List<Message> getAllMessage() {
        List<Message> messageList = messageRepository.load();
        if (messageList.isEmpty()) {
            System.out.println("전체 조회 결과가 없습니다.");
            return Collections.emptyList();
        }
        return messageList;
    }


    @Override
    public Message update(String sender, UUID uuid, String changeMessage) {
        List<Message> messageList = find(sender);
        Message messages = messageList.stream()
                .filter(message -> message.getId().equals(uuid))
                .findAny()
                .orElse(null);
        if (messages == null) {
            System.out.println("메시지가 존재하지 않습니다.");
            return null;
        } else {
            messages.updateMessage(changeMessage);
            messageRepository.save(messages);
        }
        return messages;
    }


    @Override
    public void delete(String sender, UUID uuid) {
        List<Message> messageList = find(sender);
        Message messages = messageList.stream()
                .filter(message -> message.getId().equals(uuid))
                .findAny()
                .orElse(null);
        if(messages == null) {
            System.out.println("메시지가 존재하지 않습니다.");
        } else {
            messageRepository.deleteFromFile(messages);
        }
    }
}
