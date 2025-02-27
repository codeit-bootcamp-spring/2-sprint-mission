package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import java.util.Comparator;
import java.util.List;


public class JavaApplication {
    public static void main(String[] args) {

        System.out.println("--유저 테스트--");
        System.out.println("--유저 등록--");
        JCFUserService jcfuser = new JCFUserService();
        jcfuser.createUser("가영");
        jcfuser.createUser("나영");
        jcfuser.createUser("다영");

        System.out.println("--유저 조회(단건)--");
        User user1 = jcfuser.getUserByName("가영");
        User user2 = jcfuser.getUserByName("나영");
        User user3 = jcfuser.getUserByName("다영");

        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);

        System.out.println("--유저 조회(다건)--");
        List<User> users = jcfuser.getAllUsers();
        users.stream()
                .sorted(Comparator.comparing(User::getUsername))
                .forEach(System.out::println);

        // 수정
        System.out.println("--유저 수정--");
        user1 = jcfuser.updateUserName(user1.getId(), "Amy");
        user2 = jcfuser.updateUserName(user2.getId(), "Bamy");
        user3 = jcfuser.updateUserName(user3.getId(), "Camy");

        //수정된 데이터 조회
        System.out.println("--유저 수정된 데이터 조회--");
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);

        // 삭제
        System.out.println("--유저 삭제--");
        jcfuser.deleteUser(user1.getId());

        // 조회를 통해 삭제되었는지 확인
        boolean bool = jcfuser.isDeleted(user1.getId());
        System.out.println(user1.toString(bool));

        bool = jcfuser.isDeleted(user2.getId());
        System.out.println(user2.toString(bool));

        bool = jcfuser.isDeleted(user3.getId());
        System.out.println(user3.toString(bool));


        System.out.println("--채널 테스트--");
        System.out.println("--채널 등록--");

        JCFChannelService jcfchannel = new JCFChannelService();

        jcfchannel.createChannel("channel1", user2);
        jcfchannel.createChannel("channel2", user3);

        System.out.println("--채널 조회(단건)--");
        Channel channel1 = jcfchannel.getChannelByChannelName("channel1");
        Channel channel2 = jcfchannel.getChannelByChannelName("channel2");

        System.out.println(channel1);
        System.out.println(channel2);

        System.out.println("--채널 조회(다건)--");
        List<Channel> channels = jcfchannel.getAllChannels();
        channels.stream()
                .sorted(Comparator.comparing(Channel::getChannelname))
                .forEach(System.out::println);

        System.out.println("--채널 수정--");
        channel1 = jcfchannel.updateChannelName(channel1.getId(), "채널1");
        channel2 = jcfchannel.updateChannelName(channel2.getId(), "채널2");

        //수정된 데이터 조회
        System.out.println("--채널 수정된 데이터 조회--");
        System.out.println(channel1);
        System.out.println(channel2);


        // 삭제
        System.out.println("--채널 삭제--");
        jcfchannel.deleteChannel(channel1.getId());


        // 조회를 통해 삭제되었는지 확인
        boolean boolc = jcfchannel.isDeleted(channel1.getId());
        System.out.println(channel1.toString(boolc));

        boolc = jcfchannel.isDeleted(channel2.getId());
        System.out.println(channel2.toString(boolc));

        System.out.println("--메세지 테스트--");
        System.out.println("--메세지 등록--");

        JCFMessageService jcfmessage = new JCFMessageService();

        jcfmessage.createMessage("Hello", user1, channel2);
        jcfmessage.createMessage("Hi", user2, channel2);

        System.out.println("--메세지 조회(단건)--");
        Message message1 = jcfmessage.getMessageByUser(user1);
        Message message2 = jcfmessage.getMessageByUser(user2);

        System.out.println(message1);
        System.out.println(message2);

        System.out.println("--메세지 조회(다건)--");
        List<Message> messages = jcfmessage.getAllMessages();
        messages.forEach(System.out::println);

        System.out.println("--메세지 수정--");
        message1 = jcfmessage.updateMessage(message1.getId(), "nice to meet you");
        message2 = jcfmessage.updateMessage(message2.getId(), "thank you");

        //수정된 데이터 조회
        System.out.println("--메세지 수정된 데이터 조회--");
        System.out.println(message1);
        System.out.println(message2);


        // 삭제
        System.out.println("--메세지 삭제--");
        jcfmessage.deleteMessage(message1.getId());


        // 조회를 통해 삭제되었는지 확인
        boolean boolm = jcfmessage.isDeleted(message1.getId());
        System.out.println(message1.toString(boolm));

        boolm = jcfmessage.isDeleted(message2.getId());
        System.out.println(message2.toString(boolm));


    }
}
