package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDTO;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDTO;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDTO;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDTO;
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

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel createPrivateChannel(CreatePrivateChannelDTO dto) {
        Channel newChannel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(newChannel);

        dto.userlist().forEach(user -> {
            ReadStatus readStatus = new ReadStatus(user.getId(), newChannel.getId());
            readStatusRepository.save(readStatus);
        });
        return newChannel;
    }

    @Override
    public Channel createPublicChannel(CreatePublicChannelDTO dto) {
        Channel newChannel = new Channel(ChannelType.PUBLIC, dto.channelName(), dto.description());
        return channelRepository.save(newChannel);
    }

    @Override
    public ChannelResponseDTO searchChannel(UUID channelId) {
        Channel channel = getChannel(channelId);

        Instant lastMessageAt = messageRepository.findByChannelId(channelId).stream()
                .max(Comparator.comparing(Message::getCreatedAt))
                .map(Message::getCreatedAt)
                .orElse(null);

        List<UUID> userIds = channel.getType() == ChannelType.PRIVATE
                ? readStatusRepository.findByChannelId(channelId).stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .map(ReadStatus::getUserId)
                .toList()
                : null;

        return new ChannelResponseDTO(
                channel.getId(),
                channel.getType(),
                channel.getChannelName(),
                channel.getDescription(),
                lastMessageAt,
                userIds
        );
    }

    @Override
    public List<ChannelResponseDTO> searchAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();

        List<Channel> publicChannels = channels.stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC)
                .toList();

        List<UUID> privateChannelIds = readStatusRepository.findByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .toList();

        List<Channel> privateChannels = channels.stream()
                .filter(channel -> channel.getType() == ChannelType.PRIVATE && privateChannelIds.contains(channel.getId()))
                .toList();

        List<Channel> filteredChannels = new ArrayList<>();
        filteredChannels.addAll(publicChannels);
        filteredChannels.addAll(privateChannels);

        return filteredChannels.stream().map(channel -> {
            Instant lastMessageAt = messageRepository.findByChannelId(channel.getId()).stream()
                    .max(Comparator.comparing(Message::getCreatedAt))
                    .map(Message::getCreatedAt)
                    .orElse(null);
            List<UUID> userIds = channel.getType() == ChannelType.PRIVATE
                    ? readStatusRepository.findByChannelId(channel.getId()).stream()
                    .map(ReadStatus::getUserId)
                    .toList()
                    : null;
            return new ChannelResponseDTO(
                    channel.getId(),
                    channel.getType(),
                    channel.getChannelName(),
                    channel.getDescription(),
                    lastMessageAt,
                    userIds
            );
        }).toList();
    }

    @Override
    public Channel updateChannel(UpdateChannelDTO dto) {
        Channel channel = getChannel(dto.channelId());

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new UnsupportedOperationException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.updateChannel(dto.channelName(), dto.description());
        return channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = getChannel(channelId);

        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);
        channelRepository.delete(channelId);
    }

    private Channel getChannel(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + channelId + "인 채널을 찾을 수 없습니다."));
    }
}
