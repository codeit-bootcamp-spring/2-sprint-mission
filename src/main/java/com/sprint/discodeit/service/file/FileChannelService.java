package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.dto.channelDto.ChannelCreateRequestDto;
import com.sprint.discodeit.domain.dto.channelDto.ChannelResponseDto;
import com.sprint.discodeit.domain.entity.Channel;
import com.sprint.discodeit.domain.entity.ReadStatus;
import com.sprint.discodeit.domain.mapper.ChannelMapper;
import com.sprint.discodeit.repository.ChannelRepository;
import com.sprint.discodeit.repository.file.ReadStatusRepository;
import com.sprint.discodeit.service.ChannelServiceV1;
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
        // 저장
        channelRepository.save(channelMapper);
        if(readStatus != null){
            readStatusRepository.save(readStatus);
        }
        return new ChannelResponseDto(channelMapper.getName(), channelMapper.getType() , readStatus);
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException(channelId.toString() + " 없는 체널 입니다"));;
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findByAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException(channelId.toString()+ " 없는 회원 입니다"));;
        channel.update(newName, newDescription);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.delete(channelId);
    }
}
