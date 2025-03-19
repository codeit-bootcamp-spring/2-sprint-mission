package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.provider.MessageUpdaterProvider;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.updater.MessageUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentService binaryContentService;
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
        if (messageCreateRequest.requests() != null) {      // 프론트에서 json을 전달할 때, reqeust 부분을 비워놓지 않고 반드시 빈 리스트로 만들어서 전달하면 필요 없을듯?
            for (BinaryContentCreateRequest request : messageCreateRequest.requests()) {
                newMessage.addAttachment(this.binaryContentService.create(request));    // binaryContentService를 통해 binaryContent를 생성하고 id를 message에 넣기
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
            if (!binaryContentService.existsById(attachmentId)) {
                throw new NoSuchElementException("해당 Id가 binaryContentRepository에 존재하지 않습니다 : " + attachmentId);
            }
        }
        List<MessageUpdater> applicableUpdaters = messageUpdaterProvider.getApplicableUpdaters(findMessage, messageUpdateRequest);
        applicableUpdaters.forEach(updater -> updater.update(findMessage, messageUpdateRequest, this.messageRepository));
    }

    @Override
    public void deleteBinaryContentInMessage(UUID messageId, UUID binaryContentId) {
        this.binaryContentService.deleteByID(binaryContentId);
        this.messageRepository.deleteAttachment(messageId, binaryContentId);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        List<UUID> attachmentIds = this.messageRepository.findById(messageId).getAttachmentIds();
        for(UUID id : attachmentIds) {
            this.binaryContentService.deleteByID(id);
        }
        this.messageRepository.deleteById(messageId);
    }
}
