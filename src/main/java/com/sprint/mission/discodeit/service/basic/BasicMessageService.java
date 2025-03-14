package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public Message create(String message, UUID channelId, UUID senderId) {
        List<User> userList = userRepository.load();
        List<Channel> channelList = channelRepository.load();
        Optional<User> user = userList.stream()
                .filter(u -> u.getId().equals(senderId))
                .findAny();
        Optional<Channel> channel = channelList.stream()
                .filter(c -> c.getId().equals(channelId))
                .findAny();
        if (user.isEmpty()) {
            throw new IllegalArgumentException("등록된 사용자가 없습니다.");
        }
        if (channel.isEmpty()) {
            throw new IllegalArgumentException("등록된 채널이 없습니다.");
        }
        Message messages = new Message(message, channelId, senderId);
        messageRepository.save(messages);
        return messages;
    }


    @Override
    public Message getMessage(UUID messageId) {
        Optional<Message> message = messageRepository.load().stream()
                .filter(m -> m.getId().equals(messageId))
                .findAny();
        return message.orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));
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
    public Message update(UUID messageId, String changeMessage) {
        Message message = getMessage(messageId);
        message.updateMessage(changeMessage);
        messageRepository.save(message);
        return message;
    }


    @Override
    public void delete(UUID messageId) {
        Message message = getMessage(messageId);
        messageRepository.remove(message);
    }
}
