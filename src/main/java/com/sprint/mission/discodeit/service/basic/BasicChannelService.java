package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.Collections;
import java.util.List;

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }


    @Override
    public Channel create(Channel channel) {
        if (find(channel.getChannelName()) != null) {
            System.out.println("등록된 채널이 존재합니다.");
            return null;
        } else {
            channelRepository.save(channel);
            System.out.println(channel);
            return channel;
        }
    }

    @Override
    public Channel getChannel(String channelName) {
        return find(channelName);
    }

    private Channel find(String channelName) {
        return channelRepository.load().stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findAny()
                .orElse(null);
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
    public Channel update(String channelName, String changeChannel, String changeDescription) {
        Channel channel = find(channelName);
        if (channel == null) {
            System.out.println("채널이 존재하지 않습니다.");
            return null;
        } else {
            channel.updateChannel(changeChannel, changeDescription);
            channelRepository.save(channel);
            return channel;
        }
    }

    @Override
    public void delete(String channelName) {
        Channel channel = find(channelName);
        if (channel == null) {
            System.out.println("채널이 존재하지 않습니다.");
        } else {
            channelRepository.deleteFromFile(channel);
        }
    }
}
