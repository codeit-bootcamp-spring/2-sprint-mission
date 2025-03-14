package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public Channel create(String channelName, String description) {
        Channel channel = new Channel(channelName, description);
        Optional<Channel> ChannelList = channelRepository.load().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findAny();
        if (ChannelList.isPresent()) {
            throw new IllegalArgumentException("등록된 채널이 존재합니다.");
        } else {
            channelRepository.save(channel);
            System.out.println(channel);
            return channel;
        }
    }


    @Override
    public Channel getChannel(UUID channelId) {
        Optional<Channel> channel = channelRepository.load().stream()
                .filter(c -> c.getId().equals(channelId))
                .findAny();
        return channel.orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다"));

    }

    @Override
    public List<Channel> getAllChannel() {
        List<Channel> channelList = channelRepository.load();
        if (channelList.isEmpty()) {
            System.out.println("전체 조회 결과가 없습니다.");
            return Collections.emptyList();
        }
        return channelList;
    }

    @Override
    public Channel update(UUID channelId, String changeChannel, String changeDescription) {
        Channel channel = getChannel(channelId);
        channel.updateChannel(changeChannel, changeDescription);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = getChannel(channelId);
        channelRepository.remove(channel);
    }
}
