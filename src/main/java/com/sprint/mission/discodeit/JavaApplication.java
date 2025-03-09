package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {

        //  파일 기반 서비스 인스턴스 생성
        ChannelService channelService = new FileChannelService("channels.ser");

        //  1. 채널 생성
        Channel c1 = channelService.create(ChannelType.PUBLIC, "공개 톡방", "공개된 잡담방입니다.");
        Channel c2 = channelService.create(ChannelType.PRIVATE, "비공개 톡방", "속닥속닥 톡방입니다.");
        System.out.println("채널 생성 완료!");

        // 모든 채널 조회
        System.out.println("\n저장된 채널 목록:");
        printAllChannels(channelService);

        // 특정 채널 조회
        System.out.println("\n특정 채널 조회 (ID: " + c1.getId() + ")");
        System.out.println(channelService.findById(c1.getId()));

        // 채널 이름 업데이트
        System.out.println("\n채널 이름 업데이트");
        channelService.updateName(c1.getId(), "새로운 공개 톡방명 ㅇㅇ");
        printAllChannels(channelService);

        // 채널 설명 업데이트
        System.out.println("채널 설명 업데이트");
        channelService.updateDesc(c1.getId(), "새로운 공개 톡방은 방장 허락하에 얘기할 수 있습니다.");
        printAllChannels(channelService);

        // 채널 아이디로 삭제
        System.out.println("\n채널 삭제 (비공개 톡방)");
        channelService.delete(c2.getId());
        printAllChannels(channelService);

        // 프로그램 재실행 후 데이터 유지 확인 (객체를 새로 생성했지만, 파일이 존재하기 때문에 기존 데이터가 로드됌. 유지됌.)
        System.out.println("\n프로그램 재실행 후 데이터 유지 테스트");
        ChannelService newChannelService = new FileChannelService("channels.ser");
        printAllChannels(newChannelService);

        // ------------------
        System.out.println("--------------");

        // 파일 기반 서비스 인스턴스 생성, 다형성
        UserService userService = new FileUserService("users.ser");

        // 사용자 생성
        User u1 = userService.create("김현호", "password123");
        User u2 = userService.create("현호 킴", "1234");
        User u3 = userService.create("김 ㅇㅇ", "54321");
        System.out.println("사용자 생성 완료!");

        // 모든 사용자 조회
        System.out.println("\n저장된 사용자 목록:");
        printAllUsers(userService);

        // 특정 사용자 조회
        System.out.println("\n특정 사용자 조회 (ID: " + u1.getId() + ")");
        System.out.println(userService.findById(u1.getId()));

        // 사용자 아이디, 비밀번호 업데이트
        System.out.println("\n사용자 비밀번호 업데이트");
        userService.updateName(u1.getId(), "박현호");
        userService.updatePassword(u1.getId(), "newPassword123");
        printAllUsers(userService);

        // 사용자 삭제
        System.out.println("\n사용자 삭제 현호 킴");
        // u2를 삭제했을 경우, 메세지 생성할때 에러남. 근데 아무 출력도 안 나옴...왜지
        userService.delete(u3.getId());
        printAllUsers(userService);

        // 프로그램 재실행 후 데이터 유지 확인
        System.out.println("\n프로그램 재실행 후 데이터 유지 테스트");
        UserService newUserService = new FileUserService("users.ser");
        printAllUsers(newUserService);

        // ------------------------------
        System.out.println("--------------");

        MessageService messageService = new FileMessageService("messages.ser", channelService, userService);

        // 메시지 생성
        Message m1 = messageService.create("안녕하세요!", c1.getId(), u1.getId());
        Message m2 = messageService.create("반가워요!", c1.getId(), u2.getId());
        System.out.println("메시지 생성 완료!");

        // 모든 메시지 조회
        System.out.println("\n저장된 메시지 목록:");
        printAllMessages(messageService);

        // 특정 메시지 조회
        System.out.println("\n특정 메시지 조회 (ID: " + m1.getId() + ")");
        System.out.println(messageService.findById(m1.getId()));

        // 메시지 내용 업데이트
        System.out.println("\n메시지 내용 업데이트");
        messageService.update(m1.getId(), "수정된 메시지!");
        printAllMessages(messageService);

        // 메시지 삭제
        System.out.println("\n메시지 삭제 (m2)");
        messageService.delete(m2.getId());
        printAllMessages(messageService);

        // 프로그램 재실행 후 데이터 유지 확인
        System.out.println("\n프로그램 재실행 후 데이터 유지 테스트");
        MessageService newMessageService = new FileMessageService("messages.ser", channelService, userService);
        printAllMessages(newMessageService);
    }

    private static void printAllMessages(MessageService messageService) {
        List<Message> messages = messageService.findAll();
        if (messages.isEmpty()) {
            System.out.println("저장된 메시지가 없습니다. (출력할게 없음)");
        } else {
            messages.forEach(System.out::println);
        }
    }

    private static void printAllChannels(ChannelService channelService) {
        List<Channel> channels = channelService.findAll();
        if (channels.isEmpty()) {
            System.out.println("저장된 채널이 없습니다. (출력할게 없음)");
        } else {
            channels.forEach(System.out::println); // 메소드 참조 방식
        }
    }

    private static void printAllUsers(UserService userService) {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            System.out.println("저장된 사용자가 없습니다. (출력할게 없음)");
        } else {
            users.forEach(e -> System.out.println(e)); //람다 방식 -> 왜 메소드 참조 방식을 추천할까? 람다가 읽고 이해하기 쉬운거 같은데?
        }
    }
}
