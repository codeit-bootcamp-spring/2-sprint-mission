package com.sprint.mission.discodeit.main;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFMessageService messageService = new JCFMessageService();
        JCFChannelService channelService = new JCFChannelService();

        System.out.println("=== 사용자 테스트 ===");
        User user1 = new User("최규원", 26);
        userService.create(user1);
        User user2 = new User("홍성준", 26);
        userService.create(user2);

        System.out.println("특정 사용자 조회 : " + userService.find(user1.getId()));
        System.out.println("특정 사용자 조회 : " + userService.find(user2.getId()));

        user1.update("조정연", 25);
        userService.update(user1.getId(), user1);
        System.out.println("수정된 사용자 조회" + userService.find(user1.getId()));

        userService.delete(user1.getId());
        System.out.println("유저1 삭제");

        System.out.println("모든 사용자 조회" + userService.findAll());

        System.out.println("=== 채널 테스트 ===");
        Channel channel1 = new Channel("개인 채널");
        channelService.create(channel1);
        Channel channel2 = new Channel("팀 채널");
        channelService.create(channel2);

        System.out.println("개인 채널 조회 : " + channelService.find(channel1.getId()));
        System.out.println("개인 채널 조회 : " + channelService.find(channel2.getId()));

        channel1.updateChannelName("과제 채널");
        channelService.update(channel1.getId(), channel1);
        System.out.println("수정된 채널 조회 : " + channelService.find(channel1.getId()));

        channelService.delete(channel1.getId());
        System.out.println("채널1 삭제");

        System.out.println("모든 채널 조회 : " + channelService.findAll());

        System.out.println("=== 메시지 테스트 ===");
        Message message1 = new Message(user2, "안녕하세요.", channel2);
        messageService.create(message1);

        System.out.println("메시지1 조회 : " + messageService.find(message1.getId()));

        message1.updateContent("수정된 메시지입니다.");
        messageService.update(message1.getId(), message1);
        System.out.println("수정된 메시지1 조회 : " + messageService.find(message1.getId()));

        messageService.delete(message1.getId());
        System.out.println("메시지1 삭제");

        System.out.println("모든 메시지 조회 : " + messageService.findAll());
    }
}