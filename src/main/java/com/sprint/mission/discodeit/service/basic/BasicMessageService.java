package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageReadResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.common.NoSuchIdException;
import com.sprint.mission.discodeit.exception.message.CreateMessageException;
import com.sprint.mission.discodeit.exception.message.DeleteMessageException;
import com.sprint.mission.discodeit.exception.message.FindMessageListException;
import com.sprint.mission.discodeit.exception.message.UpdateMessageException;
import com.sprint.mission.discodeit.model.ChannelType;
import com.sprint.mission.discodeit.provider.MessageUpdaterProvider;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.updater.message.MessageUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // 일단 미션에서 요구해서 createMessage 메서드에서 binaryContent를 create하도록 구현했지만, controller에서 따로 분리되어 실행되도록 하는게 맞는 것 같다.
    @Override
    public UUID createMessage(MessageCreateRequest messageCreateRequest) {
        try {
            UserService.validateUserId(messageCreateRequest.senderId(), this.userRepository);
            ChannelService.validateChannelId(messageCreateRequest.channelId(), this.channelRepository);
            Channel channel = channelRepository.findById(messageCreateRequest.channelId());
            // Private 채널인 경우 sender가 participants로 있는지 확인
            if (channel.getChannelType() == ChannelType.PRIVATE && !channel.getParticipantIds().contains(messageCreateRequest.senderId())) {
                throw new NoSuchElementException("해당 senderId를 가진 사용자가 해당 channelId의 Channel에 참여하지 않았습니다.");
            }
            Message newMessage = new Message(messageCreateRequest.senderId(), messageCreateRequest.content(), messageCreateRequest.channelId());     // content에 대한 유효성 검증은 Message 생성자에게 맡긴다.
            if (messageCreateRequest.attachmentIds() != null) {      // 프론트에서 json을 전달할 때, attachmentIds 부분을 비워놓지 않고 반드시 빈 리스트로 만들어서 전달하면 필요 없을듯?
                for (UUID attachmentId : messageCreateRequest.attachmentIds()) {
                    if (!binaryContentService.existsById(attachmentId)) {
                        throw new NoSuchIdException("해당 attachmentId가 존재하지 않습니다 : " + attachmentId, HttpStatus.NOT_FOUND);
                    }
                    newMessage.addAttachment(attachmentId);    // binaryContentService를 통해 binaryContent를 생성하고 id를 message에 넣기
                }
            }
            this.messageRepository.add(newMessage);
            channel.setLastMessageTime(newMessage.getCreatedAt());              // 채널에 마지막 메세지 시간 초기화
            return newMessage.getId();
        } catch (NoSuchIdException e) {
            throw new CreateMessageException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (NoSuchElementException e) {
            throw new CreateMessageException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            throw new CreateMessageException("message 생성 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public Message readMessage(UUID messageId) {
        return this.messageRepository.findById(messageId);
    }

    @Override
    public List<MessageReadResponse> findAllByChannelId(UUID channelId) {
        try {
            ChannelService.validateChannelId(channelId, this.channelRepository);
            List<MessageReadResponse> messageReadResponses = new ArrayList<>();
            for (Message message : messageRepository.findMessageListByChannelId(channelId)) {
                messageReadResponses.add(new MessageReadResponse(message.getId(), message.getContent(), message.getAttachmentIds()));
            }
            return messageReadResponses;
        } catch (NoSuchIdException e) {
            throw new FindMessageListException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (NoSuchElementException e) {
            throw new FindMessageListException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            throw new FindMessageListException("channelId로 메세지 리스트 조회 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    // binaryContent를 업데이트할지 다음 미션의 컨트롤러에서 결정할 것!
    @Override
    public void updateMessage(UUID id, MessageUpdateRequest messageUpdateRequest) {
        try {
            Message findMessage = this.messageRepository.findById(id);
            for (UUID attachmentId : messageUpdateRequest.attachmentIds()) {
                if (!binaryContentService.existsById(attachmentId)) {
                    throw new NoSuchElementException("해당 Id가 binaryContentRepository에 존재하지 않습니다 : " + attachmentId);
                }
            }
            List<MessageUpdater> applicableUpdaters = messageUpdaterProvider.getApplicableUpdaters(findMessage, messageUpdateRequest);
            applicableUpdaters.forEach(updater -> updater.update(id, messageUpdateRequest, this.messageRepository));
        } catch (NoSuchIdException e) {
            throw new UpdateMessageException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (NoSuchElementException e) {
            throw new UpdateMessageException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        }
        catch (Exception e) {
            throw new UpdateMessageException("메세지 업데이트 도중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public void deleteBinaryContentInMessage(UUID messageId, UUID binaryContentId) {
        this.binaryContentService.deleteByID(binaryContentId);
        this.messageRepository.deleteAttachment(messageId, binaryContentId);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        try {
            List<UUID> attachmentIds = this.messageRepository.findById(messageId).getAttachmentIds();
            for (UUID id : attachmentIds) {
                this.binaryContentService.deleteByID(id);
            }
            this.messageRepository.deleteById(messageId);
        } catch (NoSuchIdException e) {
            throw new DeleteMessageException(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (Exception e) {
            throw new DeleteMessageException("message 삭제 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}
