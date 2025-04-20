package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import com.sprint.mission.discodeit.dto.file.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.handler.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;
    private final BinaryContentService binaryContentService;


    @Override
    @Transactional
    public Message createMessage(CreateMessageRequest request, List<MultipartFile> attachments) {
        if (request.authorId() == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }
        if (request.channelId() == null) {
            throw new IllegalArgumentException("채널 ID는 null일 수 없습니다.");
        }

        User author = userRepository.findById(request.authorId())
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + request.authorId()));

        Channel channel = channelRepository.findById(request.channelId())
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채널입니다: " + request.channelId()));

        List<BinaryContent> attachmentIds = new ArrayList<>();
        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile file : attachments) {
                try {
                    CreateBinaryContentRequest binaryRequest = new CreateBinaryContentRequest(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                    );

                    BinaryContentDto dto = binaryContentService.create(binaryRequest);
                    BinaryContent content = binaryContentRepository.findById(dto.id())
                        .orElseThrow(() -> new IllegalStateException("저장된 파일을 다시 찾을 수 없습니다."));
                    attachmentIds.add(content);
                } catch (Exception e) {
                    throw new RuntimeException("첨부 파일 저장 실패", e);
                }
            }
        }

        Message message = new Message(author, channel, request.content(), attachmentIds);
        return messageRepository.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto getMessageById(UUID messageId) {
        return messageRepository.findById(messageId)
            .map(messageMapper::toDto)
            .orElseThrow(() -> new MessageNotFoundException(messageId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다: " + channelId));

        Slice<Message> slice = messageRepository.findAllByChannelOrderByCreatedAtDesc(channel,
            pageable);
        Slice<MessageDto> messageDtos = slice.map(messageMapper::toDto);
        return pageResponseMapper.fromSlice(messageDtos);
    }

    @Override
    @Transactional
    public void updateMessage(UpdateMessageRequest request) {
        messageRepository.findById(request.messageId()).ifPresent(message -> {
            message.update(request.newContent(), message.getAttachments());
            messageRepository.save(message);
        });
    }

    @Override
    @Transactional
    public void deleteMessage(UUID messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.getAttachments().forEach(binaryContentRepository::delete);
            messageRepository.deleteById(messageId);
        });
    }
}
