package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void createChannel(String channelName) {
        Channel channel = channelRepository.channelSave(channelName);
        if (channel == null) {
            System.out.println("[실패] 채널 저장 실패");
            return;
        }
        System.out.println("[성공]" + channel);
    }

    @Override
    public Channel findChannel(UUID channelUUID) {
        return channelRepository.findChannelById(channelUUID)
                .orElseGet(() -> {
                    System.out.println("채널이 존재하지 않습니다.");
                    return null;
                });
    }

    @Override
    public List<Channel> findAllChannel() {
        List<Channel> channelList = channelRepository.findAllChannel();
        if (channelList.isEmpty()) {
            System.out.println("채널이 존재하지 않습니다.");
        }

        return channelList;
    }

    @Override
    public void updateChannel(UUID channelUUID, String channelName) {
        Channel channel = channelRepository.updateChannelChannelName(channelUUID, channelName);
        if (channel == null) {
            System.out.println("[실패] 채널 변경 실패");
            return;
        }
        System.out.println("[성공]" + channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        boolean isDeleted = channelRepository.deleteChannelById(id);
        if (!isDeleted) {
            System.out.println("[실패] 채널 삭제 실패");
            return;
        }
        System.out.println("[성공] 채널 삭제 완료");
    }
}
