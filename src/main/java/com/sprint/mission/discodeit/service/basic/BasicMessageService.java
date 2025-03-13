package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    @Override
    public Message createMessage(UUID senderId, String content, UUID channelId) {
        UserService.validateUserId(senderId, this.userRepository);
        ChannelService.validateChannelId(channelId, this.channelRepository);
        // 해당 채널에 sender가 participant로 있는지 확인하는 코드 필요?
        if (!this.channelRepository.findById(channelId).getParticipants().contains(senderId)) {
            throw new NoSuchElementException("해당 senderId를 가진 사용자가 해당 channelId의 Channel에 참여하지 않았습니다.");
        }
        Message newMessage = new Message(senderId, content, channelId);     //content에 대한 유효성 검증은 Message 생성자에게 맡긴다.
        this.messageRepository.add(newMessage);
        return newMessage;
    }

    @Override
    public Message readMessage(UUID messageId) {
        return this.messageRepository.findById(messageId);
    }

    @Override
    public Map<UUID, Message> readAllMessages() {
        return this.messageRepository.getAll();
    }

    @Override
    public void updateMessageContent(UUID messageId, String newContent) {
        this.messageRepository.updateMessageContent(messageId, newContent);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        this.messageRepository.deleteById(messageId);
    }
}
