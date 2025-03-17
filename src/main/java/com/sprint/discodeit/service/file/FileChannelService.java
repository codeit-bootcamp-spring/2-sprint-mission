package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.entity.Channel;
import com.sprint.discodeit.domain.entity.ChannelType;
import com.sprint.discodeit.repository.ChannelRepository;
import com.sprint.discodeit.service.ChannelService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId.toString()).orElseThrow(() -> new NoSuchElementException(channelId.toString() + " 없는 체널 입니다"));;
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findByAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = channelRepository.findById(channelId.toString()).orElseThrow(() -> new NoSuchElementException(channelId.toString()+ " 없는 회원 입니다"));;
        channel.update(newName, newDescription);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        channelRepository.delete(channelId);
    }
}
