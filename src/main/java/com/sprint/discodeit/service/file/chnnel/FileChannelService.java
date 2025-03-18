package com.sprint.discodeit.service.file.chnnel;

import com.sprint.discodeit.domain.ChannelType;
import com.sprint.discodeit.domain.dto.channelDto.ChannelCreateRequestDto;
import com.sprint.discodeit.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.domain.dto.channelDto.ChannelUpdateRequestDto;
import com.sprint.discodeit.domain.entity.Channel;
import com.sprint.discodeit.domain.entity.ReadStatus;
import com.sprint.discodeit.domain.mapper.ChannelMapper;
import com.sprint.discodeit.repository.ChannelRepository;
import com.sprint.discodeit.repository.file.ReadStatusRepository;
import com.sprint.discodeit.service.ChannelServiceV1;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileChannelService implements ChannelServiceV1 {

    private final ChannelRepository channelRepository;
    private final ReadStatusService readStatusService;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ChannelResponseDto create(ChannelCreateRequestDto channelCreateRequestDto) {

        Channel channelMapper = ChannelMapper.toChannelMapper(channelCreateRequestDto);
        ReadStatus readStatus = readStatusService.dispatchChannelCreation(channelMapper.getName(),
                channelCreateRequestDto.userId(), channelMapper.getId());

        List<UUID> userIds = new ArrayList<>();

        // 저장
        channelRepository.save(channelMapper);
        if(readStatus != null){
            readStatusRepository.save(readStatus);
        }
        return new ChannelResponseDto(channelMapper.getId(), channelMapper.getName(), channelMapper.getCreatedAt() ,channelMapper.getType());
    }


    @Override
    public Channel update(ChannelUpdateRequestDto channelUpdateRequestDto) {
        Channel channel = channelRepository.findById(channelUpdateRequestDto.channelId()).orElseThrow(() -> new NoSuchElementException(channelUpdateRequestDto.channelId() + " 없는 채널 입니다"));
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
}
