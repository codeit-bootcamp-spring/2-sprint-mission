package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
    private final ReadStatusRepository readStatusRepository;
    @Override
    public Channel createPrivate(ChannelCreatePrivateDto channelCreatePrivateDto) {

        Channel newChannel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(newChannel);

        channelCreatePrivateDto.users().forEach(user -> {
            ReadStatus readStatus = new ReadStatus(user.getId(), newChannel.getId());
            readStatusRepository.save(readStatus);
        });

        return newChannel;
    }

    @Override
    public Channel createPublic(ChannelCreatePublicDto channelCreatePublicDto) {
        boolean isExistChannel = channelRepository.findAll().stream()
                .anyMatch(channel -> channel.getName().equals(channelCreatePublicDto.name()));

        if (isExistChannel) {
            throw new RuntimeException(channelCreatePublicDto.name() + " 채널은 이미 존재합니다.");
        }

        Channel newChannel = new Channel(ChannelType.PUBLIC, channelCreatePublicDto.name(),
                channelCreatePublicDto.description());
        channelRepository.save(newChannel);

        return newChannel;
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
