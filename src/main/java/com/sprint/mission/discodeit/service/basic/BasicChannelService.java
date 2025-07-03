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
import com.sprint.mission.discodeit.service.basic.CustomUserDetailsService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @PreAuthorize("hasRole('CHANNEL_MANAGER') or hasRole('ADMIN')")
    @Transactional
    @Override
    public ChannelDto create(PublicChannelCreateRequest request) {
        log.info("🏢 Service: Public 채널 생성 시작 - 요청: {}", request);
        validatePublicChannelRequest(request);

        String channelName = request.name();
        String description = request.description();

        if (!StringUtils.hasText(channelName)) {
            log.error("🏢 Service: 채널명이 비어있음");
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION);
        }

        if (channelRepository.existsByName(channelName)) {
            log.error("🏢 Service: 이미 존재하는 채널명: {}", channelName);
            throw new ChannelException(ErrorCode.CHANNEL_ALREADY_EXISTS);
        }

        User currentUser = getCurrentUser();
        log.info("🏢 Service: 현재 사용자: {}", currentUser.getUsername());
        
        Channel channel = new Channel(ChannelType.PUBLIC, channelName, description,
            currentUser.getId());

        Channel savedChannel = channelRepository.save(channel);
        log.info("🏢 Service: 채널 생성 완료 - ID: {}", savedChannel.getId());
        return channelMapper.toDto(savedChannel);
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest request) {
        log.info("🔒 Service: Private 채널 생성 시작 - 요청: {}", request);
        validatePrivateChannelRequest(request);

        // 모든 참여자가 존재하는지 확인
        List<User> participants = userRepository.findAllById(request.participantIds());
        log.info("🔒 Service: 요청된 참여자 수: {}, 찾은 참여자 수: {}", 
                 request.participantIds().size(), participants.size());
        
        if (participants.size() != request.participantIds().size()) {
            // 어떤 사용자가 존재하지 않는지 찾기
            List<UUID> foundUserIds = participants.stream()
                .map(User::getId)
                .collect(Collectors.toList());
            
            UUID missingUserId = request.participantIds().stream()
                .filter(id -> !foundUserIds.contains(id))
                .findFirst()
                .orElse(null);
                
            log.error("🔒 Service: 존재하지 않는 사용자 ID: {}", missingUserId);
            throw new UserNotFoundException(missingUserId);
        }

        User currentUser = getCurrentUser();
        log.info("🔒 Service: 현재 사용자: {}", currentUser.getUsername());
        log.info("🔒 Service: 참여자들: {}", 
                 participants.stream().map(User::getUsername).collect(Collectors.toList()));

        // 채널명 자동 생성: "사용자A와 사용자B의 대화"
        String channelName = participants.stream()
            .map(User::getUsername)
            .collect(Collectors.joining("와 ", "", "의 대화"));
        log.info("🔒 Service: 자동 생성된 채널명: {}", channelName);

        Channel channel = new Channel(ChannelType.PRIVATE, channelName, null,
            currentUser.getId());
        Channel savedChannel = channelRepository.save(channel);
        log.info("🔒 Service: 채널 생성 완료 - ID: {}", savedChannel.getId());

        List<ReadStatus> readStatuses = participants.stream()
            .map(user -> new ReadStatus(user, savedChannel, savedChannel.getCreatedAt()))
            .collect(Collectors.toList());

        if (!readStatuses.isEmpty()) {
            readStatusRepository.saveAll(readStatuses);
            log.info("🔒 Service: ReadStatus 생성 완료 - {} 개", readStatuses.size());
        }
        
        ChannelDto result = channelMapper.toDto(savedChannel);
        log.info("🔒 Service: Private 채널 생성 성공 - 채널명: {}", result.name());
        return result;
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        Channel channel = findChannelByIdOrThrow(channelId);
        log.info("채널 조회 완료");
        try {
            return channelMapper.toDto(channel);
        } catch (Exception e) {
            throw new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        List<UUID> subscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
            .map(ReadStatus::getChannel).map(Channel::getId)
            .collect(Collectors.toList());

        List<Channel> channelList;
        if (subscribedChannelIds.isEmpty()) {
            channelList = channelRepository.findAllByType(ChannelType.PUBLIC);
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

        }

        return channelList.stream().map(channelMapper::toDto).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER') or hasRole('ADMIN')")
    @Transactional
    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
        log.info("✏️ Service: 채널 업데이트 시작 - channelId: {}, request: {}", channelId, request);
        Channel channel = findChannelByIdOrThrow(channelId);

        if (channel.isPrivate()) {
            log.error("✏️ Service: Private 채널 업데이트 거부");
            throw new ChannelException(ErrorCode.PRIVATE_CHANNEL_UPDATE_DENIED);
        }

        boolean updated = channel.update(request.newName(), request.newDescription());

        if (updated) {
            Channel updatedChannel = channelRepository.save(channel);
            log.info("✏️ Service: 채널 업데이트 완료");
            return channelMapper.toDto(updatedChannel);
        } else {
            log.info("✏️ Service: 변경사항 없음");
            return channelMapper.toDto(channel);
        }
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER') or hasRole('ADMIN')")
    @Transactional
    @Override
    public void delete(UUID channelId) {
        log.info("🗑️ Service: 채널 삭제 시작 - channelId: {}", channelId);
        if (!channelRepository.existsById(channelId)) {
            log.error("🗑️ Service: 존재하지 않는 채널");
            throw new ChannelNotFoundException(channelId);
        }

        messageRepository.deleteAllByChannelId(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);
        channelRepository.deleteById(channelId);
        log.info("🗑️ Service: 채널 삭제 완료");
    }

    private Channel findChannelByIdOrThrow(UUID channelId) {
        return channelRepository.findById(channelId)
            .orElseThrow(() -> new ChannelNotFoundException(channelId));
    }

    private void validatePublicChannelRequest(PublicChannelCreateRequest request) {
        if (request == null || !StringUtils.hasText(request.name())) {
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION);
        }
    }

    private void validatePrivateChannelRequest(PrivateChannelCreateRequest request) {
        if (request == null || request.participantIds() == null || request.participantIds()
            .isEmpty()) {
            log.error("🔒 Validation: 참여자 목록이 null이거나 비어있음");
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION);
        }

        if (request.participantIds().size() != 2) {
            log.error("🔒 Validation: 참여자 수가 2명이 아님 - 실제: {}", request.participantIds().size());
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION);
        }
        
        // 중복된 참여자 ID 확인
        if (request.participantIds().stream().distinct().count() != request.participantIds().size()) {
            log.error("🔒 Validation: 중복된 참여자 ID가 있음");
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION);
        }
        
        log.info("🔒 Validation: 개인 채널 요청 검증 완료");
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllPublic() {
        log.info("🏢 Service: 공개 채널 목록 조회 시작");
        List<Channel> publicChannels = channelRepository.findAllByType(ChannelType.PUBLIC);
        log.info("🏢 Service: 공개 채널 {} 개 조회", publicChannels.size());
        return publicChannels.stream()
            .map(channelMapper::toDto)
            .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }

        CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
        return principal.user();
    }
}