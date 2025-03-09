package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private static BasicChannelService instance; // 싱글톤 인스턴스
    private final ChannelRepository channelRepository; // 의존성 주입

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
    public UUID createChannel() {
        Channel channel = new Channel();
        channelRepository.save(channel);
        System.out.println("채널이 생성되었습니다: " + channel);
        return channel.getId();
    }

    @Override
    public void searchChannel(UUID id) {
        Channel channel = channelRepository.findById(id);
        if (channel == null) {
            System.out.println("조회하신 채널이 존재하지 않습니다.");
            return;
        }
        System.out.println("CHANNEL: " + channel);
    }

    @Override
    public void searchAllChannels() {
        List<Channel> channels = channelRepository.findAll();
        if (channels.isEmpty()) {
            System.out.println("등록된 채널이 없습니다.");
            return;
        }
        channels.forEach(channel -> System.out.println("CHANNEL: " + channel));
    }

    @Override
    public void updateChannel(UUID id) {
        Channel channel = channelRepository.findById(id);
        if (channel == null) {
            System.out.println("업데이트할 채널이 존재하지 않습니다.");
            return;
        }
        channel.updateTime(System.currentTimeMillis());
        channelRepository.update(channel);
        System.out.println(id + " 채널 업데이트 완료되었습니다.");
    }

    @Override
    public void deleteChannel(UUID id) {
        if (!channelRepository.existsById(id)) {
            System.out.println("삭제할 채널이 존재하지 않습니다.");
            return;
        }
        channelRepository.delete(id);
        System.out.println(id + " 채널 삭제 완료되었습니다.");
    }
}
