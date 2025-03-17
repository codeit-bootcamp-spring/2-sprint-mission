package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public Channel create(ChannelType type, String name, String description){
        Map<UUID, Channel> channelData = channelRepository.getChannelData();

        if(channelData.values().stream()
                .anyMatch(channel -> channel.getName().equals(name))){
            throw new IllegalArgumentException("같은 이름을 가진 채널이 있습니다.");
        }
        Channel channel = new Channel(type, name, description);

        return channelRepository.save(channel);
    }

    public Channel find(UUID channelId){
        return channelRepository.findById(channelId);

    }

    public List<Channel> findAll(){
        return channelRepository.findAll();
    }

    public Channel update(UUID channelId, String newName, String newDescription){
        Map<UUID, Channel> channelData = channelRepository.getChannelData();
        Channel channelNullable = channelData.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        return channelRepository.update(channel, newName, newDescription);
    }

    public void delete(UUID channelId){
        Map<UUID, Channel> channelData = channelRepository.getChannelData();
        if (!channelData.containsKey(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }

        channelRepository.delete(channelId);
    }
}
