package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final ChannelRepository repository;

    public FileChannelService(String filename) {
        this.repository = new FileChannelRepository(filename);
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        repository.save(channel);
        System.out.println("채널 생성 및 저장 완료: " + channel.getId());
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        System.out.println("채널 조회: " + channelId);
        return repository.findById(channelId);
    }

    @Override
    public List<Channel> findAll() {
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
        System.out.println("채널 삭제 및 저장 완료: " + channelId);
    }
}
