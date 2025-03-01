package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.*;

/*
목표
Git과 GitHub을 통해 프로젝트를 관리할 수 있다. [O]
채팅 서비스의 도메인 모델을 설계하고, Java로 구현할 수 있다. [O]
인터페이스를 설계하고 구현체를 구현할 수 있다. [O]
싱글톤 패턴을 구현할 수 있다. [O]
Java Collections Framework에 데이터를 생성/수정/삭제할 수 있다. [O]
Stream API를 통해 JCF의 데이터를 조회할 수 있다. [O]
[심화] 모듈 간 의존 관계를 이해하고 팩토리 패턴을 활용해 의존성을 관리할 수 있다. []
 */
public class JavaApplication {
    static JCFUserService userService = JCFUserService.getInstance();
    static JCFChannelService channelService = JCFChannelService.getInstance();
    static JCFMessageService messageService = JCFMessageService.getInstance();
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean mainRunning = true;
        boolean userRunning = false;
        boolean channelRunning = false;
        boolean messageRunning = false;
        String origin, inputId, inputName, inputPwd, inputEmail, inputPhone, inputCategory, inputIntroduction, inputContent;
        List<String> ableChannel;
        UUID loginUserUuid = null;
        UUID loginChannelUuid = null;
        int num = 0;
        while (mainRunning) {
            commonPrint("main");
            num = sc.nextInt();
            sc.nextLine();
            switch (num) {
                case 1:
                    userRunning = true;
                    while (userRunning) {
                        System.out.println("유저 페이지");
                        System.out.println("=====================================");
                        System.out.println("로그인 된 ID: " + userService.getUserId(loginUserUuid));
                        System.out.println("로그인 된 이름: " + userService.getUserName(loginUserUuid));
                        commonPrint("user");
                        num = sc.nextInt();
                        sc.nextLine();
                        switch (num) {
                            case 1:
                                if (loginUserUuid != null) {
                                    System.out.println("이미 로그인 되어 있습니다");
                                    continue;
                                }
                                System.out.println("로그인");
                                System.out.print("ID: ");
                                inputId = sc.nextLine();
                                System.out.print("비밀번호: ");
                                inputPwd = sc.nextLine();
                                loginUserUuid = userService.login(inputId,inputPwd);
                                System.out.println(loginUserUuid);
                                continue;

                            case 2:
                                if (loginUserUuid == null) {
                                    System.out.println("[Error] 로그인 선행이 필수입니다");
                                    continue;
                                }
                                System.out.println("로그아웃");
                                loginUserUuid = null;
                                System.out.println(loginUserUuid);
                                continue;

                            case 3:
                                System.out.println("사용자 정보 등록");
                                System.out.print("사용자 ID: ");
                                inputId = sc.nextLine();
                                System.out.print("사용자 이름: ");
                                inputName = sc.nextLine();
                                System.out.print("사용자 비밀번호: ");
                                inputPwd = sc.nextLine();
                                System.out.print("사용자 이메일: ");
                                inputEmail = sc.nextLine();
                                System.out.print("사용자 번호: ");
                                inputPhone = sc.nextLine();
                                userService.create(inputId, inputName, inputPwd, inputEmail, inputPhone);
                                continue;

                            case 4:
                                System.out.println("사용자 정보 조회(여러 사용자 조회시 ,(콤마)를 사용해주세요)");
                                System.out.print("조회할 ID를 입력하세요: ");
                                inputId = sc.nextLine();
                                if (inputId.contains(",")) {
                                    System.out.println(userService.readAll(List.of(inputId.split(","))));
                                } else {
                                    System.out.println(userService.read(inputId));
                                }
                                continue;

                            case 5:
                                if (loginUserUuid == null) {
                                    System.out.println("로그인이 필요합니다.");
                                    continue;
                                }
                                System.out.println("사용자 정보 변경 ");
                                System.out.println("현재 정보");
                                System.out.println(userService.read(loginUserUuid));
                                System.out.println("변경할 정보를 입력해주세요(변경하지 않을 내용은 공백으로 남겨주세요)");
                                System.out.print("변경할 ID: ");
                                inputId = sc.nextLine();
                                System.out.print("변경할 비밀번호: ");
                                inputPwd = sc.nextLine();
                                System.out.print("변경할 E-mail: ");
                                inputEmail = sc.nextLine();
                                System.out.print("변경할 핸드폰번호: ");
                                inputPhone = sc.nextLine();
                                userService.update(loginUserUuid,inputId,inputPwd,inputEmail,inputPhone);
                                continue;

                            case 6:
                                if (loginUserUuid == null) {
                                    System.out.println("로그인이 필요합니다.");
                                    continue;
                                }
                                System.out.println("사용자 정보 삭제");
                                userService.delete(loginUserUuid);
                                continue;

                            case 7:
                                userRunning = false;
                                break;

                            default:
                                System.out.println("숫자를 입력하세요");
                                break;
                        }
                    }
                    break;
                case 2:
                    channelRunning = true;
                    if (loginUserUuid == null) {
                        System.out.println("로그인이 필요합니다.");
                        continue;
                    }
                    while (channelRunning) {
                        System.out.println("채널 페이지");
                        System.out.println("=====================================");
                        System.out.println("로그인 된 ID: " + userService.getUserId(loginUserUuid));
                        System.out.println("로그인 된 이름: " + userService.getUserName(loginUserUuid));
                        commonPrint("channel");
                        num = sc.nextInt();
                        sc.nextLine();
                        switch (num) {
                            case 1:
                                System.out.println("채널 입장");
                                System.out.println("입장 가능 채널 명");
                                List<String> ableJoinChannel = channelService.getUserChannels(loginUserUuid);
                                if (ableJoinChannel.isEmpty()) {
                                    System.out.println("입장 가능한 채널이 존재하지 않습니다.");
                                    continue;
                                }
                                System.out.println(ableJoinChannel);
                                System.out.print("입장 할 채널 명: ");
                                inputName = sc.nextLine();
                                if (!ableJoinChannel.contains(inputName)) {
                                    System.out.println("[Error] 입장 가능한 채널이 아닙니다");
                                    continue;
                                }
                                loginChannelUuid = channelService.getChannelUuid(inputName);
                                messageRunning = true;
                                while (messageRunning) {
                                    System.out.println("메시지 페이지");
                                    System.out.println("=====================================");
                                    System.out.println("로그인 된 ID: " + userService.getUserId(loginUserUuid));
                                    System.out.println("로그인 된 이름: " + userService.getUserName(loginUserUuid));
                                    System.out.println("현재 채널: " + channelService.getChannelName(loginChannelUuid));
                                    commonPrint("message");
                                    num = sc.nextInt();
                                    sc.nextLine();
                                    switch (num) {
                                        case 1:
                                            System.out.println("메시지 작성");
                                            System.out.print("메시지 내용: ");
                                            inputContent = sc.nextLine();
                                            messageService.create(inputContent, loginUserUuid, loginChannelUuid);
                                            continue;
                                        case 2:
                                            System.out.println("제일 최근 메시지 조회");
                                            Message recentMessage = messageService.read(loginChannelUuid);
                                            if (recentMessage == null) {
                                                continue;
                                            }
                                            System.out.println(recentMessage);
                                            continue;
                                        case 3:
                                            System.out.println("전체 메시지 조회");
                                            List<Message> allMessage = messageService.readAll(loginChannelUuid);
                                            if (allMessage.isEmpty()) {
                                                continue;
                                            }
                                            System.out.println(allMessage);
                                            continue;
                                        case 4:
                                            System.out.println("메시지 수정");
                                            List<Message> toFixMessage = messageService.getMyMessage(loginUserUuid, loginChannelUuid);
                                            if (toFixMessage.isEmpty()) {
                                                continue;
                                            }
                                            System.out.println(toFixMessage);
                                            System.out.print("수정하고 싶은 메시지 번호를 입력하세요: ");
                                            num = sc.nextInt();
                                            sc.nextLine();
                                            if (messageService.isMessageNotExist(num,userService.getUserName(loginUserUuid))) {
                                                continue;
                                            }
                                            System.out.print("메시지 내용을 수정하세요: ");
                                            inputContent = sc.nextLine();
                                            messageService.update(num, inputContent);
                                            continue;
                                        case 5:
                                            System.out.println("메시지 삭제");
                                            List<Message> toDelMessage = messageService.getMyMessage(loginUserUuid, loginChannelUuid);
                                            if (toDelMessage.isEmpty()) {
                                                continue;
                                            }
                                            System.out.print("삭제하고 싶은 메시지 번호를 입력하세요: ");
                                            num = sc.nextInt();
                                            sc.nextLine();
                                            if (messageService.isMessageNotExist(num,userService.getUserName(loginUserUuid))) {
                                                continue;
                                            }
                                            messageService.delete(num);
                                            continue;
                                        case 6:
                                            messageRunning = false;
                                            break;
                                        default:
                                            System.out.println("올바른 번호를 입력하세요");
                                            break;
                                    }
                                }
                                continue;

                            case 2:
                                System.out.println("채널 가입");
                                System.out.println("존재하는 채널 정보");
                                List<Channel> channelInfo = channelService.readAll();
                                System.out.println(channelInfo);
                                if (channelInfo == null) {
                                    continue;
                                }
                                System.out.print("가입 할 채널 명: ");
                                inputName = sc.nextLine();
                                channelService.signUp(inputName,loginUserUuid);
                                continue;

                            case 3:
                                System.out.println("채널 생성");
                                System.out.print("채널 카테고리명: ");
                                inputCategory = sc.nextLine();
                                System.out.print("채널 명: ");
                                inputName = sc.nextLine();
                                System.out.print("채널 소개: ");
                                inputIntroduction = sc.nextLine();
                                channelService.create(inputCategory, inputName, inputIntroduction, loginUserUuid, loginUserUuid);
                                continue;

                            case 4:
                                System.out.println("채널 목록 조회(조회할 채널이 여러개면 ,(콤마)를 이용해주세요)");
                                System.out.print("조회할 채널명: ");
                                inputName = sc.nextLine();
                                System.out.println("채널 정보");
                                if (inputName.contains(",")) {
                                    System.out.println(channelService.readAll(List.of(inputName.split(","))));
                                } else {
                                    System.out.println(channelService.read(inputName));
                                }
                                continue;

                            case 5:
                                System.out.println("채널 정보 변경");
                                ableChannel = channelService.getOwnerChannelName(loginUserUuid);
                                System.out.println("변경 가능한 채널명: " + ableChannel);
                                if (ableChannel == null) {
                                    System.out.println("변경 가능한 채널이 존재하지 않습니다.");
                                    continue;
                                }
                                System.out.println("변경할 현재 채널명을 입력하세요");
                                origin = sc.nextLine();
                                if (!ableChannel.contains(origin)) {
                                    System.out.println("관리자 권한을 가진 채널 명을 입력해주세요");
                                    continue;
                                }
                                System.out.println("변경할 채널 정보를 입력하세요(변경을 원치 않으면 공백)");
                                System.out.print("채널 카테고리: ");
                                inputCategory = sc.nextLine();
                                System.out.print("채널 이름: ");
                                inputName = sc.nextLine();
                                System.out.print("채널 소개: ");
                                inputIntroduction = sc.nextLine();
                                channelService.update(origin, inputCategory, inputName, inputIntroduction);
                                continue;
                            case 6:
                                System.out.println("채널 삭제");
                                ableChannel = channelService.getOwnerChannelName(loginUserUuid);
                                System.out.println("삭제 가능한 채널" + ableChannel);
                                if (ableChannel == null || ableChannel.isEmpty()) {
                                    System.out.println("삭제 가능한 채널이 존재하지 않습니다");
                                    continue;
                                }
                                System.out.print("삭제할 채널명: ");
                                inputName = sc.nextLine();
                                if (!ableChannel.contains(inputName)) {
                                    System.out.println("관리자 권한을 가진 채널 명을 입력해주세요");
                                    continue;
                                }
                                channelService.delete(inputName,loginUserUuid);
                                continue;

                            case 7:
                                channelRunning = false;
                                break;
                            }
                        }
                    break;
                case 3:
                    mainRunning = false;
                    break;
                default:
                    System.out.println("숫자를 입력하세요");
                    break;
                }
            }
        }
    public static void commonPrint(String var) {

        switch (var) {
            case "main":
                System.out.println("\n=====================================");
                System.out.println("         🌟 관리자 페이지 🌟");
                System.out.println("=====================================");
                System.out.println("     [1] User");
                System.out.println("     [2] Channel");
                System.out.println("     [3] 종료");
                System.out.println("=====================================");
                System.out.print("선택하세요: ");
                break;
            case "user":
                System.out.println("[1] 유저 로그인");
                System.out.println("[2] 유저 로그아웃");
                System.out.println("[3] 유저 등록");
                System.out.println("[4] 유저 조회");
                System.out.println("[5] 유저 정보 수정");
                System.out.println("[6] 유저 삭제");
                System.out.println("[7] 돌아가기");
                System.out.println("=====================================");
                System.out.print(" ▶ 선택하세요: ");
                break;
            case "channel":
                System.out.println("=====================================");
                System.out.println("[1] 채널 입장");
                System.out.println("[2] 채널 가입");
                System.out.println("[3] 채널 등록");
                System.out.println("[4] 채널 조회");
                System.out.println("[5] 채널 수정");
                System.out.println("[6] 채널 삭제");
                System.out.println("[7] 돌아가기");
                System.out.println("=====================================");
                System.out.print(" ▶ 선택하세요: ");
                break;

            case "message":
                System.out.println("[1] 메시지 작성");
                System.out.println("[2] 가장 최근 메시지 조회");
                System.out.println("[3] 전체 메시지 조회");
                System.out.println("[4] 메시지 수정");
                System.out.println("[5] 메시지 삭제");
                System.out.println("[6] 뒤로가기");
                System.out.print("선택하세요 : ");
                break;
            default:
                System.out.println("올바르지 않은 번호입니다");
                break;
        }
    }
}

