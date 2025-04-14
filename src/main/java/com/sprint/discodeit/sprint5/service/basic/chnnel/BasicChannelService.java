package com.sprint.discodeit.sprint5.service.basic.chnnel;

import com.sprint.discodeit.sprint5.domain.ChannelType;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.ChannelFindResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.ChannelSummaryResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.ChannelUpdateRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.PrivateChannelCreateRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.channelDto.PublicChannelCreateRequestDto;
import com.sprint.discodeit.sprint5.domain.entity.Channel;
import com.sprint.discodeit.sprint5.domain.entity.ReadStatus;
import com.sprint.discodeit.sprint5.domain.mapper.ChannelMapper;
import com.sprint.discodeit.sprint5.repository.file.ChannelRepository;
import com.sprint.discodeit.sprint5.repository.file.FileChannelRepository;
import com.sprint.discodeit.sprint5.repository.file.FileMessageRepository;
import com.sprint.discodeit.sprint5.repository.file.ReadStatusRepository;
import com.sprint.discodeit.sprint5.service.basic.userss.ReadStatusService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelServiceV1 {

    private final ChannelRepository channelRepository;
    private final ReadStatusService readStatusService;
    private final ReadStatusRepository readStatusRepository;
    private final FileChannelRepository fileChannelRepository;
    private final FileMessageRepository fileMessageRepository;

    @Override
    public ChannelResponseDto createPrivateChannel(PrivateChannelCreateRequestDto requestDto) {
        List<UUID> usersIds = requestDto.usersIds();
        Channel channel = ChannelMapper.toPrviateChannel(requestDto);
        channelRepository.save(channel);
        List<ReadStatus> readStatuses = readStatusService.createReadStatusesForPrivateChannel(usersIds, channel.getId());
        readStatusRepository.saveAll(readStatuses);
        return new ChannelResponseDto(channel.getId(), channel.getName(), channel.getCreatedAt(), ChannelType.PRIVATE);
    }

    @Override
    public ChannelResponseDto createPublicChannel(PublicChannelCreateRequestDto requestDto) {
        Channel channel = ChannelMapper.toPublicChannel(requestDto);
        channelRepository.save(channel);
        return new ChannelResponseDto(channel.getId(), channel.getName(), channel.getCreatedAt(), ChannelType.PUBLIC);
    }

    @Override
    public Channel update(String channelId, ChannelUpdateRequestDto channelUpdateRequestDto) {
        Channel channel = channelRepository.findById(UUID.fromString(channelId)).orElseThrow(() -> new NoSuchElementException(channelId + " 없는 채널 입니다"));
        if (channel.getType() == ChannelType.PUBLIC) {
            channel.update(channelUpdateRequestDto.newName(), channelUpdateRequestDto.newDescription());
        }else{
            throw new IllegalArgumentException("private 방은 수정이 불가능 합니다.");
        }
        return channel;
    }


    @Override
    public void delete(UUID channelId) {
        readStatusRepository.delete(channelId);
        channelRepository.delete(channelId);
    }

    @Override
    public ChannelFindResponseDto findChannelById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));
        Instant latestMessageTime = fileMessageRepository.findLatestMessageTimeByChannelId(channel.getId());
        List<UUID> participantusersIds = null;
        if (channel.getType() == ChannelType.PRIVATE) {
            participantusersIds = readStatusRepository.findByusersIdAndChannelId(channelId);
        }
        return new ChannelFindResponseDto(channel.getId(), channel.getName(), latestMessageTime, channel.getType(), participantusersIds);
    }



    public List<ChannelSummaryResponseDto> findAllByusersId(UUID usersId) {
        List<Channel> publicChannels = fileChannelRepository.findByChannelType(ChannelType.PRIVATE);

        List<UUID> privateChannelIds = readStatusRepository.findChannelIdsByusersIdAll(usersId);
        List<Channel> privateChannels = fileChannelRepository.findByIdAll(privateChannelIds);

        List<Channel> allChannels = new ArrayList<>();
        allChannels.addAll(publicChannels);
        allChannels.addAll(privateChannels);

        return allChannels.stream()
                .map(this::toChannelSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChannelSummaryResponseDto> findAll() {
        List<Channel> publicChannels = fileChannelRepository.findByChannelType(ChannelType.PRIVATE);
        return publicChannels.stream()
                .map(this::toChannelSummaryDto) // 이미 분리한 변환 메서드 사용
                .collect(Collectors.toList());
    }

    private ChannelSummaryResponseDto toChannelSummaryDto(Channel channel) {
        Instant latestMessageAt = fileMessageRepository.findLatestMessageTimeByChannelId(channel.getId());
        if (latestMessageAt == null) {
            latestMessageAt = channel.getCreatedAt();
        }

        List<UUID> participantusersIds = null;
        if (channel.getType() == ChannelType.PRIVATE) {
            participantusersIds = readStatusRepository.findByusersIdAndChannelId(channel.getId());
        }

        return new ChannelSummaryResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getType(),
                latestMessageAt,
                participantusersIds
        );
    }


}
