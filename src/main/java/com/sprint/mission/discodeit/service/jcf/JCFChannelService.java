package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.ChannelSaveDto;
import com.sprint.mission.discodeit.dto.FindChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final List<Channel> channelList = new ArrayList<>();

    @Override
    public ChannelSaveDto createPublicChannel(String channelName, ChannelType channelType) {
        Channel channel = new Channel(channelName, channelType);
        channelList.add(channel);
        System.out.println("채널 개설 성공" + channel);
        return null;
    }

    @Override
    public ChannelSaveDto createPrivateChannel(String channelName, ChannelType channelType, List<UUID> userList) {
        return null;
    }

    @Override
    public FindChannelDto findChannel(UUID id) {
        //for (Channel channel : channelList) {
        //    if (channel.getId().equals(id)) {
        //        return channel;
        //    }
        //}
        //System.out.print("[실패]찾으시는 채널이 존재하지 않습니다.  ");
        return null;
    }

    @Override
    public List<FindChannelDto> findAllByUserId(UUID userUUID) {
        //if (channelList.isEmpty()) {
        //    System.out.println("개설된 채널이 없습니다.");
        //}
        //return channelList;
        return null;
    }

    @Override
    public void updateChannel(UUID uuid, String channelName) {
        if (channelList.stream().noneMatch(data -> data.getId().equals(uuid))) {
            System.out.println("[실패]수정하려는 채널이 존재하지 않습니다.");
            return;
        }

        for (Channel channel : channelList) {
            if (channel.getId().equals(uuid)) {
                channel.updateChannelName(channelName);
                System.out.println("[성공]채널 변경 완료[채널 아이디: " + channel.getId() +
                        ", 채널명: " + channel.getChannelName() +
                        ", 변경 시간: " + channel.getUpdatedAt() + "]");
            }
        }
    }

    @Override
    public void deleteChannel(UUID uuid) {
        boolean isremove = channelList.removeIf(data -> data.getId().equals(uuid));

        if (!isremove) {
            System.out.println("[실패]삭제하려는 채널이 존재하지 않습니다.");
        } else {
            System.out.println("[성공]채널 삭제 완료");
        }
    }

}
