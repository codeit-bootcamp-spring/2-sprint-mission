package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelReqDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelReqDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public ChannelResDto createPublicChannel(CreateChannelReqDto.Public createChannelReqDto) {
        Channel channel = new Channel(ChannelType.PUBLIC, createChannelReqDto.name(), createChannelReqDto.description());
        channelRepository.save(channel);
        return new ChannelResDto(channel.getId(), channel.getTitle(), channel.getDescription(), channel.getChannelType(), null, Collections.emptyList());
    }

    @Override
    public ChannelResDto createPrivateChannel(CreateChannelReqDto.Private createChannelReqDto) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(channel);

        for (UUID participantUserId : createChannelReqDto.participantUserIds()) {
            readStatusRepository.save(new ReadStatus(channel.getId(), participantUserId, Instant.MIN));
        }

        return new ChannelResDto(channel.getId(), channel.getTitle(), channel.getDescription(), channel.getChannelType(),null, createChannelReqDto.participantUserIds());
    }

    @Override
    public ChannelResDto find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        Instant latestMessageAt = null;
        List<Message> messages = messageRepository.findAllByChannelId(channel.getId());
        messages.sort(Comparator.comparing(Message::getCreatedAt).reversed());
        if (!messages.isEmpty()) {
            latestMessageAt = messages.get(0).getCreatedAt();
        }

        List<UUID> participantUserIds = new ArrayList<>();

        if (channel.getChannelType() == ChannelType.PRIVATE) { //채널의 유형이 PRIVATE이면 참여중인 모든 유저를 찾는다.
            readStatusRepository.findAllByChannelId(channelId)
                    .forEach(readStatus -> participantUserIds.add(readStatus.getUserId()));
        }


        return new ChannelResDto(
                channel.getId(),
                channel.getTitle(),
                channel.getDescription(),
                channel.getChannelType(),
                latestMessageAt,
                participantUserIds
        );
    }

    @Override
    public List<ChannelResDto> findAll() {
        return channelRepository.findAll().stream()
                .map(channel -> {
                    Instant latestMessageAt = null;
                    List<Message> messages = messageRepository.findAllByChannelId(channel.getId());
                    messages.sort(Comparator.comparing(Message::getCreatedAt).reversed());
                    if (!messages.isEmpty()) {
                        latestMessageAt = messages.get(0).getCreatedAt();
                    }

                    List<UUID> participantUserIds = new ArrayList<>();

                    if (channel.getChannelType() == ChannelType.PRIVATE) { //채널의 유형이 PRIVATE이면 참여중인 모든 유저를 찾는다.
                        readStatusRepository.findAllByChannelId(channel.getId())
                                .forEach(readStatus -> participantUserIds.add(readStatus.getUserId()));
                    }

                    return new ChannelResDto(
                            channel.getId(),
                            channel.getTitle(),
                            channel.getDescription(),
                            channel.getChannelType(),
                            latestMessageAt,
                            participantUserIds
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ChannelResDto> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
            .filter(channel -> readStatusRepository.findAllByChannelId(channel.getId())
                    .stream()
                    .anyMatch(readStatus -> readStatus.getUserId().equals(userId)))
            .map(channel -> {
                Instant latestMessageAt = null;
                List<Message> messages = messageRepository.findAllByChannelId(channel.getId());
                messages.sort(Comparator.comparing(Message::getCreatedAt).reversed());
                if (!messages.isEmpty()) {
                    latestMessageAt = messages.get(0).getCreatedAt();
                }

                List<UUID> participantUserIds = new ArrayList<>();

                if (channel.getChannelType() == ChannelType.PRIVATE) {
                    readStatusRepository.findAllByChannelId(channel.getId())
                            .forEach(readStatus -> participantUserIds.add(readStatus.getUserId()));
                }

                return new ChannelResDto(
                        channel.getId(),
                        channel.getTitle(),
                        channel.getDescription(),
                        channel.getChannelType(),
                        latestMessageAt,
                        participantUserIds
                );
            })
            .collect(Collectors.toList());
    }

    @Override
    public ChannelResDto update(UUID channelId, UpdateChannelReqDto updateChannelReqDto) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        if(channel.getChannelType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.updateChannel(updateChannelReqDto.name(), updateChannelReqDto.description(), Instant.now());
        Channel updatedChannel = channelRepository.save(channel);

        Instant latestMessageAt = null;
        List<Message> messages = messageRepository.findAllByChannelId(channel.getId());
        messages.sort(Comparator.comparing(Message::getCreatedAt).reversed());
        if (!messages.isEmpty()) {
            latestMessageAt = messages.get(0).getCreatedAt();
        }

        List<UUID> participantUserIds = new ArrayList<>();

        return new ChannelResDto(
                updatedChannel.getId(),
                updatedChannel.getTitle(),
                updatedChannel.getDescription(),
                updatedChannel.getChannelType(),
                latestMessageAt,
                participantUserIds
        );
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        //연관된 엔티티를 삭제한다.
        messageRepository.deleteAllByChannelId(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);

        channelRepository.deleteById(channel.getId());
    }
}
