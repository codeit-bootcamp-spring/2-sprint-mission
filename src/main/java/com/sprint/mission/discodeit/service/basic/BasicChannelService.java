package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private static final Logger log = LoggerFactory.getLogger(BasicChannelService.class);

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;

    @Transactional
    @Override
    public Channel create(PublicChannelCreateRequest request) {
        validatePublicChannelRequest(request);

        String channelName = request.name();
        if (!StringUtils.hasText(channelName)) {
            throw new IllegalArgumentException("Public channel name must be provided.");
        }

        if (channelRepository.findByName(channelName)) {
            throw new RuntimeException("Channel with name '" + channelName + "' already exists.");
        }

        User owner = findUserByIdOrThrow(request.ownerId());

        Channel channel;
        String description = request.description();
        if (StringUtils.hasText(description)) {
            channel = Channel.createPublicChannel(channelName, description);
        } else {
            channel = Channel.createPublicChannel(channelName);
        }

        channel.addParticipant(owner);

        return channelRepository.save(channel);
    }

    @Transactional
    @Override
    public Channel create(PrivateChannelCreateRequest request) {
        validatePrivateChannelRequest(request);

        String channelName = request.channelName();
        if (!StringUtils.hasText(channelName)) {
            throw new IllegalArgumentException("Private channel name must be provided.");
        }

        if (channelRepository.findByName(channelName)) {
            throw new RuntimeException("Channel with name '" + channelName + "' already exists.");
        }

        User owner = findUserByIdOrThrow(request.ownerId());

        Channel channel = Channel.createPrivateChannel(channelName);
        channel.addParticipant(owner);

        if (request.participantIds() != null) {
            for (UUID participantId : request.participantIds()) {
                if (!participantId.equals(request.ownerId())) {
                    User participant = findUserByIdOrThrow(participantId);
                    channel.addParticipant(participant);
                }
            }
        }

        return channelRepository.save(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        log.debug("채널 조회 요청: ID={}", channelId);
        Channel channel = findChannelByIdOrThrow(channelId);

        try {
            return channelMapper.toDto(channel);
        } catch (Exception e) {
            throw new RuntimeException("채널 정보 조회 중 오류 발생 (Mapper)", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        User user = findUserByIdOrThrow(userId);

        return user.getUserChannels().stream()
            .map(userChannel -> {
                Channel channel = userChannel.getChannel();
                try {
                    return channelMapper.toDto(channel);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            })
            .filter(dto -> dto != null)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Channel update(UUID channelId, PublicChannelUpdateRequest request) {
        log.info("채널 업데이트 요청: ID={}, Data={}", channelId, request);
        Channel channel = findChannelByIdOrThrow(channelId);

        if (channel.isPrivate()) {
            throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다.");
        }

        boolean updated = channel.update(request.newName(), request.newDescription());

        if (updated) {
            Channel updatedChannel = channelRepository.save(channel);
            log.info("채널 업데이트 완료: ID={}", updatedChannel.getId());
            return updatedChannel;
        } else {
            log.info("채널 업데이트 내용 없음: ID={}", channelId);
            return channel;
        }
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        log.info("채널 삭제 요청: ID={}", channelId);

        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("채널이 존재 하지 않음");
        }

        channelRepository.deleteById(channelId);
        log.info("채널 삭제 완료: ID={}", channelId);
    }

    private User findUserByIdOrThrow(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("유저가 존재 하지 않음"));
    }

    private Channel findChannelByIdOrThrow(UUID channelId) {
        return channelRepository.findById(channelId)
            .orElseThrow(() -> new NoSuchElementException("채널이 존재 하지 않음"));
    }

    private void validatePublicChannelRequest(PublicChannelCreateRequest request) {
        if (request == null || request.ownerId() == null || !StringUtils.hasText(request.name())) {
            throw new IllegalArgumentException("공개 채널 생성 요청 정보가 유효하지 않습니다.");
        }
    }

    private void validatePrivateChannelRequest(PrivateChannelCreateRequest request) {
        if (request == null || request.ownerId() == null) {
            throw new IllegalArgumentException("비공개 채널 생성 요청 정보가 유효하지 않습니다.");
        }
    }

    private String generatePrivateChannelName(UUID ownerId, List<UUID> participantIds) {
        List<UUID> ids = new ArrayList<>();
        ids.add(ownerId);
        if (participantIds != null) {
            ids.addAll(participantIds);
        }
        return "DM-" + ids.stream().distinct().sorted()
            .map(UUID::toString).collect(Collectors.joining("-"));
    }
}