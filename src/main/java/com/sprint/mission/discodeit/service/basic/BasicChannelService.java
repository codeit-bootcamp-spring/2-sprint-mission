package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
        validatePublicChannelRequest(request);

        String channelName = request.name();
        String description = request.description();

        if (!StringUtils.hasText(channelName)) {
            log.warn("채널명 누락");
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION,
                Map.of("missingField", "channelName"));
        }

        if (channelRepository.existsByName(channelName)) {
            log.warn("채널명 중복");
            throw new ChannelException(ErrorCode.CHANNEL_ALREADY_EXISTS,
                Map.of("channelName", channelName));
        }

        Channel channel = new Channel(ChannelType.PUBLIC, channelName, description);

        Channel savedChannel = channelRepository.save(channel);
        log.info("공개 채널 생성 완료");
        return channelMapper.toDto(savedChannel);
    }

    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest request) {
        validatePrivateChannelRequest(request);

        List<User> participants = userRepository.findAllById(request.participantIds());

        Channel channel = new Channel(ChannelType.PRIVATE, request.channelName(), null);
        channelRepository.save(channel);

        log.info("비공개 채널 기본 정보 저장 완료");

        List<ReadStatus> readStatuses = participants.stream()
            .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
            .collect(Collectors.toList());

        if (!readStatuses.isEmpty()) {
            readStatusRepository.saveAll(readStatuses);
            log.info("ReadStatus 저장 완료");
        } else {
            log.info("ReadStatus 없음");
        }

        log.info("비공개 채널 생성 완료");
        return channelMapper.toDto(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        Channel channel = findChannelByIdOrThrow(channelId, "조회할");
        log.info("채널 조회 완료");
        try {
            return channelMapper.toDto(channel);
        } catch (Exception e) {
            log.error("채널 DTO 변환 오류", e);
            throw new RuntimeException("채널 정보 조회 중 오류 발생 (Mapper)", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("사용자 없음");
            throw new UserNotFoundException(userId.toString());
        }

        List<UUID> subscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
            .map(ReadStatus::getChannel).map(Channel::getId).distinct()
            .collect(Collectors.toList());
        log.info("사용자 채널 목록 확인 완료");

        List<Channel> channelList;
        if (subscribedChannelIds.isEmpty()) {
            channelList = channelRepository.findAllByType(ChannelType.PUBLIC);
            log.info("공개 채널 조회 완료");
        } else {
            List<Channel> publicChannels = channelRepository.findAllByType(ChannelType.PUBLIC);
            List<Channel> subscribedPrivateChannels = channelRepository.findAllByIdInAndType(
                subscribedChannelIds, ChannelType.PRIVATE);

            channelList = new ArrayList<>(publicChannels);
            subscribedPrivateChannels.forEach(spc -> {
                if (publicChannels.stream().noneMatch(pc -> pc.getId().equals(spc.getId()))) {
                    channelList.add(spc);
                }
            });

            log.info("채널 조회 완료");
        }

        return channelList.stream().map(channelMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
        Channel channel = findChannelByIdOrThrow(channelId, "업데이트할");

        if (channel.isPrivate()) {
            log.warn("비공개 채널 수정 불가");
            throw new ChannelException(ErrorCode.PRIVATE_CHANNEL_UPDATE_DENIED,
                Map.of("channelId", channelId.toString()));
        }

        boolean updated = channel.update(request.newName(), request.newDescription());

        if (updated) {
            Channel updatedChannel = channelRepository.save(channel);
            log.info("채널 업데이트 완료");
            return channelMapper.toDto(updatedChannel);
        } else {
            log.info("채널 변경 사항 없음");
            return channelMapper.toDto(channel);
        }
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            log.warn("채널 없음");
            throw new ChannelNotFoundException(Map.of("channelId", channelId.toString()));
        }

        log.info("채널 메시지 삭제 완료");
        messageRepository.deleteAllByChannelId(channelId);

        log.info("채널 ReadStatus 삭제 완료");
        readStatusRepository.deleteAllByChannelId(channelId);

        channelRepository.deleteById(channelId);
        log.info("채널 삭제 완료");
    }


    private Channel findChannelByIdOrThrow(UUID channelId, String actionType) {
        try {
            return channelRepository.findById(channelId).orElseThrow(() -> {
                log.warn("채널 없음");
                return new ChannelNotFoundException(Map.of("channelId", channelId.toString()));
            });
        } catch (NoSuchElementException e) {
            log.warn("채널을 찾지 못했습니다 (NoSuchElementException). Channel ID: {}", channelId, e);
            throw new ChannelNotFoundException(Map.of("channelId", channelId.toString()));
        }
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
            log.warn("비공개 채널 생성 요청이 null 입니다.");
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION,
                Map.of("reason", "Request is null"));
        }
        if (CollectionUtils.isEmpty(request.participantIds())) {
            log.warn("비공개 채널 생성 요청 유효성 검사 실패: 참여자 ID 목록이 비어있습니다. 요청: {}", request);
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION,
                Map.of("reason", "Participant list is null or empty", "requestBody",
                    String.valueOf(request)));
        }

        List<UUID> requestedParticipantIds = request.participantIds();
        for (UUID participantId : requestedParticipantIds) {
            if (!userRepository.existsById(participantId)) {
                log.warn("비공개 채널 생성 실패: 존재하지 않는 사용자 ID [{}] 포함.", participantId);
                throw new UserNotFoundException(Map.of("userId", participantId.toString()));
            }
        }
    }

}