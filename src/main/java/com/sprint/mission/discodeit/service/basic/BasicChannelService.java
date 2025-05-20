package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;

    @Override
    public Channel create(PublicChannelCreateRequest request) {
        log.info("▶▶ [SERVICE] Creating public channel - name: {}, description: {}", request.name(), request.description());
        Channel channel = Channel.builder()
                .name(request.name())
                .description(request.description())
                .type(ChannelType.PUBLIC)
                .build();
        Channel savedChannel = channelRepository.save(channel);
        log.info("◀◀ [SERVICE] Public channel created - id: {}", savedChannel.getId());
        return savedChannel;
    }

    @Override
    public Channel create(PrivateChannelCreateRequest request) {
        log.info("▶▶ [SERVICE] Creating private channel with {} participants", request.participantIds().size());
        Channel channel = Channel.builder()
                .type(ChannelType.PRIVATE)
                .build();
        Channel createdChannel = channelRepository.save(channel);

        request.participantIds().forEach(userId -> {
            log.debug("▶▶ [SERVICE] Adding participant - userId: {}", userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.error("◀◀ [SERVICE] User not found - userId: {}", userId);
                        return new UserNotFoundException(userId);
                    });

            ReadStatus readStatus = ReadStatus.builder()
                    .user(user)
                    .channel(createdChannel)
                    .lastReadAt(Instant.MIN)
                    .build();

            readStatusRepository.save(readStatus);
            log.debug("◀◀ [SERVICE] Participant added - userId: {}", userId);
        });

        log.info("◀◀ [SERVICE] Private channel created - id: {}", createdChannel.getId());
        return createdChannel;
    }

    @Override
    public ChannelDto find(UUID channelId) {
        log.info("▶▶ [SERVICE] Finding channel - id: {}", channelId);
        return channelRepository.findById(channelId)
                .map(channel -> {
                    log.info("◀◀ [SERVICE] Channel found - id: {}", channelId);
                    return channelMapper.toDto(channel);
                })
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] Channel not found - id: {}", channelId);
                    return new ChannelNotFoundException(channelId);
                });
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        log.info("▶▶ [SERVICE] Finding channels for user - userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] User not found - userId: {}", userId);
                    return new UserNotFoundException(userId);
                });

        List<Channel> subscribedChannels = readStatusRepository.findAllByUser(user).stream()
                .map(ReadStatus::getChannel)
                .toList();

        List<ChannelDto> result = channelRepository.findAll().stream()
                .filter(channel ->
                        channel.getType().equals(ChannelType.PUBLIC) ||
                                subscribedChannels.contains(channel)
                )
                .map(channelMapper::toDto)
                .toList();

        log.info("◀◀ [SERVICE] Found {} channels for user - userId: {}", result.size(), userId);
        return result;
    }

    @Override
    public Channel update(UUID channelId, PublicChannelUpdateRequest request) {
        log.info("▶▶ [SERVICE] Updating channel - id: {}", channelId);
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] Channel not found - id: {}", channelId);
                    return new NoSuchElementException("Channel with id " + channelId + " not found");
                });
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            log.error("◀◀ [SERVICE] Update failed: Private channel cannot be updated - id: {}", channelId);
            throw new PrivateChannelUpdateException(channelId);
        }

        if (request.newName() != null) {
            log.debug("▶▶ [SERVICE] Updating name - new: {}", request.newName());
            channel.setName(request.newName());
        }

        if (request.newDescription() != null) {
            log.debug("▶▶ [SERVICE] Updating description - new: {}", request.newDescription());
            channel.setDescription(request.newDescription());
        }

        Channel updatedChannel = channelRepository.save(channel);
        log.info("◀◀ [SERVICE] Channel updated - id: {}", channelId);
        return updatedChannel;
    }

    @Override
    public void delete(UUID channelId) {
        log.info("▶▶ [SERVICE] Deleting channel - id: {}", channelId);
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] Channel not found - id: {}", channelId);
                    return new ChannelNotFoundException(channelId);
                });

        log.debug("▶▶ [SERVICE] Deleting related messages for channel - id: {}", channelId);
        messageRepository.deleteAllByChannel(channel);

        log.debug("▶▶ [SERVICE] Deleting read statuses for channel - id: {}", channelId);
        readStatusRepository.deleteAllByChannel(channel);

        channelRepository.deleteById(channelId);
        log.info("◀◀ [SERVICE] Channel deleted - id: {}", channelId);
    }
}