package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;

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
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Transactional
    @Override
    public ChannelDto create(PublicChannelCreateRequest request) {
        log.info("공개 채널 생성 시작. 채널명: '{}'", request.name());
        validatePublicChannelRequest(request);

        String channelName = request.name();
        String description = request.description();

        if (!StringUtils.hasText(channelName)) {
            log.warn("공개 채널 생성 실패: 채널명이 제공되지 않았습니다.");
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION,
                Map.of("missingField", "channelName"));
        }

        if (channelRepository.existsByName(channelName)) {
            log.warn("공개 채널 생성 실패: 채널명 '{}'이(가) 이미 존재합니다.", channelName);
            throw new ChannelException(ErrorCode.CHANNEL_ALREADY_EXISTS,
                Map.of("channelName", channelName));
        }

        Channel channel = new Channel(ChannelType.PUBLIC, channelName, description);

        Channel savedChannel = channelRepository.save(channel);
        log.info("공개 채널 생성 완료. 채널 ID: '{}', 채널명: '{}'", savedChannel.getId(),
            savedChannel.getName());
        return channelMapper.toDto(savedChannel);
    }

    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest request) {
        log.info("비공개 채널 생성 시작. 요청된 참여자 수: {}", request.participantIds() != null ? request.participantIds().size() : 0);
        validatePrivateChannelRequest(request);

        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(channel);

        log.info("비공개 채널 기본 정보 저장 완료. 채널 ID: '{}'", channel.getId());

        if (CollectionUtils.isEmpty(request.participantIds())) {
            log.warn("비공개 채널 생성 요청에 참여자 ID가 없습니다. 채널 ID: {}", channel.getId());
        }

        List<User> participants = userRepository.findAllById(request.participantIds());
        if (participants.size() != request.participantIds().size()) {
            log.warn("요청된 참여자 ID 중 일부를 찾을 수 없습니다. 요청된 ID 수: {}, 찾은 사용자 수: {}", request.participantIds().size(), participants.size());
        }

        List<ReadStatus> readStatuses = participants.stream()
            .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
            .collect(Collectors.toList());
        
        if (!readStatuses.isEmpty()) {
            readStatusRepository.saveAll(readStatuses);
            log.info("{}개의 ReadStatus 저장 완료. 채널 ID: '{}'", readStatuses.size(), channel.getId());
        } else {
            log.info("저장할 ReadStatus 없음. 채널 ID: '{}'", channel.getId());
        }

        log.info("비공개 채널 생성 및 ReadStatus 설정 완료. 채널 ID: '{}'", channel.getId());
        return channelMapper.toDto(channel);
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
        log.info("사용자 ID '{}'의 모든 채널 조회 시도 (ReadStatus 기반).", userId);
        if (!userRepository.existsById(userId)) {
            log.warn("사용자 ID '{}'에 해당하는 사용자를 찾을 수 없어 채널 조회를 중단합니다.", userId);
            throw new UserNotFoundException(userId.toString());
        }

        List<UUID> subscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
            .map(ReadStatus::getChannel)
            .map(Channel::getId)
            .distinct()
            .collect(Collectors.toList());
        log.info("사용자 ID '{}'가 ReadStatus를 가진 채널 ID 목록: {}", userId, subscribedChannelIds);

        List<Channel> channelList;
        if (subscribedChannelIds.isEmpty()) {
            channelList = channelRepository.findAllByType(ChannelType.PUBLIC);
            log.info("사용자 ID '{}'는 구독한 채널이 없어 공개 채널만 조회. 공개 채널 수: {}", userId, channelList.size());
        } else {
            List<Channel> publicChannels = channelRepository.findAllByType(ChannelType.PUBLIC);
            List<Channel> subscribedPrivateChannels = channelRepository.findAllByIdInAndType(subscribedChannelIds, ChannelType.PRIVATE);
            
            channelList = new ArrayList<>(publicChannels);
            subscribedPrivateChannels.forEach(spc -> {
                if (publicChannels.stream().noneMatch(pc -> pc.getId().equals(spc.getId()))) {
                    channelList.add(spc);
                }
            });

            log.info("사용자 ID '{}'의 공개 채널 및 구독 채널 조회. 총 {}개 후보.", userId, channelList.size());
        }
        
        return channelList.stream()
                          .map(channelMapper::toDto)
                          .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
        log.info("채널 업데이트 시작. ID: '{}'", channelId);
        Channel channel = findChannelByIdOrThrow(channelId, "업데이트할");

        if (channel.isPrivate()) {
            log.warn("채널 업데이트 실패: 비공개 채널(ID: '{}')은 수정할 수 없습니다.", channelId);
            throw new ChannelException(ErrorCode.PRIVATE_CHANNEL_UPDATE_DENIED,
                Map.of("channelId", channelId.toString()));
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
            throw new ChannelException(ErrorCode.CHANNEL_NOT_FOUND,
                Map.of("channelId", channelId.toString()));
        }

        log.info("채널 ID '{}' 관련 메시지 삭제 시작", channelId);
        messageRepository.deleteAllByChannelId(channelId);
        log.info("채널 ID '{}' 관련 메시지 삭제 완료", channelId);
        
        log.info("채널 ID '{}' 관련 ReadStatus 삭제 시작", channelId);
        readStatusRepository.deleteAllByChannelId(channelId);
        log.info("채널 ID '{}' 관련 ReadStatus 삭제 완료", channelId);

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
                return new ChannelException(ErrorCode.CHANNEL_NOT_FOUND,
                    Map.of("channelId", channelId.toString(), "actionType", actionType));
            });
    }

    private void validatePublicChannelRequest(PublicChannelCreateRequest request) {
        if (request == null || !StringUtils.hasText(request.name())) {
            log.warn("공개 채널 생성 요청 유효성 검사 실패 (채널명 누락 등): {}", request);
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION,
                Map.of("reason", "Request or channel name is null/empty", "requestBody",
                    String.valueOf(request)));
        }
    }

    private void validatePrivateChannelRequest(PrivateChannelCreateRequest request) {
        if (request == null) {
            log.warn("비공개 채널 생성 요청이 null입니다.");
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION,
                Map.of("reason", "Request is null"));
        }
        if (CollectionUtils.isEmpty(request.participantIds())) {
            log.warn("비공개 채널 생성 요청 유효성 검사 실패: 참여자 ID 목록이 비어있습니다. 요청: {}", request);
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION,
                Map.of("reason", "Participant list is null or empty", "requestBody",
                    String.valueOf(request)));
        }
        if (request.participantIds().size() != 2) {
            log.warn("비공개 채널은 두 명의 참여자만 허용합니다 (1:1). 요청된 총 참여자 수: {}",
                request.participantIds().size());
            throw new ChannelException(ErrorCode.CHANNEL_OPERATION_NOT_PERMITTED,
                Map.of("reason", "Private channels must have exactly two participants.",
                    "actualParticipantCount", String.valueOf(request.participantIds().size())));
        }
    }

    private String generatePrivateChannelName(List<UUID> participantIds) {
        if (CollectionUtils.isEmpty(participantIds)) {
            log.warn("비공개 채널명 생성 시 참여자 ID 목록이 비어있어 기본 이름을 반환합니다.");
            return "private_channel_empty_participants";
        }
        return participantIds.stream()
            .map(UUID::toString)
            .sorted()
            .collect(Collectors.joining("_"));
    }
}