package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapping.MessageMapping;
import com.sprint.mission.discodeit.service.BinaryContentRepository;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Autowired
    public BasicMessageService(
            @Qualifier("basicMessageRepository") MessageRepository messageRepository,
            @Qualifier("basicChannelRepository") ChannelRepository channelRepository,
            @Qualifier("basicUserRepository") UserRepository userRepository,
            @Qualifier("basicBinaryContentRepository") BinaryContentRepository binaryContentRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.binaryContentRepository = binaryContentRepository;
    }



    @Override
    public MessageDto.Response create(MessageDto.Create messageCreateDTO) {
        // 사용자가 채널에 속해 있는지 검증
        validateUserInChannel(messageCreateDTO.getChannelId(), messageCreateDTO.getAuthorId());

        // 메시지 생성
        Message message = new Message(
                messageCreateDTO.getChannelId(),
                messageCreateDTO.getAuthorId(),
                messageCreateDTO.getContent()
        );
        
        // 첨부파일 처리
        if (messageCreateDTO.getBinaryContents() != null && !messageCreateDTO.getBinaryContents().isEmpty()) {
            for (BinaryContentDto.Create file : messageCreateDTO.getBinaryContents()) {
                try {
                    // 첨부파일 생성
                    BinaryContent attachment = new BinaryContent(
                            file.getFile().getBytes(),
                            file.getContentType(),
                            file.getFileName(),
                            message.getId(),
                            "MESSAGE"
                    );
                    
                    binaryContentRepository.register(attachment);
                    
                    // 메시지에 첨부파일 ID 추가
                    message.addAttachment(attachment.getId());
                } catch (IOException e) {
                    throw new RuntimeException("첨부파일 처리 실패", e);
                }
            }
        }
        
        // 메시지 저장
        messageRepository.register(message);
        
        return MessageMapping.INSTANCE.messageToResponse(message);
    }

    // 유저 채널 속했는지 검증
    private void validateUserInChannel(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + channelId));
        User user = userRepository.findByUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        boolean channelInUser = channel.getUserList().contains(userId);
        boolean userBelongsToChannel = user.getBelongChannels().contains(channelId);
        if (!(channelInUser && userBelongsToChannel)) {
            throw new IllegalArgumentException("해당 유저는 이 채널에 존재하지 않습니다.");
        }
    }

    @Override
    public MessageDto.Response findByMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다"));

        return MessageMapping.INSTANCE.messageToResponse(message);
    }

    @Override
    public List<MessageDto.Response> findAllMessage() {
        return messageRepository.findAll().stream()
                .map(MessageMapping.INSTANCE::messageToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDto.Response> findAllByChannelId(UUID channelId) {
        channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다"));
                
        return messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(MessageMapping.INSTANCE::messageToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MessageDto.Response updateMessage(MessageDto.Update messageUpdateDTO) {
        Message message = messageRepository.findById(messageUpdateDTO.getMessageId())
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다"));

        // 내용 업데이트
        MessageMapping.INSTANCE.updateMessageFromDto(messageUpdateDTO, message);

        // 첨부파일 추가
        if (messageUpdateDTO.getBinaryContents()!= null) {
            for (BinaryContentDto.Create attachmentId : messageUpdateDTO.getBinaryContents()) {
                message.addAttachment(attachmentId.getOwnerId());
            }
        }

        if (messageUpdateDTO.getBinaryContents() != null) {
            List<UUID> removedAttachments = new ArrayList<>();

            for (BinaryContentDto.Create attachmentId : messageUpdateDTO.getBinaryContents()) {
                if (attachmentId != null && message.getAttachmentIds().contains(attachmentId)) {
                    message.removeAttachment(attachmentId.getOwnerId());
                    removedAttachments.add(attachmentId.getOwnerId());
                }
            }

            // 바이너리 콘텐츠 직접 삭제
            for (UUID attachmentId : removedAttachments) {
                binaryContentRepository.delete(attachmentId);
            }
        }

        // 저장 및 응답 반환
        if(! messageRepository.register(message)){
            throw new RuntimeException("저장오류");
        }
        return MessageMapping.INSTANCE.messageToResponse(message);
    }
    
    @Override
    public MessageDto.Response updateMessage(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + messageId));
        message.updateMessage(newContent);
        messageRepository.updateMessage(message);
        return MessageMapping.INSTANCE.messageToResponse(message);
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + messageId));
        
        // 첨부파일 직접 삭제
        if (!message.getAttachmentIds().isEmpty()) {
            for (UUID attachmentId : message.getAttachmentIds()) {
                binaryContentRepository.delete(attachmentId);
            }
        }
        
        return messageRepository.deleteMessage(messageId);
    }
}
