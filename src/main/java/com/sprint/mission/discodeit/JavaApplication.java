package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService(userService, channelService);
        User loginUser = null;
        Channel enterChannel = null;

        Scanner sc = new Scanner(System.in);

        boolean run = true;
        while (run) {
            System.out.println("1. 유저 관리");
            System.out.println("2. 채널 관리");
            System.out.println("3. 채팅 시작");
            System.out.println("4. 종료");
            System.out.print("선택: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":       //유저
                    boolean userRun = true;
                    while (userRun) {
                        System.out.println("1. 유저 생성");
                        System.out.println("2. 유저 조회");
                        System.out.println("3. 유저 목록");
                        System.out.println("4. 유저 수정");
                        System.out.println("5. 유저 삭제");
                        System.out.println("6. 유저 선택");
                        System.out.println("7. 뒤로 가기");
                        System.out.print("선택 : ");
                        String userChoice = sc.nextLine();
                        switch (userChoice) {
                            case "1":       //유저 생성
                                System.out.print("생성할 유저명을 입력해 주세요: ");
                                String createUserName = sc.nextLine();
                                userService.create(createUserName);
                                break;
                            case "2":       //유저 조회
                                System.out.print("조회할 유저명을 입력하세요: ");
                                String includesUserName = sc.nextLine();
                                if (userService.read(includesUserName) == null) {
                                    System.out.println("존재하지 않는 유저입니다.");
                                } else {
                                    System.out.println(userService.read(includesUserName).getUserName());
                                }
                                break;
                            case "3":       //유저 목록
                                if (userService.readAll().isEmpty()) {
                                    System.out.println("존재하는 유저가 없습니다.");
                                } else {
                                    userService.readAll().values().forEach(user -> {
                                        System.out.println(user.getUserName());
                                    });
                                }
                                break;
                            case "4":       //유저 수정
                                System.out.print("수정할 유저의 이름을 입력하세요: ");
                                String userName = sc.nextLine();
                                if (userService.read(userName) == null) {
                                    System.out.println("존재하지 않는 유저입니다.");
                                    break;
                                }
                                System.out.print("변경할 유저명을 입력하세요: ");
                                String changedName = sc.nextLine();
                                if (userService.read(changedName) != null) {
                                    System.out.println("이미 존재하는 유저입니다.");
                                    break;
                                }
                                userService.update(userName, changedName);
                                break;
                            case "5":       //유저 삭제
                                System.out.print("삭제할 유저를 입력하세요: ");
                                String deleteUserName = sc.nextLine();
                                userService.delete(deleteUserName);
                                break;
                            case "6":       //유저 선택
                                if (userService.readAll().isEmpty()) {
                                    System.out.println("존재하는 유저가 없습니다.");
                                    break;
                                }
                                System.out.println("유저를 선택해 주세요: ");
                                userService.readAll().values().forEach(u -> System.out.println(u.getUserName()));
                                loginUser = userService.read(sc.nextLine());
                                break;
                            case "7":       //뒤로 가기
                                userRun = false;
                                break;
                            default:
                                System.out.println("다시 입력해주세요.");
                        }
                    }
                    break;
                case "2":       //채널
                    boolean channelRun = true;
                    while (channelRun) {
                        System.out.println("1. 채널 생성");
                        System.out.println("2. 채널 조회");
                        System.out.println("3. 채널 목록");
                        System.out.println("4. 채널 수정");
                        System.out.println("5. 채널 삭제");
                        System.out.println("6. 채널 선택");
                        System.out.println("7. 뒤로 가기");
                        System.out.print("선택 : ");
                        String channelChoice = sc.nextLine();
                        switch (channelChoice) {
                            case "1":       //채널 생성
                                System.out.print("생성할 채널명을 입력해 주세요: ");
                                String createChannelName = sc.nextLine();
                                channelService.create(createChannelName);
                                break;
                            case "2":       //채널 조회
                                System.out.print("조회할 채널을 입력해 주세요: ");
                                String includesChannelName = sc.nextLine();
                                if (channelService.read(includesChannelName) == null) {
                                    System.out.println("존재하지 않는 채널입니다.");
                                    break;
                                }
                                System.out.println(channelService.read(includesChannelName).getChannelName());
                                break;
                            case "3":       //채널 목록
                                if (channelService.readAll().isEmpty()) {
                                    System.out.println("존재하는 채널이 없습니다.");
                                    break;
                                }
                                channelService.readAll().values().forEach(channel -> {
                                    System.out.println(channel.getChannelName());
                                });
                                break;
                            case "4":       //채널 수정
                                System.out.print("수정할 채널의 이름을 입력하세요: ");
                                String channelName = sc.nextLine();
                                if (channelService.read(channelName) == null) {
                                    System.out.println("존재하지 않는 채널입니다.");
                                    break;
                                }
                                System.out.print("변경할 채널명을 입력하세요: ");
                                String changedName = sc.nextLine();
                                if (channelService.read(changedName) != null) {
                                    System.out.println("이미 존재하는 채널입니다.");
                                    break;
                                }
                                channelService.update(channelName, changedName);
                                break;
                            case "5":       //채널 삭제
                                System.out.print("삭제할 채널을 입력하세요: ");
                                String deleteChannelName = sc.nextLine();
                                channelService.delete(deleteChannelName);
                                break;
                            case "6":       //채널 선택
                                if (channelService.readAll().isEmpty()) {
                                    System.out.println("존재하는 채널이 없습니다.");
                                    break;
                                }
                                System.out.println("채널을 선택해 주세요: ");
                                channelService.readAll().values().forEach(c -> System.out.println(c.getChannelName()));
                                enterChannel = channelService.read(sc.nextLine());
                                break;
                            case "7":       //뒤로 가기
                                channelRun = false;
                                break;
                            default:
                                System.out.println("다시 입력해 주세요.");
                        }
                    }
                    break;
                case "3":       //채팅 시작
                    if (loginUser == null) {
                        System.out.println("먼저 유저를 선택해 주세요.");
                        break;
                    }
                    if (enterChannel == null) {
                        System.out.println("먼저 채널을 선택해 주세요.");
                        break;
                    }
                    boolean messageRun = true;
                    while (messageRun) {
                        System.out.println("접속한 채널 : " + enterChannel.getChannelName());
                        System.out.println("접속한 유저 : " + loginUser.getUserName());
                        System.out.println("1. 메세지 작성");
                        System.out.println("2. 메세지 조회");
                        System.out.println("3. 전체 메세지");
                        System.out.println("4. 메세지 수정");
                        System.out.println("5. 메세지 삭제");
                        System.out.println("6. 뒤로 가기");
                        System.out.print("선택 : ");
                        String messageChoice = sc.nextLine();
                        switch (messageChoice) {
                            case "1":       //메세지 작성
                                System.out.print("메세지 입력: ");
                                String writingMessage = sc.nextLine();
                                Message createdMessage = messageService.create(writingMessage, loginUser, enterChannel);
                                System.out.println("메시지 생성 완료. ID: " + createdMessage.getId());
                                break;
                            case "2":       //메세지 조회
                                System.out.println("1. 유저 필터");
                                System.out.println("2. 채널 필터");
                                System.out.println("3. 전부");
                                System.out.print("입력: ");
                                String filterChoice = sc.nextLine();
                                switch (filterChoice) {
                                    case "1":
                                        System.out.print("필터링할 유저 입력: ");
                                        String includesUserName = sc.nextLine();
                                        List<Message> userFiltered = messageService.readUser(includesUserName);
                                        if (userFiltered.isEmpty()) {
                                            System.out.println("해당 유저의 메세지가 없습니다.");
                                        } else {
                                            userFiltered.forEach(message -> {
                                                System.out.printf("ID: %s, (%d)[%s]%s: %s%n",
                                                        message.getId(),
                                                        message.getUpdatedAt(),
                                                        message.getChannel().getChannelName(),
                                                        message.getUser().getUserName(),
                                                        message.getMessage());
                                            });
                                        }
                                        break;
                                    case "2":
                                        System.out.print("필터링할 채널 입력: ");
                                        String includesChannelName = sc.nextLine();
                                        List<Message> channelFiltered = messageService.readChannel(includesChannelName);
                                        if (channelFiltered.isEmpty()) {
                                            System.out.println("해당 채널의 메세지가 없습니다.");
                                        } else {
                                            channelFiltered.forEach(message -> {
                                                System.out.printf("ID: %s, (%d)[%s]%s: %s%n",
                                                        message.getId(),
                                                        message.getUpdatedAt(),
                                                        message.getChannel().getChannelName(),
                                                        message.getUser().getUserName(),
                                                        message.getMessage());
                                            });
                                        }
                                        break;
                                    case "3":
                                        System.out.print("필터링할 유저 입력: ");
                                        String fullFilterUser = sc.nextLine();
                                        System.out.print("필터링할 채널 입력: ");
                                        String fullFilterChannel = sc.nextLine();
                                        List<Message> fullFiltered = messageService.readFullFilter(fullFilterUser, fullFilterChannel);
                                        if (fullFiltered.isEmpty()) {
                                            System.out.println("해당 조건의 메세지가 없습니다.");
                                        } else {
                                            fullFiltered.forEach(message -> {
                                                System.out.printf("ID: %s, (%d)[%s]%s: %s%n",
                                                        message.getId(),
                                                        message.getUpdatedAt(),
                                                        message.getChannel().getChannelName(),
                                                        message.getUser().getUserName(),
                                                        message.getMessage());
                                            });
                                        }
                                        break;
                                    default:
                                        System.out.println("다시 입력해 주세요.");
                                }
                                break;
                            case "3":       //전체 메세지
                                List<Message> allMessages = messageService.readAll();
                                if (allMessages.isEmpty()) {
                                    System.out.println("메세지가 없습니다.");
                                } else {
                                    allMessages.forEach(message -> {
                                        System.out.printf("ID: %s, (%d)[%s]%s: %s%n",
                                                message.getId(),
                                                message.getUpdatedAt(),
                                                message.getChannel().getChannelName(),
                                                message.getUser().getUserName(),
                                                message.getMessage());
                                    });
                                }
                                break;
                            case "4":       //메세지 수정
                                List<Message> myMessagesForUpdate = messageService.readUser(loginUser.getUserName());
                                if (myMessagesForUpdate.isEmpty()) {
                                    System.out.println("수정할 메세지가 없습니다.");
                                    break;
                                }
                                System.out.println("수정 가능한 메세지 목록:");
                                myMessagesForUpdate.forEach(message -> {
                                    System.out.printf("ID: %s, (%d) %s%n",
                                            message.getId(),
                                            message.getCreatedAt(),
                                            message.getMessage());
                                });
                                System.out.print("수정할 메시지의 ID를 입력해 주세요: ");
                                String updateIdStr = sc.nextLine();
                                UUID updateId;
                                try {
                                    updateId = UUID.fromString(updateIdStr);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("잘못된 ID 형식입니다.");
                                    break;
                                }
                                System.out.print("수정할 메시지를 입력해 주세요: ");
                                String newMessage = sc.nextLine();
                                messageService.update(updateId, newMessage);
                                System.out.println("메시지 수정 완료.");
                                break;
                            case "5":       //메세지 삭제
                                List<Message> myMessagesForDelete = messageService.readUser(loginUser.getUserName());
                                if (myMessagesForDelete.isEmpty()) {
                                    System.out.println("삭제할 메세지가 없습니다.");
                                    break;
                                }
                                System.out.println("삭제 가능한 메세지 목록:");
                                myMessagesForDelete.forEach(message -> {
                                    System.out.printf("ID: %s, (%d) %s%n",
                                            message.getId(),
                                            message.getCreatedAt(),
                                            message.getMessage());
                                });
                                System.out.print("삭제할 메시지의 ID를 입력해 주세요: ");
                                String deleteIdStr = sc.nextLine();
                                UUID deleteId;
                                try {
                                    deleteId = UUID.fromString(deleteIdStr);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("잘못된 ID 형식입니다.");
                                    break;
                                }
                                messageService.delete(deleteId);
                                System.out.println("메시지 삭제 완료.");
                                break;
                            case "6":       //뒤로 가기
                                messageRun = false;
                                break;
                            default:
                                System.out.println("다시 입력해 주세요.");
                        }
                    }
                    break;
                case "4":       //종료
                    run = false;
                    break;
                default:
                    System.out.println("다시 입력해 주세요.");
            }
        }
    }
}
