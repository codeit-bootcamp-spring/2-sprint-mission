package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService(userService, channelService);

        System.out.println("=== 사용자 테스트 ===");
        User user1 = new User("최규원");
        userService.create(user1);
        User user2 = new User("홍성준");
        userService.create(user2);
        User user3 = new User("차종민");
        userService.create(user3);

        System.out.println("특정 사용자 조회 : " + userService.find(user1.getId()));
        System.out.println("특정 사용자 조회 : " + userService.find(user2.getId()));
        System.out.println("특정 사용자 조회 : " + userService.find(user3.getId()));

        user1.update("조정연");
        userService.update(user1);
        System.out.println("수정된 사용자 조회 : " + userService.find(user1.getId()));

        userService.delete(user1.getId());
        System.out.println("유저1 삭제");

        System.out.println("모든 사용자 조회 : " + userService.findAll());

        System.out.println("=== 채널 테스트 ===");
        Channel channel1 = new Channel("개인 채널");
        channelService.create(channel1);
        Channel channel2 = new Channel("팀 채널");
        channelService.create(channel2);
        Channel channel3 = new Channel("학습 채널");
        channelService.create(channel3);

        System.out.println("개인 채널 조회 : " + channelService.find(channel1.getId()));
        System.out.println("개인 채널 조회 : " + channelService.find(channel2.getId()));
        System.out.println("개인 채널 조회 : " + channelService.find(channel3.getId()));

        channel1.updateChannelName("과제 채널");
        channelService.update(channel1);
        System.out.println("수정된 채널 조회 : " + channelService.find(channel1.getId()));

        channelService.delete(channel2.getId());
        System.out.println("채널2 삭제");

        System.out.println("모든 채널 조회 : " + channelService.findAll());

        System.out.println("=== 메시지 테스트 ===");
        Message message1 = new Message(user2, "안녕하세요.", channel1);
        messageService.create(message1);
        Message message2 = new Message(user1, "테스트입니다.", channel1);
        messageService.create(message2);
        Message message3 = new Message(user3, "좋은 아침입니다!", channel3);
        messageService.create(message3);
        Message message4 = new Message(user2, "식사 맛있게 하세요.", channel3);
        messageService.create(message4);

        System.out.println("메시지1 조회 : " + messageService.find(message1.getId()));

        message1.updateContent("수정된 메시지입니다.");
        messageService.update(message1);
        System.out.println("수정된 메시지1 조회 : " + messageService.find(message1.getId()));

        messageService.delete(message1.getId());
        System.out.println("메시지1 삭제");

        System.out.println("모든 메시지 조회 : " + messageService.findAll());
    }
}