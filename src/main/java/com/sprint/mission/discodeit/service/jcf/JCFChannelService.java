package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    public List<Channel> channelsData;

    public JCFChannelService() {
        channelsData = new ArrayList<>();
    }

    // 채널 생성 메소드
    public void createChannel(Channel channel) {
        channelsData.add(channel);
        System.out.println("---------------------[채널 생성]-------------------------");
        System.out.println("채널 이름: " + channel.getChannelName() + "\n채널 설명: " + channel.getDescription());
        System.out.println("생성 시간: " + channel.getCreatedAtFormatted());
        System.out.println("업데이트 시간: " + channel.getupdatedAttFormatted());
        System.out.println("--------------------------------------------------------");

    }

    // 채널 단일 조회 메소드
    public Channel getChannel(String channelName) {
        for (Channel ch : channelsData) {
            if (ch.getChannelName().equals(channelName)) {
                return ch;
            }
        }
        return null;
    }

    public void findChannel(String channelName) {
        Channel ch = getChannel(channelName);
        if (ch != null) {
            System.out.println("---------------------[채널 조회 결과]----------------------");
            System.out.printf("채널 이름: %-5s채널 설명: %s\n", ch.getChannelName(), ch.getDescription());
            System.out.println("생성 시간: " + ch.getCreatedAtFormatted());
            System.out.println("업데이트 시간: " + ch.getupdatedAttFormatted());
            System.out.println("---------------------------------------------------------");
        } else {
            System.out.println("---------------------------------------------------------");
            System.out.println("[조회 한 채널이 존재하지 않습니다]");
            System.out.println("---------------------------------------------------------");
        }
    }

    // 채널 전체 조회 메소드
    public List<Channel> getAllChannels() {
        System.out.println("-------------------[채널 전체 조회 결과]--------------------");
        channelsData.forEach(ch ->
                System.out.printf("채널 이름: %-5s 채널 설명 : %s\n생성 시간: %s\n업데이트 시간: %s\n\n",
                        ch.getChannelName(), ch.getDescription(), ch.getCreatedAtFormatted(), ch.getupdatedAttFormatted()));
        System.out.println("---------------------------------------------------------");
        return channelsData;
    }


    // 채널 정보 수정 메소드
    public void updateChannel(String channelName, String changeChannel, String changeDescription) {
        String oldChannelName;
        for (Channel ch : channelsData) {
            if (ch.getChannelName().equals(channelName)) {
                oldChannelName = ch.getChannelName();
                ch.updateChannel(changeChannel, changeDescription);
                System.out.println("-------------------[채널 정보 수정 결과]--------------------");
                System.out.printf("기존 채널: %-5s새로운 채널: %-5s 새로운 채널 설명: %s%n", oldChannelName, ch.getChannelName(), ch.getDescription());
                System.out.println("---------------------------------------------------------");
                return;
            }
        }
        System.out.println("---------------------------------------------------------");
        System.out.println("[입력 한 채널이 존재하지 않습니다]");
        System.out.println("---------------------------------------------------------");

    }

    // 채널 삭제 메소드
    public void deleteChannel(String channelName) {
        for (Channel ch : channelsData) {
            if (ch.getChannelName().equals(channelName)) {
                channelsData.remove(ch);
                System.out.println("---------------------[채널 삭제 결과]----------------------");
                System.out.printf("삭제 된 채널: %s\n", ch.getChannelName());
                System.out.println("---------------------------------------------------------");
                return;
            }
        }
        System.out.println("---------------------------------------------------------");
        System.out.println("[입력 한 채널이 존재하지 않습니다]");
        System.out.println("---------------------------------------------------------");

    }

}
