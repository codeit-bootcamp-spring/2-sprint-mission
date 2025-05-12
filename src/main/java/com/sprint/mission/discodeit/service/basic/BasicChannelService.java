package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelExistsException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateNotSupportedException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusService readStatusService;
    private final ChannelMapper channelMapper;

    @Transactional
    @Override
    public ChannelDto createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto) {
        log.info("Creating private channel: {}", channelCreatePrivateDto);

        Channel newChannel = Channel.createPrivate();
        channelRepository.save(newChannel);

        channelCreatePrivateDto.participantIds().forEach(userId -> {
            ReadStatusCreateDto readStatusCreateDto = new ReadStatusCreateDto(userId, newChannel.getId(),
                    newChannel.getCreatedAt());
            readStatusService.create(readStatusCreateDto);
            log.debug("ReadStatus created for userId {} in channelId {}", userId, newChannel.getId());
        });

        log.info("Private channel created successfully: channelId={}", newChannel.getId());
        return channelMapper.toDto(newChannel);
    }

    @Transactional
    @Override
    public ChannelDto createPublic(ChannelCreatePublicDto channelCreatePublicDto) {
        log.info("Creating public channel: {}", channelCreatePublicDto);

        if (channelRepository.existsByTypeAndName(ChannelType.PUBLIC, channelCreatePublicDto.name())) {
            log.warn("Public channel name {} already exists", channelCreatePublicDto.name());
            throw new ChannelExistsException(channelCreatePublicDto.name());
        }

        Channel newChannel = Channel.createPublic(channelCreatePublicDto.name(), channelCreatePublicDto.description());
        channelRepository.save(newChannel);

        log.info("Public channel created successfully: channelId={}", newChannel.getId());
        return channelMapper.toDto(newChannel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto findById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        return channelMapper.toDto(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<UUID> joinedChannelIds = readStatusService.findAllByUserId(userId).stream()
                .map(ReadStatusDto::channelId)
                .toList();

        List<Channel> allChannels = channelRepository.findAll();

        // PRIVATE 채널 중 가입한 것만
        List<ChannelDto> privateChannels = allChannels.stream()
                .filter(channel -> channel.getType().equals(ChannelType.PRIVATE) &&
                        joinedChannelIds.contains(channel.getId()))
                .map(channelMapper::toDto)
                .toList();

        // PUBLIC 채널
        List<ChannelDto> publicChannels = allChannels.stream()
                .filter(channel -> channel.getType().equals(ChannelType.PUBLIC))
                .map(channelMapper::toDto)
                .toList();

        return Stream.concat(privateChannels.stream(), publicChannels.stream()).toList();
    }

    @Transactional
    @Override
    public ChannelDto update(UUID channelId, ChannelUpdateDto channelUpdateDto) {
        log.info("Updating channel: channelId={}", channelId);

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.warn("Channel not found: channelId={}", channelId);
                    return new ChannelNotFoundException(channelId);
                });

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            log.warn("Private channel update not supported: channelId={}", channelId);
            throw new PrivateChannelUpdateNotSupportedException(channelId);
        }

        channel.update(channelUpdateDto.newName(), channelUpdateDto.newDescription());

        log.info("Channel updated successfully: channelId={}", channelId);
        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        log.info("Deleting channel: channelId={}", channelId);
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        channelRepository.delete(channel);
        log.info("Channel deleted successfully: channelId={}", channelId);
    }
}
