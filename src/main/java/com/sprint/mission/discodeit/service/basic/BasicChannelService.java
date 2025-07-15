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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @PreAuthorize("hasAuthority('ROLE_CHANNEL_MANAGER')")
    @Transactional
    @Override
    public ChannelDto create(PublicChannelCreateRequest request, User user) {
        validatePublicChannelRequest(request);

        String channelName = request.name();
        String description = request.description();

        if (!StringUtils.hasText(channelName)) {
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION);
        }

        if (channelRepository.existsByName(channelName)) {
            throw new ChannelException(ErrorCode.CHANNEL_ALREADY_EXISTS);
        }

        Channel channel = new Channel(
                ChannelType.PUBLIC,
                channelName,
                description,
                user
        );

        Channel savedChannel = channelRepository.save(channel);
        return channelMapper.toDto(savedChannel);
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest request, User user) {
        validatePrivateChannelRequest(request);

        List<User> participants = userRepository.findAllById(request.participantIds());

        if (participants.size() != request.participantIds().size()) {
            List<UUID> foundUserIds = participants.stream()
                    .map(User::getId)
                    .toList();

            UUID missingUserId = request.participantIds().stream()
                    .filter(id -> !foundUserIds.contains(id))
                    .findFirst()
                    .orElse(null);

            throw new UserNotFoundException(missingUserId);
        }

        String channelName = participants.stream()
                .map(User::getUsername)
                .sorted()
                .collect(Collectors.joining(", "));

        Channel channel = new Channel(
                ChannelType.PRIVATE,
                channelName,
                null,
                user
        );
        Channel savedChannel = channelRepository.save(channel);

        List<ReadStatus> readStatuses = participants.stream()
                .map(p -> new ReadStatus(p, savedChannel, savedChannel.getCreatedAt()))
                .collect(Collectors.toList());
        readStatusRepository.saveAll(readStatuses);

        return channelMapper.toDto(savedChannel);
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

    @PreAuthorize("hasAuthority('ROLE_CHANNEL_MANAGER')")
    @Transactional
    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
        Channel channel = findChannelByIdOrThrow(channelId);

        if (channel.isPrivate()) {
            throw new ChannelException(ErrorCode.PRIVATE_CHANNEL_UPDATE_DENIED);
        }

        boolean updated = channel.update(request.newName(), request.newDescription());

        if (updated) {
            Channel updatedChannel = channelRepository.save(channel);
            return channelMapper.toDto(updatedChannel);
        } else {
            return channelMapper.toDto(channel);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_CHANNEL_MANAGER')")
    @Transactional
    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new ChannelNotFoundException(channelId);
        }

        messageRepository.deleteAllByChannelId(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);
        channelRepository.deleteById(channelId);
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
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION);
        }

        if (request.participantIds().size() != 2) {
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION);
        }

        if (request.participantIds().stream().distinct().count() != request.participantIds().size()) {
            throw new ChannelException(ErrorCode.INVALID_CHANNEL_INFORMATION);
        }

    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllPublic() {
        List<Channel> publicChannels = channelRepository.findAllByType(ChannelType.PUBLIC);
        return publicChannels.stream()
                .map(channelMapper::toDto)
                .collect(Collectors.toList());
    }
}