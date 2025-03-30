package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DataConflictException;
import com.sprint.mission.discodeit.exception.InvalidRequestException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapping.ReadStatusMapping;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.MessageRepository;
import com.sprint.mission.discodeit.service.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service("basicReadStatusService")
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    @Override
    public ReadStatusDto.ResponseReadStatus create(ReadStatusDto.Create readStatusDto) {
        validateUserAndChannel(readStatusDto.getUserId(), readStatusDto.getChannelId());

        readStatusRepository.findByUserIdAndChannelId(readStatusDto.getUserId(), readStatusDto.getChannelId())
                .ifPresent(existing -> {
                    throw new DataConflictException("ReceptionStatus", "userId/channelId",
                            readStatusDto.getUserId() + "/" + readStatusDto.getChannelId());
                });

       ReadStatus readStatus =ReadStatusMapping.INSTANCE.createDtoToEntity(readStatusDto);

        boolean success = readStatusRepository.register(readStatus);
        if (!success) {
            throw new InvalidRequestException("reception_status", "수신 상태 저장에 실패했습니다");
        }

        return ReadStatusMapping.INSTANCE.toResponseDto(readStatus);
    }

    @Override
    public ReadStatusDto.ResponseReadStatus find(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReadStatus", "id", id));
        
        return ReadStatusMapping.INSTANCE.toResponseDto(readStatus);
    }

    @Override
    public List<ReadStatusDto.ResponseReadStatus> findAllByUserId(UUID userId) {
        // 사용자 존재 확인
        userRepository.findByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatusMapping.INSTANCE::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReadStatusDto.ResponseReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId) {
        validateUserAndChannel(userId, channelId);
        
        ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(userId, channelId)
                .orElseThrow(() -> new ResourceNotFoundException("ReadStatus", "userId/channelId", 
                        userId + "/" + channelId));
        
        return ReadStatusMapping.INSTANCE.toResponseDto(readStatus);
    }

    @Override
    public ReadStatusDto.ResponseReadStatus update(UUID readStatusId, ReadStatusDto.Update updateDto) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new ResourceNotFoundException("ReceptionStatus", "id", readStatusId));

        messageRepository.findById(updateDto.getLastReadMessageId())
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", updateDto.getLastReadMessageId()));

        readStatus.updateLastReadMessage(updateDto.getLastReadMessageId());

        boolean success = readStatusRepository.updateReadStatus(readStatus);
        if (!success) {
            throw new InvalidRequestException("reception_status", "수신 상태 업데이트에 실패했습니다");
        }
        return ReadStatusMapping.INSTANCE.toResponseDto(readStatus);
    }

    @Override
    public boolean delete(UUID id) {

        return readStatusRepository.deleteReadStatus(id);
    }
    
    @Override
    public boolean deleteAllByUserId(UUID userId) {
        return readStatusRepository.deleteAllByUserId(userId);
    }
    
    @Override
    public boolean deleteAllByChannelId(UUID channelId) {
        // 채널 존재 확인
        channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId));
        
        return readStatusRepository.deleteAllByChannelId(channelId);
    }
    
    @Override
    public Integer getUnreadMessageCount(UUID userId, UUID channelId) {
        validateUserAndChannel(userId, channelId);

        ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(userId, channelId)
                .orElse(null);
        
        //없을 시 메세지 수 반환
        if (readStatus == null) {
            return (int) messageRepository.findAll()
                    .stream()
                    .filter(m -> m.getChannelId().equals(channelId))
                    .count();
        }

        // 마지막으로 읽은 메시지 ID 이후의 메시지 수 조회
        List<Message> allMessages = messageRepository.findAll()
                .stream()
                .filter(msg -> msg.getChannelId().equals(channelId))
                .filter(msg -> msg.getCreatedAt().isAfter(readStatus.getLastReadAt()))
                .toList();

        return allMessages.size();
    }

    // 검증 로직
    private void validateUserAndChannel(UUID userId, UUID channelId) {
        userRepository.findByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId));
    }
}
