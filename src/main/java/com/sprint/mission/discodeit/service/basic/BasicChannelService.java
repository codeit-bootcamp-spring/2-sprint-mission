package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final ChannelMapper channelMapper;

    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest request) {
        log.info("Creating private channel");

        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(channel);

        List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
                .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
                .toList();
        readStatusRepository.saveAll(readStatuses);

        log.info("Private channel created successfully : id = {}", channel.getId());

        return channelMapper.toDto(channel);
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Transactional
    @Override
    public ChannelDto create(PublicChannelCreateRequest request) {
        log.info("Creating public channel");

        String channelName = request.name();
        String description = request.description();
        Channel channel = new Channel(ChannelType.PUBLIC, channelName, description);
        channelRepository.save(channel);

        log.info("Public channel created successfully : id = {}", channel.getId());
        return channelMapper.toDto(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto searchChannel(UUID channelId) {
        Channel channel = getChannel(channelId);
        return channelMapper.toDto(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllChannelsByUserId(UUID userId) {
        List<UUID> subscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatus -> readStatus.getChannel().getId())
                .toList();

        return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, subscribedChannelIds)
                .stream()
                .map(channelMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Transactional
    @Override
    public ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest request) {
        log.info("Updating public channel : id = {}", channelId);
        String newName = request.newName();
        String newDescription = request.newDescription();
        Channel channel = getChannel(channelId);

        if (channel.getType() == ChannelType.PRIVATE) {
            log.warn("Cannot update private channel: id = {}", channelId);
            throw PrivateChannelUpdateException.forChannel(channelId);
        }

        channel.updateChannel(newName, newDescription);

        log.info("Channel updated successfully: id = {} ", channel.getId());

        return channelMapper.toDto(channel);
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Transactional
    @Override
    public void deleteChannel(UUID channelId) {
        log.info("Deleting channel : id = {}", channelId);

        if (!channelRepository.existsById(channelId)) {
            throw ChannelNotFoundException.withId(channelId);
        }

        messageRepository.deleteAllByChannelId(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);

        channelRepository.deleteById(channelId);
        log.info("Channel deleted successfully : id = {}", channelId);
    }

    private Channel getChannel(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.warn("Channel with id {} not found", channelId);
                    return ChannelNotFoundException.withId(channelId);
                });
    }
}

