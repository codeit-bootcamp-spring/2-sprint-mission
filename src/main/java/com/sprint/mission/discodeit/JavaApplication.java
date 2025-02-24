package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserservice;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserservice userservice = JCFUserservice.getInstance();
        JCFChannelService channelservice = JCFChannelService.getInstance();
        JCFMessageService messageservice = JCFMessageService.getInstance();

        // user 등록
        UUID user1 = userservice.createUser();
        UUID user2 = userservice.createUser();
        UUID user3 = userservice.createUser();
        //user 전체 조회
        userservice.searchAllUsers();
        //특정 user 조회
        userservice.searchUser(user2);
        //user 수정
        userservice.updateUser(user2);
        //수정된 user 조회
        userservice.searchUser(user2);
        //user 삭제
        userservice.deleteUser(user1);
        //삭제 확인
        userservice.searchUser(user1);


        // Channel 등록
        UUID channel1 = channelservice.createChannel();
        UUID channel2 = channelservice.createChannel();
        //channel 전체 조회
        channelservice.searchAllChannels();
        //특정 channel 조회
        channelservice.searchChannel(channel2);
        //channel 수정
        channelservice.updateChannel(channel2);
        //수정된 channel 조회
        channelservice.searchChannel(channel2);
        //channel 삭제
        channelservice.deleteChannel(channel1);
        //삭제 확인
        channelservice.searchChannel(channel1);


        //Message 등록
        UUID message1 = messageservice.createMessage();
        UUID message2 = messageservice.createMessage();
        //message 전체 조회
        messageservice.searchAllMessages();
        //특정 channel 조회
        messageservice.searchMessage(message2);
        //channel 수정
        messageservice.updateMessage(message2);
        //수정된 channel 조회
        messageservice.searchMessage(message2);
        //channel 삭제
        messageservice.deleteMessage(message1);
        //삭제 확인
        messageservice.searchMessage(message1);


    }
}
