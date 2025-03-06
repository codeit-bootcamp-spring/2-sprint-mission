package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class JCFChannelService implements ChannelService {

    public List<Channel> channelsData;

    public JCFChannelService() {
        channelsData = new ArrayList<>();
    }

    // 채널 생성
    @Override
    public Channel create(Channel channel) {
        if (!validateChannel(channel)) {
            return null;
        }
        return createChannel(channel);
    }

    private Channel createChannel(Channel channel) {
        channelsData.add(channel);
        System.out.println(channel);
        return channel;
    }

    private boolean validateChannel(Channel channel) {
        if (getChannel(channel.getChannelName()) != null) {
            System.out.println("등록된 채널이 존재합니다.");
            return false;
        }
        return true;
    }


    // 채널 단일 조회
    @Override
    public Channel getChannel(String channelName) {
        return findChannel(channelName);
    }

    private Channel findChannel(String channelName) {
        for (Channel channelList : channelsData) {
            if (channelList.getChannelName().equals(channelName)) {
                return channelList;
            }
        }
        return null;
    }


    // 채널 전체 조회
    @Override
    public List<Channel> getAllChannel() {
        if (channelsData.isEmpty()) {
            System.out.println("전체 조회 결과가 없습니다.");
            return Collections.emptyList();
        }
        for (Channel channelList : channelsData) {
            System.out.println(channelList);
        }
        return channelsData;
    }


    // 채널 정보 수정
    @Override
    public Channel update(String channelName, String changeChannel, String changeDescription){
        return updateChannel(channelName, changeChannel, changeDescription);
    }

    private Channel updateChannel(String channelName, String changeChannel, String changeDescription){
        Channel channel = getChannel(channelName);
        if (channel != null) {
            channel.updateChannel(changeChannel, changeDescription);
            System.out.printf("[ %s ], [ %s ] 로 변경되었습니다.", channel.getChannelName(), channel.getDescription());
            return channel;
        }
        System.out.println("입력 한 채널이 존재하지 않습니다");
        return null;
    }


    // 채널 삭제
    @Override
    public void delete(String channelName){
        Channel delChannel = getChannel(channelName);
        if (delChannel != null) {
            channelsData.remove(delChannel);
        }
        System.out.println("삭제 할 채널이 존재하지 않습니다.");
    }

}
