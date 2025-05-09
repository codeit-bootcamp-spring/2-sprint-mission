package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private static final Logger log = LoggerFactory.getLogger(BasicChannelService.class);

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;

    @Transactional
    @Override
    public ChannelDto create(PublicChannelCreateRequest request) {
        log.info("공개 채널 생성 시작. 채널명: '{}', 소유자 ID: '{}'", request.name(), request.ownerId());
        validatePublicChannelRequest(request);

        String channelName = request.name();
        if (!StringUtils.hasText(channelName)) {
            log.warn("공개 채널 생성 실패: 채널명이 제공되지 않았습니다.");
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION, Map.of("missingField", "channelName"));
        }

        if (channelRepository.existsByName(channelName)) {
            log.warn("공개 채널 생성 실패: 채널명 '{}'이(가) 이미 존재합니다.", channelName);
            throw new ChannelException(ErrorCode.CHANNEL_ALREADY_EXISTS, Map.of("channelName", channelName));
        }

        User owner = findUserByIdOrThrow(request.ownerId(), "채널 소유자");

        Channel channel;
        String description = request.description();
        if (StringUtils.hasText(description)) {
            channel = Channel.createPublicChannel(channelName, description);
        } else {
            channel = Channel.createPublicChannel(channelName);
        }

        channel.addParticipant(owner);
        Channel savedChannel = channelRepository.save(channel);
        log.info("공개 채널 생성 완료. 채널 ID: '{}', 채널명: '{}'", savedChannel.getId(),
            savedChannel.getName());
        return channelMapper.toDto(savedChannel);
    }

    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest request) {
        log.info("비공개 채널 생성 시작. 소유자 ID: '{}'", request.ownerId());
        validatePrivateChannelRequest(request);

        String channelName = request.channelName();
        if (!StringUtils.hasText(channelName)) {
            channelName = generatePrivateChannelName(request.ownerId(), request.participantIds());
            log.info("비공개 채널명이 제공되지 않아 자동 생성합니다. 생성된 채널명: '{}'", channelName);
        }

        if (channelRepository.existsByName(channelName)) {
            log.warn("비공개 채널 생성 실패: 채널명 '{}'이(가) 이미 존재합니다.", channelName);
            throw new ChannelException(ErrorCode.CHANNEL_ALREADY_EXISTS, Map.of("channelName", channelName));
        }

        User owner = findUserByIdOrThrow(request.ownerId(), "채널 소유자");

        Channel channel = Channel.createPrivateChannel(channelName);
        channel.addParticipant(owner);

        if (request.participantIds() != null) {
            for (UUID participantId : request.participantIds()) {
                if (!participantId.equals(request.ownerId())) {
                    User participant = findUserByIdOrThrow(participantId, "참여자");
                    channel.addParticipant(participant);
                }
            }
        }
        Channel savedChannel = channelRepository.save(channel);
        log.info("비공개 채널 생성 완료. 채널 ID: '{}', 채널명: '{}'", savedChannel.getId(),
            savedChannel.getName());
        return channelMapper.toDto(savedChannel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        log.info("채널 조회 시도. ID: '{}'", channelId);
        Channel channel = findChannelByIdOrThrow(channelId, "조회할");
        log.info("채널 조회 성공. ID: '{}', 이름: '{}'", channel.getId(), channel.getName());
        try {
            return channelMapper.toDto(channel);
        } catch (Exception e) {
            log.error("채널 ID '{}' DTO 변환 중 오류 발생", channelId, e);
            throw new RuntimeException("채널 정보 조회 중 오류 발생 (Mapper)", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        log.info("사용자 ID '{}'의 모든 채널 조회 시도.", userId);
        if (!userRepository.existsById(userId)) {
            log.warn("사용자 ID '{}'에 해당하는 사용자를 찾을 수 없어 채널 조회를 중단합니다.", userId);
            throw new UserNotFoundException(userId.toString());
        }
        List<Channel> channelList = channelRepository.findChannelsByUserId(userId);
        log.info("사용자 ID '{}'의 채널 {}개를 조회했습니다.", userId, channelList.size());
        return channelMapper.toDto(channelList);
    }

    @Transactional
    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
        log.info("채널 업데이트 시작. ID: '{}'", channelId);
        Channel channel = findChannelByIdOrThrow(channelId, "업데이트할");

        if (channel.isPrivate()) {
            log.warn("채널 업데이트 실패: 비공개 채널(ID: '{}')은 수정할 수 없습니다.", channelId);
            throw new ChannelException(ErrorCode.PRIVATE_CHANNEL_UPDATE_DENIED, Map.of("channelId", channelId.toString()));
        }

        String oldName = channel.getName();
        String oldDescription = channel.getDescription();

        boolean updated = channel.update(request.newName(), request.newDescription());

        if (updated) {
            Channel updatedChannel = channelRepository.save(channel);
            log.info("채널 업데이트 완료. ID: '{}'. 이전 이름: '{}', 새 이름: '{}'. 이전 설명: '{}', 새 설명: '{}'",
                channelId, oldName, updatedChannel.getName(), oldDescription,
                updatedChannel.getDescription());
            return channelMapper.toDto(updatedChannel);
        } else {
            log.info("채널 업데이트 변경 사항 없음. ID: '{}'", channelId);
            return channelMapper.toDto(channel);
        }
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        log.info("채널 삭제 시작. ID: '{}'", channelId);
        if (!channelRepository.existsById(channelId)) {
            log.warn("채널 삭제 실패: ID '{}'에 해당하는 채널을 찾을 수 없습니다.", channelId);
            throw new ChannelException(ErrorCode.CHANNEL_NOT_FOUND, Map.of("channelId", channelId.toString()));
        }
        channelRepository.deleteById(channelId);
        log.info("채널 삭제 완료. ID: '{}'", channelId);
    }

    private User findUserByIdOrThrow(UUID userId, String userType) {
        return userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("{} ID '{}'에 해당하는 사용자를 찾을 수 없습니다.", userType, userId);
                return new UserNotFoundException(userId.toString());
            });
    }

    private Channel findChannelByIdOrThrow(UUID channelId, String actionType) {
        return channelRepository.findById(channelId)
            .orElseThrow(() -> {
                log.warn("{} 채널 ID '{}'에 해당하는 채널을 찾을 수 없습니다.", actionType, channelId);
                return new ChannelException(ErrorCode.CHANNEL_NOT_FOUND, Map.of("channelId", channelId.toString(), "actionType", actionType));
            });
    }

    private void validatePublicChannelRequest(PublicChannelCreateRequest request) {
        if (request == null || request.ownerId() == null || !StringUtils.hasText(request.name())) {
            log.warn("공개 채널 생성 요청 유효성 검사 실패: {}", request);
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION, Map.of("validationError", "Public channel request invalid", "requestDetails", request == null ? "null" : request.toString()));
        }
    }

    private void validatePrivateChannelRequest(PrivateChannelCreateRequest request) {
        if (request == null || request.ownerId() == null) {
            log.warn("비공개 채널 생성 요청 유효성 검사 실패: {}", request);
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION, Map.of("validationError", "Private channel request invalid", "requestDetails", request == null ? "null" : request.toString()));
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