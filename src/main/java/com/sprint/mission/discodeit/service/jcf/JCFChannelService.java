package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.TimeFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    Map<UUID, Channel> channelRepository = new HashMap<>();

    @Override
    public void saveChannel(Channel channel) {
        channelRepository.put(channel.getId(), channel);
    }

    @Override
    public void findAll() {
        List<Channel> channelList = channelRepository.values().stream().toList();

        if(channelList.isEmpty()){
            System.out.println("등록된 채널이 없습니다.");
        }else {
            for (Channel channel : channelList) {
                System.out.println("채널 ID: " + channel.getId());
                System.out.println("채널 이름: " + channel.getName());
                System.out.println("채널 생성 일자: " + TimeFormatter.format(channel.getCreatedAt()));
                System.out.println("채널 수정 일자: " + TimeFormatter.format(channel.getUpdatedAt()));
                System.out.println("----------------------------------");
            }
        }
    }

    @Override
    public void findByName(String name) {
        List<Channel> channelList = channelRepository.values().stream()
                                .filter(channel -> channel.getName()
                                        .equalsIgnoreCase(name)).toList();

        if(channelList.isEmpty()){
            System.out.println("이름이 " + name + " 인 채널이 없습니다.");
        }else{
            System.out.println("[" + name + "]" + " 채널 검색 결과");
            for (Channel channel : channelList) {
                System.out.println("채널 ID: " + channel.getId());
                System.out.println("채널 이름: " + channel.getName());
                System.out.println("채널 생성 일자: " + TimeFormatter.format(channel.getCreatedAt()));
                System.out.println("채널 수정 일자: " + TimeFormatter.format(channel.getUpdatedAt()));
                System.out.println("----------------------------------");
            }
        }
    }

    @Override
    public void update(UUID id, String name) {
        if(!channelRepository.containsKey(id)){
            System.out.println("없는 채널 입니다.");
        }
        channelRepository.get(id).setName(name);
        channelRepository.get(id).setUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public void delete(UUID id) {
        channelRepository.remove(id);
    }
}
