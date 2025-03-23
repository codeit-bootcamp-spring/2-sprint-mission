package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.groups.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public void create(ChannelPublicCreateDto channelPublicCreateDto) {
        if (channelRepository.findById(channelPublicCreateDto.channelId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 채널입니다: " + channelPublicCreateDto.channelId());
        }

        if(channelPublicCreateDto.channelType() == ChannelType.PRIVATE) {
            this.createPrivateChannel(new ChannelPrivateCreateDto(channelPublicCreateDto.channelId(), channelPublicCreateDto.userId(), channelPublicCreateDto.channelType()));
        }else{
            this.createPublicChannel(channelPublicCreateDto);
        }
    }

    private void createPublicChannel(ChannelPublicCreateDto ChannelPublicCreateDto) {
        channelRepository.save(new Channel(
                ChannelPublicCreateDto.channelId(),
                Instant.now(),
                ChannelPublicCreateDto.name(),
                ChannelPublicCreateDto.description(),
                ChannelPublicCreateDto.userId(),
                ChannelPublicCreateDto.channelType()
        ));
    }

    private void createPrivateChannel(ChannelPrivateCreateDto ChannelPrivateCreateDto) {
        channelRepository.save(new Channel(
                ChannelPrivateCreateDto.channelId(),
                Instant.now(),
                ChannelPrivateCreateDto.userId(),
                ChannelPrivateCreateDto.channelType()
        ));

        readStatusRepository.save(new ReadStatus(UUID.randomUUID(), ChannelPrivateCreateDto.userId(), ChannelPrivateCreateDto.channelId(), false));
    }

    @Override
    public ChannelDto findById(UUID id) {
        return channelRepository.findById(id)
                .map(ChannelDto :: from)
                .orElseThrow(() -> new RuntimeException("채널을 찾을 수 없습니다."));
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        return channelRepository
                .findAll()
                .orElse(Collections.emptyList())
                .stream()
                .map( channel -> {
                    if (channel.getChannelType() == ChannelType.PRIVATE && !channel.getUserIds().containsAll(channel.getUserIds())) {
                        return null;
                    }

                    return new ChannelDto(
                            channel.getId(),
                            channel.getName(),
                            channel.getDescription(),
                            channel.getUpdatedAt(),
                            channel.getUserIds(),
                            channel.getChannelType()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public void update(ChannelUpdateDto updateDto) {

        Channel channel = channelRepository.findById(updateDto.id())
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다"));

        if(channel.getChannelType() == ChannelType.PRIVATE) {
            throw new RuntimeException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.setUpdatedAt(Instant.now());
        channel.setName(updateDto.name());
        channel.setDescription(updateDto.description());

        channelRepository.update(channel);
    }

    @Override
    public void delete(UUID id) {
        ChannelDto channelDto = this.findById(id);
        channelRepository.delete(id);

        Message message = messageRepository
                .findByChannelId(channelDto.channelId())
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."))
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

        messageRepository.delete(message.getId());

        ReadStatus readStatus= readStatusRepository
                .findAllByUserId(message.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("읽기상태를 찾을 수 없습니다."))
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("읽기상태를 찾을 수 없습니다."));

        readStatusRepository.delete(readStatus.getId());
    }
}

