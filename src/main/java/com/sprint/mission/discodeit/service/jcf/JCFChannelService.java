package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;


public class JCFChannelService implements ChannelService {

    public List<Channel> channelsData;

    public JCFChannelService() {
        channelsData = new ArrayList<>();
    }

    // 채널 생성
    public void createChannel(Channel channel) {
        if (!channelsData.contains(channel)) {
            channelsData.add(channel);
            System.out.println("-------------------[채널 생성 결과]-----------------------");
            System.out.println("채널 이름: " + channel.getChannelName() + "\n채널 설명: " + channel.getDescription());
            System.out.println("생성 시간: " + channel.getCreatedAtFormatted());
            System.out.println("업데이트 시간: " + channel.getupdatedAttFormatted());
            System.out.println("--------------------------------------------------------");
        } else {
            System.out.println("[Info] 생성된 채널이 존재합니다.");
        }
    }

    // 채널 단일 조회
        public void getChannel(String channelName) {
            boolean found = false;
            for (Channel channelList : channelsData) {
                if (channelList.getChannelName().equals(channelName)) {
                    System.out.println("---------------------[채널 조회 결과]----------------------");
                    System.out.printf("채널 이름: %-5s채널 설명: %s\n", channelList.getChannelName(), channelList.getDescription());
                    System.out.println("생성 시간: " + channelList.getCreatedAtFormatted());
                    System.out.println("업데이트 시간: " + channelList.getupdatedAttFormatted());
                    System.out.println("---------------------------------------------------------");
                    found = true;
                }
                if (!found) {
                    System.out.println("조회 한 채널이 존재하지 않습니다");
                }
            }
        }


    // 채널 전체 조회
    public void getAllChannels() {
        System.out.println("-------------------[채널 전체 조회 결과]--------------------");
        for (Channel channelList : channelsData) {
            System.out.printf("채널 이름: %-10s 채널 설명 : %s\n생성 시간: %s\n업데이트 시간: %s\n\n",
                    channelList.getChannelName(), channelList.getDescription(),
                    channelList.getCreatedAtFormatted(), channelList.getupdatedAttFormatted());
            System.out.println("---------------------------------------------------------");
        }
    }


    // 채널 정보 수정
    public void updateChannel(String channelName, String changeChannel, String changeDescription) {
        String oldChannelName;
        for (Channel channelList : channelsData) {
            if (channelList.getChannelName().equals(channelName)) {
                oldChannelName = channelList.getChannelName();
                channelList.updateChannel(changeChannel, changeDescription);
                System.out.println("-------------------[채널 정보 수정 결과]--------------------");
                System.out.printf("기존 채널: %-8s새로운 채널: %-8s 새로운 채널 설명: %s%n", oldChannelName, channelList.getChannelName(), channelList.getDescription());
                System.out.println("---------------------------------------------------------");
                return;
            }
        }
        System.out.println("입력 한 채널이 존재하지 않습니다");

    }

    // 채널 삭제
    public void deleteChannel(String channelName) {
        for (Channel channelList : channelsData) {
            if (channelList.getChannelName().equals(channelName)) {
                channelsData.remove(channelList);
                System.out.println("---------------------[채널 삭제 결과]----------------------");
                System.out.printf("삭제 된 채널: %s\n", channelList.getChannelName());
                System.out.println("---------------------------------------------------------");
                return;
            }
        }
        System.out.println("입력 한 채널이 존재하지 않습니다");
    }

}
