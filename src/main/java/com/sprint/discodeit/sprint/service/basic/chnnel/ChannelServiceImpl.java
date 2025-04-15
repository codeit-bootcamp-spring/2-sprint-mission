package com.sprint.discodeit.sprint.service.basic.chnnel;

import com.sprint.discodeit.sprint.domain.ChannelType;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelFindResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelPrivateUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelPublicUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelSummaryResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelUpdateResponseDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.PrivateChannelCreateRequestDto;
import com.sprint.discodeit.sprint.domain.dto.channelDto.PublicChannelCreateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Channel;
import com.sprint.discodeit.sprint.domain.entity.PrivateChannel;
import com.sprint.discodeit.sprint.domain.entity.PublicChannel;
import com.sprint.discodeit.sprint.domain.entity.Users;
import com.sprint.discodeit.sprint.domain.mapper.ChannelMapper;
import com.sprint.discodeit.sprint.global.ErrorCode;
import com.sprint.discodeit.sprint.global.RequestException;
import com.sprint.discodeit.sprint.repository.ChannelRepository;
import com.sprint.discodeit.sprint.repository.PrivateChannelRepository;
import com.sprint.discodeit.sprint.repository.PublicChannelRepository;
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
    private final PublicChannelRepository publicChannelRepository;

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
        PublicChannel channel = ChannelMapper.toPublicChannel(requestDto);
        publicChannelRepository.save(channel);
        return new ChannelResponseDto(channel.getId(), channel.getName(), channel.getCreatedAt(), ChannelType.PUBLIC);
    }

    @Override
    public ChannelUpdateResponseDto publicUpdate(ChannelPublicUpdateRequestDto channelUpdateRequestDto,Long channelId) {
        PublicChannel channel = publicChannelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException(channelId + " 없는 채널 입니다"));
        if (channel.getType() == ChannelType.PUBLIC) {
            channel.update(channelUpdateRequestDto.newName(), channelUpdateRequestDto.newDescription());
            publicChannelRepository.save(channel);
        }else{
            throw new IllegalArgumentException("접근이 틀렸습니다.");
        }
        return new ChannelUpdateResponseDto(channel.getName(), channel.getDescription());
    }

    @Override
    public ChannelUpdateResponseDto privateUpdate(ChannelPrivateUpdateRequestDto channelUpdateRequestDto,
                                                  Long channelId) {
        PrivateChannel channel = privateChannelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException(channelId + " 없는 채널 입니다"));
        if (channel.getType() == ChannelType.PRIVATE) {
            channel.update(channelUpdateRequestDto.newName(), channelUpdateRequestDto.newDescription());
            privateChannelRepository.save(channel);
        }else{
            throw new IllegalArgumentException("접근이 틀렸습니다.");
        }
        return new ChannelUpdateResponseDto(channel.getName(), channel.getDescription());
    }


    @Override
    public void delete(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException(channelId + " 없는 채널입니다"));
        channelRepository.delete(channel);
    }

    @Override
    public ChannelFindResponseDto findChannelById(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));
        Instant latestMessageTime = fileMessageRepository.findLatestMessageTimeByChannelId(channel.getId());
        List<UUID> participantusersIds = null;
        if (channel.getType() == ChannelType.PRIVATE) {
            participantusersIds = readStatusRepository.findByusersIdAndChannelId(channelId);
        }
        return new ChannelFindResponseDto(channel.getId(), channel.getName(), latestMessageTime, channel.getType(), participantusersIds);
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
