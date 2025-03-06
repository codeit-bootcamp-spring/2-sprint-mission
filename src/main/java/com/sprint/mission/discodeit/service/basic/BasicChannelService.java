package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private static BasicChannelService instance;
    private final ChannelRepository channelRepository;

    private BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public static synchronized BasicChannelService getInstance(ChannelRepository channelRepository) {
        if (instance == null) {
            instance = new BasicChannelService(channelRepository);
        }
        return instance;
    }

    @Override
    public void create(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("채널 정보가 NULL입니다.");
        }

        if (channelRepository.find(channel.getId()) != null) {
            throw new RuntimeException("채널이 이미 존재합니다.");
        }
        channelRepository.create(channel);
        System.out.println("[" + channel +"] 생성 완료 " + channel.getId());
    }

    @Override
    public Channel find(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID가 NULL입니다.");
        }

        if (channelRepository.find(id) == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        System.out.println("선택한 채널을 조회합니다.");
        return channelRepository.find(id);
    }

    @Override
    public List<Channel> findAll() {
        if (channelRepository.findAll().isEmpty()) {
            System.out.println("등록된 채널이 없습니다.");
        }
        System.out.println("모든 채널을 조회합니다.");
        return channelRepository.findAll();
    }

    @Override
    public void update(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("채널 정보가 NULL입니다.");
        }

        if (channelRepository.find(channel.getId()) == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        channelRepository.update(channel);
        System.out.println("[" + channel +"] 수정 완료 " + channel.getId());
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID가 NULL입니다.");
        }

        if (channelRepository.find(id) == null) {
            throw new RuntimeException("채널이 존재하지 않습니다.");
        }
        channelRepository.delete(id);
        System.out.println("채널 삭제를 완료하였습니다.");
    }
}
