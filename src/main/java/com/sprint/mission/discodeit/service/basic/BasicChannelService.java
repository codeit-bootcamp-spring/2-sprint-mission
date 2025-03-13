package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public Channel create(ChannelType type, String name, String description) {
        boolean isExistChannel = findAll().stream()
                .anyMatch(channel -> channel.getName().equals(name)); // 같은 이름 == 같은 채널

        if (isExistChannel) {
            throw new RuntimeException(name + " 채널은 이미 존재합니다.");
        }

        Channel newChannel = new Channel(type, name, description);

        return channelRepository.save(newChannel);
    }

    @Override
    public Channel findById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);

        if (channel == null) {
            throw new NoSuchElementException(channelId + " 채널을 찾을 수 없습니다.");
        }

        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        boolean isExistChannel = findAll().stream().anyMatch(channel -> channel.getName().equals(newName));

        if (isExistChannel) {
            throw new RuntimeException(channelId + " 채널이 이미 존재해 수정할 수 없습니다.");
        }

        Channel channel = findById(channelId);
        channel.update(newName, newDescription);

        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = findById(channelId);
        channelRepository.delete(channel.getId());
    }
}
