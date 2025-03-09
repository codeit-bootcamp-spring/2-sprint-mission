package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

import java.util.*;

public class JavaApplication {
    static final UserService userService = new BasicUserService(new JCFUserRepository());
    static final ChannelService channelService = new BasicChannelService(new JCFChannelRepository(), userService);
    static final MessageService messageService = new BasicMessageService(new JCFMessageRepository(), userService, channelService);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UUID loginUserKey = null;
        UUID loginChannelKey = null;
        while (true) {
            commonPrint("main");
            final int num = sc.nextInt();
            sc.nextLine();
            switch (num) {
                case 1:
                    loginUserKey = userMenu(sc, loginUserKey);
                    break;
                case 2:
                    try {
                        if (loginUserKey == null) {
                            throw new IllegalStateException("로그인이 필요합니다.");
                        }
                        loginChannelKey = channelMenu(sc, loginUserKey, loginChannelKey);
                        break;
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                case 3:
                    return;
                default:
                    System.out.println("숫자를 입력하세요");
                    break;
            }
        }
    }

    private static UUID userMenu(final Scanner sc, UUID loginUserKey) {
        while (true) {
            System.out.println("유저 페이지");
            System.out.println("=====================================");
            System.out.println("로그인 된 ID: " + (loginUserKey != null ? userService.getUserId(loginUserKey) : "로그인 필요"));
            System.out.println("로그인 된 이름: " + (loginUserKey != null ? userService.getUserName(loginUserKey) : "로그인 필요"));
            commonPrint("user");
            final int num = sc.nextInt();
            sc.nextLine();
            switch (num) {
                case 1: {
                    try {
                        System.out.println("로그인\n");
                        System.out.print("ID: ");
                        final String inputId = sc.nextLine();
                        System.out.print("비밀번호: ");
                        final String inputPwd = sc.nextLine();
                        loginUserKey = userService.login(inputId, inputPwd, loginUserKey);
                        System.out.println("[Info] 로그인 성공: " + userService.getUserId(loginUserKey));
                        continue;
                    } catch (IllegalStateException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 2: {
                    try {
                        System.out.println("로그아웃");
                        userService.logOut(loginUserKey);
                        loginUserKey = null;
                        continue;
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 3: {
                    try {
                        System.out.println("사용자 정보 등록\n");
                        System.out.print("사용자 ID: ");
                        final String inputId = sc.nextLine();
                        System.out.print("사용자 이름: ");
                        final String inputName = sc.nextLine();
                        System.out.print("사용자 비밀번호: ");
                        final String inputPwd = sc.nextLine();
                        System.out.print("사용자 이메일: ");
                        final String inputEmail = sc.nextLine();
                        System.out.print("사용자 번호: ");
                        final String inputPhone = sc.nextLine();
                        userService.create(inputId, inputName, inputPwd, inputEmail, inputPhone);
                        System.out.println("[Info] 계정 생성 성공");
                        continue;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 4: {
                    try {
                        System.out.println("사용자 정보 조회(다건 조회시 ,(콤마)를 사용해주세요)\n");
                        System.out.print("조회할 ID를 입력하세요: ");
                        final String inputId = sc.nextLine();
                        if (inputId.contains(",")) {
                            System.out.println(userService.readAll(List.of(inputId.split(","))));
                            System.out.println("[Info] 다건조회: 정상 조회 되었습니다.");
                        } else {
                            System.out.println(userService.read(inputId));
                            System.out.println("[Info] 단건조회: 정상 조회 되었습니다.");
                        }
                        continue;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 5: {
                    try {
                        if (loginUserKey == null) {
                            throw new IllegalStateException("[Error] 로그인이 필수 입니다.");
                        }
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    try {
                        System.out.println("사용자 정보 변경\n");
                        System.out.println("변경할 정보를 입력해주세요(변경하지 않을 내용은 공백으로 남겨주세요)\n");
                        System.out.print("변경할 ID: ");
                        final String inputId = sc.nextLine();
                        System.out.print("변경할 비밀번호: ");
                        final String inputPwd = sc.nextLine();
                        System.out.print("변경할 E-mail: ");
                        final String inputEmail = sc.nextLine();
                        System.out.print("변경할 핸드폰번호: ");
                        final String inputPhone = sc.nextLine();
                        userService.update(loginUserKey, inputId, inputPwd, inputEmail, inputPhone);
                        System.out.println("[Info] 정상 수정 되었습니다.");
                        continue;

                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 6: {
                    try {
                        if (loginUserKey == null) {
                            throw new IllegalStateException("[Error] 로그인이 필수 입니다.");
                        }
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    try {
                        System.out.println("사용자 정보 삭제\n");
                        userService.delete(loginUserKey);
                        System.out.println("[Info] 정상 삭제 처리 되었습니다.");
                        loginUserKey = null;
                        continue;
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 7: {
                    return loginUserKey;
                }
                default:
                    System.out.println("숫자를 입력하세요");
                    break;
            }
        }
    }

    private static UUID channelMenu(final Scanner sc, UUID loginUserKey, UUID loginChannelKey) {
        while (true) {
            System.out.println("채널 페이지");
            System.out.println("=====================================");
            System.out.println("로그인 된 ID: " + (loginUserKey != null ? userService.getUserId(loginUserKey) : "로그인 필요"));
            System.out.println("로그인 된 이름: " + (loginUserKey != null ? userService.getUserName(loginUserKey) : "로그인 필요"));
            System.out.println("=====================================");
            commonPrint("channel");
            final int num = sc.nextInt();
            sc.nextLine();

            switch (num) {
                case 1: {
                    try {
                        System.out.println("채널 입장\n");
                        System.out.print("입장 할 채널 명: ");
                        final String inputName = sc.nextLine();
                        loginChannelKey = channelService.login(inputName, loginUserKey);
                        messageMenu(sc, loginUserKey, loginChannelKey);
                        continue;
                    } catch (IllegalStateException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 2: {
                    try {
                        System.out.println("채널 가입\n");
                        System.out.print("가입 할 채널 명: ");
                        final String inputName = sc.nextLine();
                        UUID channelUuid = channelService.signUp(inputName, loginUserKey);
                        System.out.println("[Info] " + channelUuid + " 에 가입되셨습니다.");
                        continue;
                    } catch (IllegalStateException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 3: {
                    try {
                        System.out.println("채널 생성\n");
                        System.out.print("채널 카테고리명: ");
                        final String inputCategory = sc.nextLine();
                        System.out.print("채널 명: ");
                        final String inputName = sc.nextLine();
                        System.out.print("채널 소개: ");
                        final String inputIntroduction = sc.nextLine();
                        Channel channel = channelService.create(inputCategory, inputName, inputIntroduction, loginUserKey, loginUserKey);
                        System.out.println("[Info] " + channel.getUuid() + " 채널이 생성되었습니다.");
                        continue;
                    } catch (IllegalStateException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 4: {
                    try {
                        System.out.println("채널 목록 조회(조회할 채널이 다건이면 ,(콤마)를 사용해주세요)\n");
                        System.out.print("조회할 채널명: ");
                        final String inputName = sc.nextLine();
                        System.out.println("채널 정보\n");
                        if (inputName.contains(",")) {
                            System.out.println(channelService.readAll(List.of(inputName.split(","))));
                        } else {
                            System.out.println(channelService.read(inputName));
                        }
                        continue;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 5: {
                    try {
                        System.out.println("채널 정보 변경\n");
                        System.out.print("변경하고싶은 채널의 현재 채널명을 입력하세요");
                        final String inputNameToModify = sc.nextLine();
                        System.out.println("변경할 채널 정보를 입력하세요(변경을 원치 않으면 공백)\n");
                        System.out.print("채널 카테고리: ");
                        final String inputCategory = sc.nextLine();
                        System.out.print("채널 이름: ");
                        final String inputName = sc.nextLine();
                        System.out.print("채널 소개: ");
                        final String inputIntroduction = sc.nextLine();
                        channelService.update(inputNameToModify, inputCategory, inputName, inputIntroduction, loginUserKey);
                        System.out.println("[Info] 정상 업데이트 되었습니다.");
                        continue;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 6: {
                    try {
                        System.out.println("채널 삭제");
                        System.out.print("삭제할 채널명: ");
                        final String inputName = sc.nextLine();
                        channelService.delete(inputName, loginUserKey);
                        loginChannelKey = null;
                        System.out.println(inputName + " 채널이 삭제되었습니다.");
                        continue;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 7:
                    return loginChannelKey;
            }
        }
    }

    private static void messageMenu(final Scanner sc, UUID loginUserKey, UUID loginChannelKey) {
        try {
            Optional.ofNullable(loginUserKey).orElseThrow(() -> new IllegalArgumentException("[Error] 로그인을 해주세요"));
            Optional.ofNullable(loginChannelKey).orElseThrow(() -> new IllegalArgumentException("[Error] 채널에 가입 해주세요"));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        while (true) {
            System.out.println("메시지 페이지");
            System.out.println("=====================================");
            System.out.println("로그인 된 ID: " + (loginUserKey != null ? userService.getUserId(loginUserKey) : "로그인 필요"));
            System.out.println("로그인 된 이름: " + (loginUserKey != null ? userService.getUserName(loginUserKey) : "로그인 필요"));
            System.out.println("로그인 된 채널 이름: " + (loginChannelKey != null ? channelService.getChannelName(loginChannelKey) : "로그인 필요"));
            commonPrint("message");
            final int num = sc.nextInt();
            sc.nextLine();
            switch (num) {
                case 1: {
                    try {
                        System.out.println("메시지 작성");
                        System.out.print("메시지 내용: ");
                        final String inputContent = sc.nextLine();
                        messageService.create(inputContent, loginUserKey, loginChannelKey);
                        continue;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 2: {
                    try {
                        System.out.println("제일 최근 메시지 조회");
                        final Message recentMessage = messageService.read(loginChannelKey);
                        System.out.println(recentMessage);
                        continue;
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 3: {
                    try {
                        System.out.println("전체 메시지 조회");
                        final List<Message> allMessage = messageService.readAll(loginChannelKey);
                        if (allMessage.isEmpty()) {
                            continue;
                        }
                        System.out.println(allMessage);
                        continue;
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 4: {
                    try {
                        System.out.println("메시지 수정");
                        System.out.print("수정하고 싶은 메시지 번호를 입력하세요: ");
                        final int selectedMessageId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("메시지 내용을 수정하세요: ");
                        final String inputContent = sc.nextLine();
                        messageService.update(selectedMessageId, inputContent);
                        System.out.println("[Info] 정상 수정 되었습니다.");
                        continue;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 5: {
                    try {
                        System.out.println("메시지 삭제");
                        System.out.print("삭제하고 싶은 메시지 번호를 입력하세요: ");
                        int selectedMessageNum = sc.nextInt();
                        sc.nextLine();
                        messageService.delete(selectedMessageNum);
                        System.out.println("[Info] 정상 삭제 되었습니다.");
                        continue;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                case 6:
                    return;
                default:
                    System.out.println("올바른 번호를 입력하세요");
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
                System.out.println("[1] 채널 입장");
                System.out.println("[2] 채널 가입");
                System.out.println("[3] 채널 생성");
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
