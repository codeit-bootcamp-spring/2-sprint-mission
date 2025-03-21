package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequestDto;
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
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
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
                .collect(Collectors.toList());
    }

    @Override
    public ChannelResponseDto update(ChannelUpdateRequestDto updateRequest) {
        Channel channel = channelRepository.findById(updateRequest.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + updateRequest.getChannelId() + " not found"));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        if (updateRequest.getNewName() != null) {
            channel.update(updateRequest.getNewName(), updateRequest.getNewDescription());
        }

        Channel updatedChannel = channelRepository.save(channel);

        return ChannelResponseDto.builder()
                .id(updatedChannel.getId())
                .type(updatedChannel.getType())
                .name(updatedChannel.getName())
                .description(updatedChannel.getDescription())
                .build();
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);
        channelRepository.deleteById(channelId);
    }
}
