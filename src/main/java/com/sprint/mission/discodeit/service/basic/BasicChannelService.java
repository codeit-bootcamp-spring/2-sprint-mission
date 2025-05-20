package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelModifyException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    public ChannelDto createPublicChannel(PublicChannelCreateRequest publicChannelCreateRequest) {
        log.info("공개 채널 생성 진행: channelName = {}", publicChannelCreateRequest.name());
        Channel channel = new Channel(publicChannelCreateRequest.name(),
            publicChannelCreateRequest.description(), ChannelType.PUBLIC);
        channelRepository.save(channel);
        log.info("공개 채널 생성 완료: channelId = {}", channel.getId());
        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional
    public ChannelDto createPrivateChannel(
        PrivateChannelCreateRequest privateChannelCreateRequest) {
        log.info("비공개 채널 생성 진행");
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        channelRepository.save(channel);
        privateChannelCreateRequest.participantIds().forEach(userId -> {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("비공개 채널 생성 중 대상 사용자를 찾을 수 없음:  userId = {}", userId);
                    return UserNotFoundException.forId(userId.toString());
                });
            ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());
            log.info("비공개 채널 생성 중 읽음 상태 저장 완료: userId = {}, readStatus = {}", userId, readStatus);
            readStatusRepository.save(readStatus);
        });
        log.info("비공개 채널 생성 완료: channelId = {}", channel.getId());
        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelDto> findAllByUserId(UUID userId) {
        return channelRepository.findAllByUser(userId).stream()
            .map(channelMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChannelDto updateChannel(UUID channelId,
        PublicChannelUpdateRequest channelUpdateParamDto) {
        log.info("채널 수정 진행: channelId = {}", channelId);
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(
                () -> {
                    log.error("채널 수정 중 해당 채널을 찾을 수 없음: channelId = {}", channelId);
                    return ChannelNotFoundException.forId(channelId.toString());
                });

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            log.warn("채널 수정 중 비공개 채널 수정 시도: channelId = {}", channelId);
            throw PrivateChannelModifyException.forId(channelId.toString());
        }

        channel.updateChannelName(channelUpdateParamDto.newName());
        channel.updateChannelDescription(channelUpdateParamDto.newDescription());

        log.info("채널 수정 완료:  channelId = {}", channelId);
        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional
    public void deleteChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> {
                log.error("채널 삭제 중 채널을 찾을 수 없음:  channelId = {}", channelId);
                return ChannelNotFoundException.forId(channelId.toString());
            });

        log.info("채널 삭제 중 메세지 삭제 진행");
        messageRepository.deleteAll(messageRepository.findByChannelId(channelId));
        log.info("채널 삭제 중 메세지 삭제 완료");

        log.info("채널 삭제 중 읽음 상태 삭제 진행");
        readStatusRepository.deleteAll(readStatusRepository.findByChannelId(channelId));
        log.info("채널 삭제 중 읽음 상태 삭제 완료");

        channelRepository.delete(channel);
        log.info("채널 삭제 완료: channelId = {}", channelId);
    }
}
