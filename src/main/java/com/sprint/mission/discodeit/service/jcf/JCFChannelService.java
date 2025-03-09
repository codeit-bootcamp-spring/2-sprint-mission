package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository repository;

    public JCFChannelService() {
        this.repository = new JCFChannelRepository();
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        repository.save(channel);
        System.out.println("✅ 채널 생성 완료: " + channel.getId());
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        System.out.println("채널 조회: " + channelId);
        return repository.findById(channelId);
    }

    @Override
    public List<Channel> findAll() {
        System.out.println("모든 채널 조회");
        return repository.findAll();
    }

    @Override
    public Channel updateName(UUID channelId, String newName) {
        Channel channel = repository.findById(channelId);
        channel.updateName(newName);
        repository.save(channel);
        System.out.println("채널 이름 변경 완료: " + newName);
        return channel;
    }

    @Override
    public Channel updateDesc(UUID channelId, String newDescription) {
        Channel channel = repository.findById(channelId);
        channel.updateDesc(newDescription);
        repository.save(channel);
        System.out.println("채널 설명 변경 완료: " + newDescription);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        repository.delete(channelId);
        System.out.println("채널 삭제 완료: " + channelId);
    }
}
