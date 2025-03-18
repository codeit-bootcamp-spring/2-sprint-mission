package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.provider.MessageUpdaterProvider;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.updater.MessageUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageUpdaterProvider messageUpdaterProvider;

    @Override
    public Message createMessage(MessageCreateRequest messageCreateRequest) {
        UserService.validateUserId(messageCreateRequest.senderId(), this.userRepository);
        ChannelService.validateChannelId(messageCreateRequest.channelId(), this.channelRepository);
        Channel channel = channelRepository.findById(messageCreateRequest.channelId());
        // 해당 채널에 sender가 participant로 있는지 확인하는 코드 필요?
        if (!channel.getParticipantIds().contains(messageCreateRequest.senderId())) {
            throw new NoSuchElementException("해당 senderId를 가진 사용자가 해당 channelId의 Channel에 참여하지 않았습니다.");
        }
        Message newMessage = new Message(messageCreateRequest.senderId(), messageCreateRequest.content(), messageCreateRequest.channelId());     // content에 대한 유효성 검증은 Message 생성자에게 맡긴다.
        if (messageCreateRequest.attachmentIds() != null && !messageCreateRequest.attachmentIds().isEmpty()) {
            for (UUID attachmentId : messageCreateRequest.attachmentIds()) {
                if (binaryContentRepository.existsById(attachmentId)) {
                    newMessage.addAttachment(attachmentId);
                }
            }
        }
        this.messageRepository.add(newMessage);
        channel.setLastMessageTime(newMessage.getCreatedAt());              // 채널에 마지막 메세지 시간 초기화
        return newMessage;
    }

    @Override
    public Message readMessage(UUID messageId) {
        return this.messageRepository.findById(messageId);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return this.messageRepository.findMessageListByChannelId(channelId);
    }

    @Override
    public void updateMessage(MessageUpdateRequest messageUpdateRequest) {
        Message findMessage = this.messageRepository.findById(messageUpdateRequest.messageId());
        for(UUID attachmentId : messageUpdateRequest.attachmentIds()) {
            if (!binaryContentRepository.existsById(attachmentId)) {
                throw new NoSuchElementException("해당 Id가 binaryContentRepository에 존재하지 않습니다 : " + attachmentId);
            }
        }
        List<MessageUpdater> applicableUpdaters = messageUpdaterProvider.getApplicableUpdaters(findMessage, messageUpdateRequest);
        applicableUpdaters.forEach(updater -> updater.update(findMessage, messageUpdateRequest, this.messageRepository));
    }

    @Override
    public void deleteMessage(UUID messageId) {
        this.messageRepository.deleteById(messageId);
    }
}
