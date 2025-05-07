package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResult;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.user.UserResult;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_MESSAGE_NOT_FOUND;
import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentStorageService binaryContentStorageService;

    @Transactional
    @Override
    public MessageResult create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> files) {
        Channel channel = channelRepository.findById(messageCreateRequest.channelId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 채널이 존재하지 않습니다."));

        // TODO: 5/7/25 분리 필요
        List<BinaryContent> attachments = binaryContentStorageService.createBinaryContents(files);

        User user = userRepository.findById(messageCreateRequest.authorId())
                .orElseThrow(() -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent()));
        Message message = messageRepository.save(new Message(channel, user, messageCreateRequest.content(), attachments));

        // TODO: 5/7/25 매핑 관련 클래스 뽑기
        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));
        return MessageResult.fromEntity(message, UserResult.fromEntity(user, userStatus.isOnline(Instant.now())));
    }

    @Transactional(readOnly = true)
    @Override
    public MessageResult getById(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));
        User user = message.getUser();

        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));
        return MessageResult.fromEntity(message, UserResult.fromEntity(user, userStatus.isOnline(Instant.now())));
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<MessageResult> getAllByChannelId(UUID channelId, Pageable pageable) {
        Slice<Message> slices = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId, pageable);
        slices.getContent();

        // TODO: 5/7/25 분리 및 페이징 테스트 필요
        return PageResponseMapper.fromSlice(slices, message -> {
            UserStatus userStatus = userStatusRepository.findByUser_Id(message.getUser().getId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

            return MessageResult.fromEntity(
                    message,
                    UserResult.fromEntity(message.getUser(), userStatus.isOnline(Instant.now()))
            );
        });
    }

    @Transactional
    @Override
    public MessageResult updateContext(UUID id, String context) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));
        message.updateContext(context);
        Message savedMessage = messageRepository.save(message);

        User user = savedMessage.getUser();

        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));
        return MessageResult.fromEntity(message, UserResult.fromEntity(user, userStatus.isOnline(Instant.now()))); // TODO: 5/7/25 이런 중복되는 로직, Mapper로 뽑자
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        List<UUID> attachmentIds = message.getAttachments()
                .stream()
                .map(BinaryContent::getId)
                .toList();

        // TODO: 5/7/25 배치 처리필요
        binaryContentStorageService.deleteBinaryContentsBatch(attachmentIds);

        messageRepository.deleteById(id);
    }

}
