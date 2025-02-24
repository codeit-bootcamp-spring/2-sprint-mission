package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private Map<UUID, Channel> data;
    private static JCFChannelService instance = null;

    public static JCFChannelService getInstance() {
        if (instance == null) {
            instance = new JCFChannelService();
        }
        return instance;
    }

    private JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public UUID createChannel() {
        Channel channel = new Channel();
        data.put(channel.getId(), channel);
        System.out.println("채널이 생성되었습니다: \n" + channel);
        return channel.getId();
    }

    @Override
    public void searchChannel(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("조회하신 채널이 존재하지 않습니다.");
            return;
        }
        System.out.println("CHANNEL: " + data.get(id));

    }

    @Override
    public void searchAllChannels() {
        if (data.isEmpty()) {
            System.out.println("등록된 채널이 존재하지 않습니다.");
            return;
        }
        for (Channel channel : data.values()) {
            System.out.println("CHANNEL: " + channel);
        }
    }

    @Override
    public void updateChannel(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("업데이트할 채널이 존재하지 않습니다.");
            return;
        }
        data.get(id).update();
        System.out.println(id + " 채널 업데이트 완료되었습니다.");

    }

    @Override
    public void deleteChannel(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("삭제할 채널이 존재하지 않습니다.");
            return;
        }
        data.remove(id);
        System.out.println(id + " 채널 삭제 완료되었습니다.");

    }
}
