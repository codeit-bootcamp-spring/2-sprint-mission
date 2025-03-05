package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.DuplicatedUserException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        UserRepository userRepository = JCFUserRepository.getInstance();
        ChannelRepository channelRepository = JCFChannelRepository.getInstance();
        MessageRepository messageRepository = JCFMessageRepository.getInstance();
        UserService userService = new JCFUserService(userRepository);
        ChannelService channelService = new JCFChannelService(userService, channelRepository);
        MessageService messageService = new JCFMessageService(userService, channelService, messageRepository);

        // User Service 테스트
        // 유저 등록 테스트
        System.out.println("=========== 유저 생성 및 유저 리스트 조회 테스트 ===========");
        System.out.println("=========== 예상 결과: 유저 7명 나옴 ===========");
        User user1 = userService.createUser("Han", "Han@gmail.com", "", "hello I'm sam");
        User user2 = userService.createUser("Kim", "Kim@gmail.com", "dog pic", "I like dogs");
        User user3 = userService.createUser("Nick", "Nick@ggg.io", "cat pic", "I love cats");
        User user4 = userService.createUser("Jack", "Jack@harlow.co", "", "");
        User user5 = userService.createUser("Jamie", "Jamie@naver.com", "korean flag", "I am Korean");
        User user6 = userService.createUser("Mr.delete", "delete@naver.com", "XXXXX", "I am gonna be deleted soon");
        User user7 = userService.createUser("Oreo", "delete2@naver.com", "", "");

        System.out.println("=========== 바로 아래에 중복된 이메일 나와야 함 ===========");
        try {
            User user8 = userService.createUser("나오지 마", "Jamie@naver.com", "korean", "I am Korean"); // 이메일 중복이므로 생성 안 하고 null 반환
        } catch (DuplicatedUserException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("=========== 바로 아래에 '유효하지 않은 이메일' 나와야 함 ===========");
        try {
            User user9 = userService.createUser("나오면 안 됨", "잘못된 이메일","","");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        userService.getAllUsers().forEach(System.out::println);
        System.out.println("=========== 유저 생성 및 전체 조회 테스트 끝 ===========");
        System.out.println();

        // 유저 읽기
        System.out.println("=========== 특정 유저 get 테스트 ===========");
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
        userService.updateUser(user3.getId(), "Nice", user3.getAvatar(), "");
        System.out.println(userService.getUserByUserId(user3.getId()));
        System.out.println("=========== 유저 update 테스트 끝 ===========");
        System.out.println();

        // 유저 삭제
        System.out.println("=========== 유저 삭제 테스트 ===========");
        System.out.println("=========== 삭제 이전: 7명 ===========");
        userService.getAllUsers().forEach(System.out::println);
        System.out.println("=========== 유저 'Oreo'를 id로 삭제 ===========");
        userService.deleteUserById(user7.getId());
        System.out.println("=========== 유저 'Mr.delete'를 id로 삭제 ===========");
        userService.deleteUserById(user6.getId());
        System.out.println("=========== 삭제 이후: 5명이여야 함 ===========");
        userService.getAllUsers().forEach(System.out::println);
        System.out.println("=========== 유저 삭제 테스트 끝===========");
        System.out.println();



        //////////////////////
        //체널 등록 테스트
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
        try {
            channelService.deleteUserFromChannel(channel1.getId(), user1.getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
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


        // 메시지 서비스 테스트
        System.out.println("=========== 메세지 생성 및 전체 메세지 조회 테스트 ===========");
        System.out.println("=========== 예상 결과: 메세지 4개 ===========");
        Message cm1 = messageService.sendMessage(user1.getId(), "단체 메일 보냄", channel1.getId());
        Message cm2 = messageService.sendMessage(user1.getId(), "하이하이", channel1.getId());
        Message cm3 = messageService.sendMessage(user2.getId(), "난 보내지면 안됨, 채널에 없거든", channel1.getId());
        Message cm4 = messageService.sendMessage(user3.getId(), "하이루", channel1.getId());

        messageService.getAllMessages().forEach(System.out::println);
        System.out.println("=========== 메세지 생성 및 전체 메세지 조회 테스트 끝===========");
        System.out.println();

        System.out.println("=========== 메세지 테스트===========");
        System.out.println("=========== 메세지 id로 메세지 조회 테스트: 내용이 '하이하이' ===========");
        System.out.println(messageService.getMessageById(cm2.getId()));
        System.out.println("=========== 채널 메세지 조회 테스트 끝 ===========");
        System.out.println();

        System.out.println("=========== 메세지 변경 테스트: 변경 이전 내용:'하이하이' ===========");
        System.out.println(messageService.getMessageById(cm2.getId()));
        System.out.println("=========== 메세지 변경 테스트: 변경 이후 내용:헬로우 ===========");
        messageService.updateMessage(cm2.getId(), "헬로우");
        System.out.println(messageService.getMessageById(cm2.getId()));
        System.out.println("=========== 메세지 수정 테스트 끝 ===========");
        System.out.println();

        System.out.println("=========== 메세지 삭제 테스트: Han이 보낸 헬로우 메세지 삭제 ===========");
        messageService.deleteMessage(cm2.getId());
        System.out.println("=========== 총 메세지 2개여야 함 ===========");
        messageService.getAllMessages().forEach(System.out::println);


        System.out.println();
        System.out.println();


    }
}
