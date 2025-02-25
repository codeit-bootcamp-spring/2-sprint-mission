package com.sprint.mission.discodeit.main;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelMessage;
import com.sprint.mission.discodeit.entity.PrivateMessage;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DuplicatedUserException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        // User Service 테스트
        UserService userService = JCFUserService.getInstance();

        // 유저 등록 테스트
        System.out.println("=========== 유저 생성 및 유저 리스트 조회 테스트 ===========");
        System.out.println("=========== 예상 결과: 유저 7명 나옴 ===========");
        User user1 = null;
        User user2 = null;
        User user3 = null;
        User user4 = null;
        User user5 = null;
        User user6 = null;
        User user7 = null;
        User user8 = null; // 실패할 것
        try {
            user1 = userService.createUser("Han", "Han@gmail.com", "", "hello I'm sam");
            user2 = userService.createUser("Kim", "Kim@gmail.com", "dog pic", "I like dogs");
            user3 = userService.createUser("Nick", "Nick@ggg.io", "cat pic", "I love cats");
            user4 = userService.createUser("Jack", "Jack@harlow.co", "", "");
            user5 = userService.createUser("Jamie", "Jamie@naver.com", "korean flag", "I am Korean");
            user6 = userService.createUser("Mr.delete", "delete@naver.com", "XXXXX", "I am gonna be deleted soon");
            user7 = userService.createUser("Oreo", "delete2@naver.com", "", "");
            System.out.println("=========== 바로 아래에 중복된 이메일 떠야 함 ===========");
            user8 = userService.createUser("나오지 마", "Jamie@naver.com", "korean", "I am Korean"); // 이메일 중복이므로 생성 안 하고 null 반환
        } catch (DuplicatedUserException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        userService.getAllUsers().forEach(System.out::println);
        System.out.println("=========== 유저 생성 및 전체 조회 테스트 끝 ===========");
        System.out.println();

        // 유저 읽기
        System.out.println("=========== 특정 유저 get 테스트 ===========");
        System.out.println("=========== 이메일로 조회: 'Kim' ===========");
        System.out.println(userService.getUserByEmail(user2.getEmail()));
        System.out.println("=========== id로 조회: 'Han' ===========");
        System.out.println(userService.getUserByUserId(user1.getId()));
        System.out.println("=========== 특정 유저 get 테스트 끝 ===========");
        System.out.println();

        // 유저 업데이트
        System.out.println("=========== 유저 update 테스트 ===========");
        System.out.println("=========== 'Nick 유저 업데이트' ===========");
        System.out.println("=========== 'Nick 수정 이전' ===========");
        System.out.println(userService.getUserByUserId(user3.getId()));
        System.out.println("=========== 'Nick 의 닉네임을 Nice로 변경, 상태 메세지 빈칸 만들기 ' ===========");
        userService.updateUser(user3.getId(), "Nice", null, "");
        System.out.println(userService.getUserByUserId(user3.getId()));
        System.out.println("=========== 유저 update 테스트 끝 ===========");
        System.out.println();

        // 유저 삭제
        System.out.println("=========== 유저 삭제 테스트 ===========");
        System.out.println("=========== 삭제 이전: 7명 ===========");
        userService.getAllUsers().forEach(System.out::println);
        System.out.println("=========== 유저 'Oreo'를 id로 삭제 ===========");
        System.out.println("'Oreo' 삭제 성공 여부: " + userService.deleteUserById(user7.getId()));
        System.out.println("=========== 유저 'Mr.delete'를 이메일로 삭제 ===========");
        System.out.println("'Mr.delete' 삭제 성공 여부: " + userService.deleteUserByEmail(user6.getEmail()));
        System.out.println("=========== 삭제 이후: 5명이여야 함 ===========");
        userService.getAllUsers().forEach(System.out::println);
        System.out.println("=========== 유저 삭제 테스트 끝===========");
        System.out.println();



        //////////////////////
        //체널 등록 테스트
        ChannelService channelService = JCFChannelService.getInstance();
        System.out.println("=========== 채널 생성 및 전체 채널 리스트 조회 테스트 ===========");
        System.out.println("=========== 예상 결과: 채널 4개 나옴 ===========");
        Channel channel1 = channelService.createChannel(user1.getId(), "sb02", "코드잇 스프린트 2기");
        Channel channel2 = channelService.createChannel(user2.getId(), "스프링 공부방", "스프링 공부하는중");
        Channel channel3 = channelService.createChannel(user2.getId(), "이거거거거", "아아아아아");
        Channel channel4 = channelService.createChannel(user3.getId(), "스프링 공부방", "다른 방 베낌");

        channelService.getAllChannels().forEach(System.out::println);
        System.out.println("=========== 채널 생성 및 전체 채널 리스트 조회 테스트 끝 ===========");
        System.out.println();

        //조회 테스트
        System.out.println("=========== 채널 조회 테스트 ===========");
        System.out.println("=========== 채널 주인 조회: Han이 나와야 함 ===========");
        UUID ownerId1 = channelService.getChannelOwnerId(channel1.getId());
        System.out.println(userService.getUserByUserId(ownerId1));
        System.out.println("=========== 특정 제목의 채널들 조회: '스프링 공부방' 채널이 2개 나와야 함 ===========");
        channelService.getChannelsByTitle("스프링 공부방").forEach(System.out::println);
        System.out.println("=========== 채널 id로 채널 조회: sb02 방이 나와야 함 ===========");
        System.out.println(channelService.getChannelByChannelId(channel1.getId()));
        System.out.println("=========== 채널 조회 테스트 끝 ===========");
        System.out.println();

        //채널 멤버 추가 및 조회 테스트
        System.out.println("=========== 채널 멤버 추가,삭제 및 조회 테스트 ===========");
        channelService.addUserToChannel(channel1.getId(), user1.getId());
        channelService.addUserToChannel(channel1.getId(), user2.getId());
        channelService.addUserToChannel(channel1.getId(), user2.getId());
        channelService.addUserToChannel(channel1.getId(), user3.getId());
        System.out.println("=========== Han, Kim, Nice가 나와야 함 ===========");
        channelService.getChannelMembers(channel1.getId()).forEach((u) -> {
            System.out.println(userService.getUserByUserId(u));
        });
        System.out.println("=========== Kim, Han(오너여서 제거 안 됨) 삭제 시도: Han, Nice가 나와야 함 ===========");
        channelService.deleteUserFromChannel(channel1.getId(), user1.getId());
        channelService.deleteUserFromChannel(channel1.getId(), user2.getId());
        channelService.getChannelMembers(channel1.getId()).forEach((u) -> {
            System.out.println(userService.getUserByUserId(u));
        });
        System.out.println("=========== 채널 멤버 추가,삭제 및 조회 테스트 끝 ===========");
        System.out.println();

        //채널 삭제 테스트
        System.out.println("=========== 채널 삭제 테스트 ===========");
        System.out.println("=========== 채널 목록: 4개 ===========");
        channelService.getAllChannels().forEach(System.out::println);

        System.out.println("=========== 채널 하나 삭제: 3개 남아야 함 ===========");
        channelService.deleteChannelByChannelId(channel4.getId());
        channelService.getAllChannels().forEach(System.out::println);
        System.out.println("=========== 채널 삭제 테스트 끝===========");
        System.out.println();
        System.out.println();


        MessageService messageService = JCFMessageService.getInstance();
        // 메시지 서비스 테스트
        System.out.println("=========== 메세지 생성 및 전체 메세지 조회 테스트 ===========");
        System.out.println("=========== 예상 결과: 메세지 9개 (개인 6개, 채널 3개) ===========");
        PrivateMessage pm1 = messageService.sendPrivateMessage(user1.getId(), "안녕하세요", user2.getId());
        PrivateMessage pm2 = messageService.sendPrivateMessage(user2.getId(), "반가워요", user1.getId());
        PrivateMessage pm3 = messageService.sendPrivateMessage(user1.getId(), "뭐하세요", user2.getId());
        PrivateMessage pm4 = messageService.sendPrivateMessage(user2.getId(), "밥 먹는 중", user1.getId());
        PrivateMessage pm5 = messageService.sendPrivateMessage(user1.getId(), "안녕하세요 Jack", user4.getId());
        PrivateMessage pm6 = messageService.sendPrivateMessage(user4.getId(), "방가방가", user1.getId());
        ChannelMessage cm1 = messageService.sendChannelMessage(user1.getId(), "단체 메일 보냄", channel1.getId());
        ChannelMessage cm2 = messageService.sendChannelMessage(user1.getId(), "하이하이", channel1.getId());
        ChannelMessage cm3 = messageService.sendChannelMessage(user2.getId(), "난 보내지면 안됨, 채널에 없거든", channel1.getId());
        ChannelMessage cm4 = messageService.sendChannelMessage(user3.getId(), "하이루", channel1.getId());

        messageService.getAllMessages().forEach(System.out::println);
        System.out.println("=========== 메세지 생성 및 전체 메세지 조회 테스트 끝===========");
        System.out.println();

        System.out.println("=========== 메세지 테스트===========");
        System.out.println("=========== 메세지 id로 메세지 조회 테스트: 내용이 '방가방가' ===========");
        System.out.println(messageService.getMessageByMessageId(pm6.getId()));

        System.out.println("=========== 송신자 기준 메세지 조회 테스트: Han이 보낸거: 개인 3개, 채널 2개 ===========");
        messageService.getMessagesBySenderId(user1.getId()).forEach(System.out::println);

        System.out.println("=========== 수신자 기준 메세지 조회 테스트: Han이 받은거 3개 ===========");
        messageService.getPrivateMessagesByReceiverId(user1.getId()).forEach(System.out::println);

        System.out.println("=========== 채널 기준 메세지 조회 테스트: 채널 1에 메세지 3개 ===========");
        messageService.getChannelMessagesByChannelId(channel1.getId()).forEach(System.out::println);
        System.out.println("=========== 채널 메세지 조회 테스트 끝 ===========");
        System.out.println();

        System.out.println("=========== 메세지 변경 테스트: 변경 이전 내용:안녕하세요 ===========");
        System.out.println(messageService.getMessageByMessageId(pm1.getId()));
        System.out.println("=========== 메세지 변경 테스트: 변경 이후 내용:헬로우 ===========");
        messageService.updateMessage(pm1.getId(), "헬로우");
        System.out.println(messageService.getMessageByMessageId(pm1.getId()));
        System.out.println("=========== 메세지 수정 테스트 끝 ===========");
        System.out.println();

        System.out.println("=========== 메세지 삭제 테스트: Han이 보낸 헬로우 메세지 삭제 ===========");
        messageService.deleteMessage(pm1.getId());
        System.out.println("=========== 송신자 기준 메세지 목록: 4개여야 삭제된거임 ===========");
        messageService.getMessagesBySenderId(user1.getId()).forEach(System.out::println);
        System.out.println("=========== 수신자 기준 메세지 목록: 1개여야 삭제된거임 ===========");
        messageService.getPrivateMessagesByReceiverId(user2.getId()).forEach(System.out::println);


    }
}
