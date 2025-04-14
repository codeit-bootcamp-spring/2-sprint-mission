package com.sprint.discodeit.sprint.service.basic.chnnel;

import com.sprint.discodeit.sprint.domain.ChannelType;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelFindResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelSummaryResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.PrivateChannelCreateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.PublicChannelCreateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Channel;
import com.sprint.discodeit.sprint.domain.entity.PrivateChannel;
import com.sprint.discodeit.sprint.domain.entity.PrivateChannelUser;
import com.sprint.discodeit.sprint.domain.entity.ReadStatus;
import com.sprint.discodeit.sprint.domain.entity.Users;
import com.sprint.discodeit.sprint.domain.mapper.ChannelMapper;
import com.sprint.discodeit.sprint.global.ErrorCode;
import com.sprint.discodeit.sprint.global.RequestException;
import com.sprint.discodeit.sprint.repository.ChannelRepository;
import com.sprint.discodeit.sprint.repository.PrivateChannelRepository;
import com.sprint.discodeit.sprint.repository.PrivateChannelUserRepository;
import com.sprint.discodeit.sprint.repository.UsersRepository;
import com.sprint.discodeit.sprint.repository.file.ChannelRepository;
import com.sprint.discodeit.sprint.repository.file.FileChannelRepository;
import com.sprint.discodeit.sprint.repository.file.FileMessageRepository;
import com.sprint.discodeit.sprint.repository.file.ReadStatusRepository;
import com.sprint.discodeit.sprint.service.basic.userss.ReadStatusService;
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
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final PrivateChannelRepository privateChannelRepository;
    private final UsersRepository usersRepository;

    @Override
    public ChannelResponseDto createPrivateChannel(PrivateChannelCreateRequestDto requestDto) {
        PrivateChannel channel = ChannelMapper.toPrviateChannel(requestDto);
        for(Long userId : requestDto.usersIds()) {
            Users users = usersRepository.findById(userId)
                    .orElseThrow(() -> new RequestException(ErrorCode.users_NOT_FOUND));

            channel.addPrivateChannelUser(users);
        }
        PrivateChannel privateChannel = privateChannelRepository.save(channel);
        return new ChannelResponseDto(privateChannel.getId(), privateChannel.getName(),privateChannel.getCreatedAt(), ChannelType.PRIVATE);
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
