package com.sprint.discodeit.service.basic.chnnel;

import com.sprint.discodeit.domain.ChannelType;
import com.sprint.discodeit.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.domain.dto.channelDto.ChannelUpdateRequestDto;
import com.sprint.discodeit.domain.dto.channelDto.PrivateChannelCreateRequestDto;
import com.sprint.discodeit.domain.dto.channelDto.PublicChannelCreateRequestDto;
import com.sprint.discodeit.domain.entity.Channel;
import com.sprint.discodeit.domain.entity.ReadStatus;
import com.sprint.discodeit.domain.mapper.ChannelMapper;
import com.sprint.discodeit.repository.ChannelRepository;
import com.sprint.discodeit.repository.file.FileChannelRepository;
import com.sprint.discodeit.repository.file.ReadStatusRepository;
import com.sprint.discodeit.service.ChannelServiceV1;
import com.sprint.discodeit.service.basic.users.ReadStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelServiceV1 {

    private final ChannelRepository channelRepository;
    private final ReadStatusService readStatusService;
    private final ReadStatusRepository readStatusRepository;
    private final FileChannelRepository fileChannelRepository;

//    @Override
//    public ChannelResponseDto create(ChannelCreateRequestDto channelCreateRequestDto) {
//
//        Channel channelMapper = ChannelMapper.toChannel(channelCreateRequestDto);
//        ReadStatus readStatus = readStatusService.dispatchChannelCreation(channelMapper.getName(),
//                channelCreateRequestDto.userId(), channelMapper.getId());
//        // 저장
//        channelRepository.save(channelMapper);
//        if(readStatus != null){
//            readStatusRepository.save(readStatus);
//        }
//        return new ChannelResponseDto(channelMapper.getId(), channelMapper.getName(), channelMapper.getCreatedAt() ,channelMapper.getType());
//    }


    @Override
    public ChannelResponseDto createPrivateChannel(PrivateChannelCreateRequestDto requestDto) {
        List<UUID> userIds = requestDto.userIds();
        Channel channel = ChannelMapper.toPrviateChannel(requestDto);
        channelRepository.save(channel);
        List<ReadStatus> readStatuses = readStatusService.createReadStatusesForPrivateChannel(userIds, channel.getId());
        readStatusRepository.saveAll(readStatuses);
        return new ChannelResponseDto(channel.getId(), channel.getName(), channel.getCreatedAt(), ChannelType.PRIVATE);
    }

    @Override
    public Channel createPublicChannel(PublicChannelCreateRequestDto requestDto) {
        return null;
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
    public List<Channel> find(ChannelType channelType) {
        List<Channel> chnnelType = fileChannelRepository.findByChnnelType(channelType);
        return chnnelType;
    }


}
