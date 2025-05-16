package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ParticipantUserNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;

    @Override
    @Transactional
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
        log.info("비공개 채널 생성 요청 - 참여자 수: {}", request.participantIds().size());

        Channel channel = new Channel("PRIVATE CHANNEL", "비공개 채널입니다.", ChannelType.PRIVATE);
        channelRepository.save(channel);

        request.participantIds().forEach(userId -> {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("참여자 조회 실패 - userId: {}", userId);
                    return new ParticipantUserNotFoundException(Map.of("userId", userId));
                });

            ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());
            readStatusRepository.save(readStatus);
            log.debug("참여자 등록 완료 - userId: {}", userId);
        });

        log.info("비공개 채널 생성 완료 - channelId: {}", channel.getId());
        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional
    public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
        log.info("공개 채널 생성 요청 - name: {}", request.name());

        Channel channel = new Channel(request.name(), request.description(), ChannelType.PUBLIC);
        channelRepository.save(channel);

        log.info("공개 채널 생성 완료 - channelId: {}", channel.getId());
        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelDto getChannelById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new ChannelNotFoundException(Map.of("channelId", channelId)));

        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Channel> getChannelsByName(String name) {
        return channelRepository.findAll().stream()
            .filter(channel -> channel.getName().equals(name))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelDto> findAllByUserId(UUID userId) {
        log.info("참여 채널 조회 요청 - userId: {}", userId);
        return channelRepository.findAll().stream()
            .filter(channel -> canUserAccessChannel(channel, userId))
            .map(channelMapper::toDto)
            .toList();
    }

    private boolean canUserAccessChannel(Channel channel, UUID userId) {
        if (!channel.isPrivate()) {
            return true;
        }
        // private 채널일 경우 userId가 참여한 경우에만 접근 가능
        return readStatusRepository.findAll().stream()
            .anyMatch(status -> status.getChannel().getId().equals(channel.getId())
                && status.getUser().getId().equals(userId));
    }

    private ChannelResponse convertToChannelResponse(Channel channel) {
        Instant latestMessageTime = getLatestMessageTime(channel.getId());
        List<UUID> participantUserIds = getParticipantUserIds(channel.getId());

        return new ChannelResponse(
            channel.getId(),
            channel.getName(),
            channel.getDescription(),
            latestMessageTime,
            participantUserIds,
            channel.getType().name(),
            channel.getCreatedAt(),
            channel.getUpdatedAt()
        );
    }

    private Instant getLatestMessageTime(UUID channelId) {
        return messageRepository.findAll().stream()
            .filter(message -> message != null
                && message.getChannel() != null
                && message.getChannel().getId() != null
                && message.getCreatedAt() != null
                && message.getChannel().getId().equals(channelId))
            .map(Message::getCreatedAt)
            .max(Instant::compareTo)
            .orElse(null);
    }

    private List<UUID> getParticipantUserIds(UUID channelId) {
        return readStatusRepository.findAll().stream()
            .filter(status -> status.getChannel().getId().equals(channelId))
            .map(status -> status.getUser().getId())
            .toList();
    }

    @Override
    @Transactional
    public ChannelDto updateChannel(UUID channelId, UpdateChannelRequest request) {
        log.info("채널 수정 요청 - channelId: {}", channelId);

        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(
                () -> new ChannelNotFoundException(Map.of("channelId", channelId)));

        if (channel.isPrivate()) {
            log.warn("PRIVATE 채널은 수정 불가 - channelId: {}", channel.getId());
            throw new PrivateChannelUpdateException(Map.of("channelId", channelId));
        }

        channel.update(request.newName(), request.newDescription());
        log.info("채널 수정 완료 - channelId: {}", channel.getId());
//            channelRepository.save(channel);
        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional
    public void deleteChannel(UUID channelId) {
        log.info("채널 삭제 요청 - channelId: {}", channelId);

        if (!channelRepository.existsById(channelId)) {
            log.warn("채널 조회 실패 - 존재하지 않는 채널 ID: {}", channelId);
            throw new ChannelNotFoundException(Map.of("channelId", channelId));
        }

        messageRepository.deleteAllByChannelId(channelId);
        log.debug("메시지 전체 삭제 완료 - channelId: {}", channelId);

        readStatusRepository.deleteAllByChannelId(channelId);
        log.debug("참여자 상태 전체 삭제 완료 - channelId: {}", channelId);

        channelRepository.deleteById(channelId);
        log.info("채널 삭제 완료 - channelId: {}", channelId);
    }
}
