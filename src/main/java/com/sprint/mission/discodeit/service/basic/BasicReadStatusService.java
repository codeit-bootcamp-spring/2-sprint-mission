package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapping.ReadStatusMapping;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.MessageRepository;
import com.sprint.mission.discodeit.service.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("basicReadStatusService")
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public BasicReadStatusService(
            @Qualifier("basicReadStatusRepository") ReadStatusRepository readStatusRepository,
            @Qualifier("basicUserRepository") UserRepository userRepository,
            @Qualifier("basicChannelRepository") ChannelRepository channelRepository,
            @Qualifier("basicMessageRepository") MessageRepository messageRepository) {
        this.readStatusRepository = readStatusRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public ReadStatusDto.ResponseReadStatus create(ReadStatusDto.Create readStatusDto) {
        // 연관 엔티티 존재 검증
        validateUserAndChannel(readStatusDto.getUserId(), readStatusDto.getChannelId());
        
        // 메시지 존재 확인
        if (readStatusDto.getLastReadMessageId() != null) {
            messageRepository.findById(readStatusDto.getLastReadMessageId())
                    .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다"));
        }
        
        // 중복 확인
        readStatusRepository.findByUserIdAndChannelId(readStatusDto.getUserId(), readStatusDto.getChannelId())
                .ifPresent(existing -> {
                    throw new IllegalStateException("이미 동일한 사용자와 채널에 대한 읽음 상태가 존재합니다");
                });

        // ReadStatus 생성
        ReadStatus readStatus = ReadStatusMapping.INSTANCE.createDtoToEntity(readStatusDto);
        
        // 저장
        boolean success = readStatusRepository.register(readStatus);
        if (!success) {
            throw new RuntimeException("저장실패");
        }

        return ReadStatusMapping.INSTANCE.toResponseDto(readStatus);
    }

    @Override
    public ReadStatusDto.ResponseReadStatus find(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("읽음 상태를 찾을 수 없습니다: " + id));
        
        return ReadStatusMapping.INSTANCE.toResponseDto(readStatus);
    }

    @Override
    public List<ReadStatusDto.ResponseReadStatus> findAllByUserId(UUID userId) {
        // 사용자 존재 확인
        userRepository.findByUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatusMapping.INSTANCE::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReadStatusDto.ResponseReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId) {
        validateUserAndChannel(userId, channelId);
        
        ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(userId, channelId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자와 채널에 대한 읽음 상태를 찾을 수 없습니다"));
        
        return ReadStatusMapping.INSTANCE.toResponseDto(readStatus);
    }

    @Override
    public ReadStatusDto.ResponseReadStatus update(ReadStatusDto.Update updateDto) {
        // 읽음 상태 존재 확인
        ReadStatus readStatus = readStatusRepository.findById(updateDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("읽음 상태를 찾을 수 없습니다"));

        messageRepository.findById(updateDto.getLastReadMessageId())
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다"));
        
        // 읽음 상태 업데이트
        ReadStatusMapping.INSTANCE.updateEntityFromDto(updateDto, readStatus);
        
        boolean success = readStatusRepository.updateReadStatus(readStatus);
        if (!success) {
            throw new RuntimeException("읽음 상태 업데이트에 실패했습니다");
        }
        
        return ReadStatusMapping.INSTANCE.toResponseDto(readStatus);
    }

    @Override
    public boolean delete(UUID id) {
        readStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("읽음 상태를 찾을 수 없습니다"));
        
        return readStatusRepository.deleteReadStatus(id);
    }
    
    @Override
    public boolean deleteAllByUserId(UUID userId) {
        // 사용자 존재 확인
        userRepository.findByUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        
        return readStatusRepository.deleteAllByUserId(userId);
    }
    
    @Override
    public boolean deleteAllByChannelId(UUID channelId) {
        // 채널 존재 확인
        channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + channelId));
        
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
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다: " + channelId));
    }
} 