package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.UserEntity;
import com.sprint.mission.discodeit.entity.ChannelEntity;
import com.sprint.mission.discodeit.entity.MessageEntity;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;
import java.util.Optional;

public class JavaApplication {
    public static void main(String[] args) {
        //User
        JCFUserService userService = new JCFUserService();
        UserEntity user1 = new UserEntity("username1", "nickname1", "010-1111-1111", "user1@google.com","password1" );
        UserEntity user2 = new UserEntity("username2", "nickname2", "010-2222-2222", "user2@google.com","password2" );
        UserEntity user3 = new UserEntity("username3", "nickname3", "010-3333-3333", "user3@google.com","password3" );
        UserEntity user4 = new UserEntity("username4", "nickname4", "010-4444-4444", "user4@google.com","password4" );
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);
        System.out.println("사용자 등록: ");
        userService.findAll().forEach(user -> System.out.println("- " + user.getUsername()));

        Optional<UserEntity> foundUser = userService.findById(user1.getId());
        foundUser.ifPresent(user -> System.out.println("조회한 사용자: " + user.getUsername()));

        userService.updateUsername(user1.getId(), "useruser");
        System.out.println("사용자명 변경: " + user1.getUsername());

        System.out.println("삭제할 사용자: " + user3.getUsername());
        userService.deleteById(user3.getId());
        System.out.println("사용자명 삭제 완료");

        System.out.println("현재 사용자 목록: ");
        userService.findAll().forEach(user -> System.out.println("- " + user.getUsername()));

        //Channel
        JCFMessageService messageService = new JCFMessageService(userService);
        JCFChannelService channelService = new JCFChannelService(messageService);


        ChannelEntity channel1 = new ChannelEntity("channelHi1", "text");
        ChannelEntity channel2 = new ChannelEntity("channelHi2", "text");
        ChannelEntity channel3 = new ChannelEntity("channelHi3" , "text");
        ChannelEntity channel4 = new ChannelEntity("channelHi4", "text");
        channelService.create(channel1);
        channelService.create(channel2);
        channelService.create(channel3);
        channelService.create(channel4);
        System.out.println("======================\n채널 등록: ");
        channelService.findAll().forEach(channel -> System.out.println("- " + channel.getName()));

        Optional <ChannelEntity> foundChannel = channelService.getChannelByName("channelHi1");
        foundChannel.ifPresent(channel -> System.out.println("조회한 채널: "+channel.getName()));

        channelService.updateChannelName("channelHi1","channelBye1");

        Optional<ChannelEntity> updatedChannelOptional = channelService.getChannelByName("channelBye1");

        ChannelEntity updatedChannel = updatedChannelOptional.get();
        System.out.println("채널명 변경: "+ updatedChannel.getName());

        System.out.println("삭제할 채널: " + channel2.getId());
        channelService.deleteById(channel2.getId());
        System.out.println("채널 삭제 완료");

        System.out.println("현재 채널 목록: ");
        channelService.findAll().forEach(channel -> System.out.println("- " + channel.getName()));

        //Message
        MessageEntity message1 = new MessageEntity("안녕하세요 !", user1,channel1);
        MessageEntity message2 = new MessageEntity("좋은",user2,channel2);
        MessageEntity message3 = new MessageEntity("아침", user3,channel3);
        MessageEntity message4 = new MessageEntity("입니다:)", user4, channel4);
        messageService.create(message1);
        messageService.create(message2);
        messageService.create(message3);
        messageService.create(message4);
        System.out.println("======================\n메시지 등록: ");
        messageService.findAll().forEach(message -> System.out.println("- " + message.getContent()));

        Optional <MessageEntity> foundMessage = messageService.getMessageById(message3.getId());
        foundMessage.ifPresent(message -> System.out.println("조회한 메시지: " + message.getContent()));

        messageService.updateMessage(message3.getId(),"저녁");
        System.out.println("수정할 메시지: " + message3.getContent());

        System.out.println("삭제할 메시지: " + message1.getContent());
        messageService.deleteById(message1.getId());
        System.out.println("메시지 삭제 완료");

        System.out.println("현재 메시지 목록: ");
        messageService.findAll()
            .stream()
                .sorted((m1,m2) -> m1.getId().toString().compareTo(m2.getId().toString()))
                .forEach(message -> System.out.println("- " + message.getContent()));


    }
}
