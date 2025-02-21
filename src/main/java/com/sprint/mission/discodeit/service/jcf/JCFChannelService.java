package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    public List<Channel> data;

    public JCFChannelService() {
        data = new ArrayList<>();
    }

    // 채널 생성 메소드
    public void createChannel(Channel channel) {
        data.add(channel);
        System.out.println("---------------------[채널 생성]-------------------------");
        System.out.println("채널 이름: " + channel.getChannelName() + "\n채널 설명: " + channel.getDescription());
        System.out.println("생성 시간: " + channel.getCreatedAtFormatted());
        System.out.println("업데이트 시간: " + channel.getupdatedAttFormatted());
        System.out.println("--------------------------------------------------------");

    }

    // 채널 단일 조회 메소드
    public Channel getChannel(String channelName) {
        for(Channel ch : data) {
            if(ch.getChannelName().equals(channelName)) {
                return ch;
            }
        }
        return null;
    }
    public void findChannel(String channelName) {
        Channel ch = getChannel(channelName);
        if(ch != null) {
            System.out.println("---------------------[채널 조회 결과]----------------------");
            System.out.println("채널 이름: " + ch.getChannelName() + "채널 설명: " + ch.getDescription());
            System.out.println("생성 시간: " + ch.getCreatedAtFormatted());
            System.out.println("업데이트 시간: " + ch.getupdatedAttFormatted());
            System.out.println("---------------------------------------------------------");
        } else{
            System.out.println("---------------------------------------------------------");
            System.out.println("[조회 한 채널이 존재하지 않습니다]");
            System.out.println("---------------------------------------------------------");
        }
    }

    // 채널 전체 조회 메소드
    public List<Channel> getAllChannels(){
        data.forEach(ch ->
                System.out.println(ch.getChannelName()));
        return data;
    }


    // 채널 정보 수정 메소드
    public void updateUser(UUID id, String channelName, String description){

    }

    // 채널 삭제 메소드
    public void deleteUser(UUID id) {

    }

}
