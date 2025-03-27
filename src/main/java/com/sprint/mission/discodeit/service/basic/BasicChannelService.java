package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channelService.ChannelCreateByPrivateRequest;
import com.sprint.mission.discodeit.dto.channelService.ChannelDto;
import com.sprint.mission.discodeit.dto.channelService.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.dto.channelService.ChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;


    @Override
    public Channel create(ChannelCreateRequest channelCreateRequest) {
        Channel channel = channelCreateRequest.toEntity();
        return channelRepository.save(channel);
    }

    @Override
    public Channel createByPrivate(ChannelCreateByPrivateRequest channelCreateByPrivateRequest) {
        Channel channel = new Channel(ChannelType.PRIVATE,null);
        channelRepository.save(channel);

        channelCreateByPrivateRequest.UserIds().stream()
                .map(userId -> new ReadStatus(userId,channel.getId()))
                .forEach(readStatusRepository::save);

        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
       return channelRepository.findById(channelId);
    }

    @Override
    public ChannelDto findByStatus(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);
        if (channel == null) {
            throw new NoSuchElementException("채널 " +  channelId + " 를 찾을 수 없습니다");
        }
        return toDto(channel);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .toList();

        return channelRepository.findAll()
                .stream()
                .filter(channel ->
                        channel.getType().equals(ChannelType.PUBLIC)
                                || mySubscribedChannelIds.contains(channel.getId())
                )
                .map(this::toDto)
                .toList();
    }

    @Override
    public Channel update(UUID id ,ChannelUpdateRequest request) {
        String newName = request.newName();
        Channel channel = channelRepository.findById(id);
        if (channel == null) {
            throw new NoSuchElementException("채널 " + id + "가 존재하지 않습니다.");
        }
        if(channel.getType().equals(ChannelType.PRIVATE)){
            throw new IllegalArgumentException("비공개 서버는 수정할 수 없습니다.");
        }
        channel.updateChannel(newName);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
       messageRepository.deleteAllByChannelId(channelId);
       readStatusRepository.deleteAllByChannelId(channelId);
       channelRepository.delete(channelId);
    }


    private ChannelDto toDto(Channel channel) {
        Instant lastMessageAt = messageRepository.findByChannel(channel.getId())
                .stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst()
                .orElse(Instant.MIN);

        List<UUID> userIds = new ArrayList<>();
        if(channel.getType().equals(ChannelType.PRIVATE)){
            readStatusRepository.findAllByChannelId(channel.getId())
                    .stream()
                    .map(ReadStatus::getUserId)
                    .forEach(userIds::add);
        }

        return new ChannelDto(
                channel.getId(),
                channel.getType(),
                channel.getChannelName(),
                userIds,
                lastMessageAt
        );
    }

}
