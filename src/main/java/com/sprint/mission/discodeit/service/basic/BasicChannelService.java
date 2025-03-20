package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel createPublicChannel(PublicChannelCreateRequestDto requestDto) {
        Channel channel = new Channel(requestDto.getType(), requestDto.getName(), requestDto.getDescription());
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPrivateChannel(PrivateChannelCreateRequestDto requestDto) {
        Channel channel = new Channel(requestDto.getType(),null,null);
        channelRepository.save(channel);

        for (UUID userId : requestDto.getUserIds()) {
            ReadStatus readStatus = new ReadStatus(userId, channel.getId(),Instant.now());
            readStatusRepository.save(readStatus);
        }
        return channel;
    }


    @Override
    public ChannelResponseDto find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id" + channelId + " not found"));

        Instant latestMessageAt = messageRepository.findLatestMessageTimeByChannelId(channelId).orElse(null);

        List<UUID> userIds = new ArrayList<>();
        if (channel.getType() == ChannelType.PRIVATE){
            userIds = readStatusRepository.findUserIdsByChannelId(channelId);
        }

        return new ChannelResponseDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                latestMessageAt,
                userIds
        );


    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) { // ✅ 반환 타입 수정
        List<Channel> allChannels = channelRepository.findAll();

        return allChannels.stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC ||
                        readStatusRepository.findUserIdsByChannelId(channel.getId()).contains(userId)) // ✅ UUID.contains() 오류 수정
                .map(channel -> {
                    Instant latestMessageAt = messageRepository.findLatestMessageTimeByChannelId(channel.getId()).orElse(null);
                    List<UUID> userIds = new ArrayList<>();

                    if (channel.getType() == ChannelType.PRIVATE) {
                        userIds = readStatusRepository.findUserIdsByChannelId(channel.getId());
                    }

                    return new ChannelResponseDto(
                            channel.getId(),
                            channel.getType(),
                            channel.getName(),
                            channel.getDescription(),
                            latestMessageAt,
                            userIds
                    );
                })
                .collect(Collectors.toList()); // ✅ Collectors import 필요
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        channel.update(newName, newDescription);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        channelRepository.deleteById(channelId);
    }
}
