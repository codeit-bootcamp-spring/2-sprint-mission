package com.sprint.mission.discodeit.basic.serviceimpl; // 서비스 구현체 패키지

import com.sprint.mission.discodeit.UpdateOperation;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ForbiddenException;
import com.sprint.mission.discodeit.exception.InvalidRequestException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapping.MessageMapping;

import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentService binaryContentService;

    @Override
    public MessageDto.Response create(MessageDto.Create messageCreateDTO) {
        validateUserInChannel(messageCreateDTO.getChannelId(), messageCreateDTO.getAuthorId());

        Message message = new Message(
                messageCreateDTO.getChannelId(),
                messageCreateDTO.getAuthorId(),
                messageCreateDTO.getContent()
        );

        if (messageCreateDTO.getBinaryContents() != null && !messageCreateDTO.getBinaryContents().isEmpty()) {
            validateAttachmentIdsExist(messageCreateDTO.getBinaryContents());
            for (UUID fileId : messageCreateDTO.getBinaryContents()) {
                message.addAttachment(fileId);
            }
        }

        try {
            if (!messageRepository.register(message)) {
                throw new RuntimeException("메시지 저장에 실패했습니다."); 
            }
            return MessageMapping.INSTANCE.messageToResponse(message);
        } catch (Exception e) {
            throw new RuntimeException("메시지 저장 중 서버 오류가 발생했습니다.", e);
        }
    }

    private void validateUserInChannel(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId)); // 404
        User user = userRepository.findByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId)); // 404

        boolean userInChannelList = channel.getUserList().contains(userId);
        boolean channelInUserBelongs = user.getBelongChannels().contains(channelId);

        if (!(userInChannelList && channelInUserBelongs)) {
            throw new ForbiddenException("해당 채널에 대한 접근 권한이 없습니다.");
        }
    }

    private void validateAttachmentIdsExist(List<UUID> attachmentIds) {
        if (attachmentIds == null || attachmentIds.isEmpty()) {
            return;
        }
        List<BinaryContentDto.Summary> existingSummaries = binaryContentService.findBinaryContentSummariesByIds(attachmentIds);

        if (existingSummaries.size() != attachmentIds.size()) {
            List<UUID> existingIds = existingSummaries.stream()
                    .map(summary -> UUID.fromString(String.valueOf(summary.getId())))
                    .toList();
            List<UUID> nonExistentIds = new ArrayList<>(attachmentIds);
            nonExistentIds.removeAll(existingIds);
            throw new InvalidRequestException("attachments", "존재하지 않는 첨부파일 ID가 포함되어 있습니다: " + nonExistentIds);
        }
    }

    @Override
    public MessageDto.Response findByMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId)); // 404

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
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId)); // 404

        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        return messages.stream()
                .map(MessageMapping.INSTANCE::messageToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MessageDto.Response updateMessage(MessageDto.Update messageUpdateDTO) {
        Message message = messageRepository.findById(messageUpdateDTO.getMessageId())
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageUpdateDTO.getMessageId()));

        boolean updated = false;

        if (messageUpdateDTO.getContent() != null && !messageUpdateDTO.getContent().equals(message.getMessage())) {
            message.updateMessage(messageUpdateDTO.getContent());
            updated = true;
        }

        if (messageUpdateDTO.getBinaryContents() != null && !messageUpdateDTO.getBinaryContents().isEmpty()) {
            UpdateOperation operation = messageUpdateDTO.getOperation();
            List<UUID> binaryContentIds = messageUpdateDTO.getBinaryContents();

            if (operation == UpdateOperation.add) {
                validateAttachmentIdsExist(binaryContentIds); // 추가할 ID 유효성 검증 (400)

                for (UUID binaryContentId : binaryContentIds) {
                    if (message.getAttachmentIds().add(binaryContentId)) {
                        updated = true;
                    }
                }
                if (updated) message.setUpdateAt();

            } else if (operation == UpdateOperation.remove) {
                List<UUID> removedAttachments = new ArrayList<>();
                boolean attachmentsChanged = false;
                for (UUID binaryContentId : binaryContentIds) {
                    if (binaryContentId != null && message.getAttachmentIds().contains(binaryContentId)) {
                        message.removeAttachment(binaryContentId);
                        removedAttachments.add(binaryContentId);
                        attachmentsChanged = true;
                    }
                }

                if (attachmentsChanged) {
                    updated = true;
                    message.setUpdateAt();
                }

                if (!removedAttachments.isEmpty()) {
                    try {

                        binaryContentService.deleteBinaryContentsByIds(removedAttachments);
                    } catch (Exception e) {
                        throw new RuntimeException("첨부파일 삭제 중 서버 오류가 발생했습니다.", e);
                    }
                }
            } else if (operation != null) {
                throw new InvalidRequestException("operation", "지원되지 않는 첨부파일 작업입니다: " + operation);
            }
        }

        if (updated) {
            try {
                if (!messageRepository.updateMessage(message)) {
                    throw new RuntimeException("메시지 업데이트에 실패했습니다."); // 500 유발
                }
            } catch (Exception e) { // Repository RuntimeException 등
                throw new RuntimeException("메시지 업데이트 중 서버 오류가 발생했습니다.", e); // 500 유발
            }
        }

        return MessageMapping.INSTANCE.messageToResponse(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId)); // 404

        Set<UUID> attachmentIds = message.getAttachmentIds();
        if (attachmentIds != null && !attachmentIds.isEmpty()) {
            List<UUID> idsToDelete = new ArrayList<>(attachmentIds);
            try {

                binaryContentService.deleteBinaryContentsByIds(idsToDelete);
            } catch (Exception e) {
                throw new RuntimeException("연결된 첨부파일 삭제 중 서버 오류가 발생했습니다.", e);
            }
        }

        try {
            if (!messageRepository.deleteMessage(messageId)) {
                // 삭제 대상이 없음 (findById 이후 동시성 문제 등) -> 404 Not Found
                throw new ResourceNotFoundException("Message", "id", messageId + " (삭제 시점 확인)");
            }
        } catch (Exception e) { // Repository RuntimeException 등
            throw new RuntimeException("메시지 삭제 중 서버 오류가 발생했습니다.", e); // 500 유발
        }
    }
}