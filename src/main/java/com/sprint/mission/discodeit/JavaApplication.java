package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Scanner;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();




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
                case "1":
                    boolean userRun = true;
                    while (userRun) {
                        System.out.println("1. 유저 생성");
                        System.out.println("2. 유저 조회");
                        System.out.println("3. 유저 목록");
                        System.out.println("4. 유저 수정");
                        System.out.println("5. 유저 삭제");
                        System.out.println("6. 뒤로 가기");
                        System.out.print("선택 : ");
                        String userChoice = sc.nextLine();
                        switch (userChoice) {
                            case "1":
                                System.out.print("생성할 유저명을 입력해 주세요: ");
                                String createUserName = sc.nextLine();
                                if(userService.read(createUserName)!=null){
                                    System.out.println("이미 존재하는 유저입니다.");
                                    break;
                                }
                                userService.create(createUserName);
                                break;

                            case "2":
                                System.out.print("조회할 유저명을 입력하세요: ");
                                System.out.println(userService.read(sc.nextLine()));
                                break;

                            case "3":
                                System.out.println(userService.readAll());
                                break;

                            case "4":
                                System.out.print("수정할 유저의 이름을 입력하세요: ");
                                String userName = sc.nextLine();
                                if(userService.read(userName)==null){
                                    System.out.println("존재하지 않는 유저입니다.");
                                    break;
                                }
                                System.out.print("변경할 유저명을 입력하세요: ");
                                String changedName = sc.nextLine();
                                if(userService.read(changedName)!=null){
                                    System.out.println("이미 존재하는 유저입니다.");
                                    break;
                                }
                                userService.update(changedName);
                                break;

                            case "5":
                                System.out.print("삭제할 유저를 입력하세요: ");
                                String deleteUserName = sc.nextLine();
                                if(userService.read(deleteUserName)==null){
                                    System.out.println("존재하지 않는 유저입니다");
                                    break;
                                }
                                userService.delete(deleteUserName);
                                break;

                            case "6":
                                userRun = false;
                                break;
                            default:
                                System.out.println("다시 입력해주세요.");
                        }
                    }
                    break;

                case "2":
                    boolean channelRun = true;
                    while (channelRun) {
                        System.out.println("1. 채널 생성");
                        System.out.println("2. 채널 조회");
                        System.out.println("3. 채널 목록");
                        System.out.println("4. 채널 수정");
                        System.out.println("5. 채널 삭제");
                        System.out.println("6. 뒤로 가기");
                        System.out.print("선택 : ");
                        String channelChoice = sc.nextLine();
                        switch (channelChoice) {
                            case "1":
                                System.out.print("생성할 채널명을 입력해 주세요: ");
                                String createChannelName = sc.nextLine();
                                if(channelService.read(createChannelName)!=null){
                                    System.out.println("이미 존재하는 채널입니다.");
                                    break;
                                }
                                channelService.create(createChannelName);
                                break;
                            case "2":
                                System.out.print("조회할 채널을 입력해 주세요: ");
                                System.out.println(channelService.read(sc.nextLine()));
                                break;
                            case "3":
                                System.out.println(channelService.readAll());
                                break;
                            case "4":
                                System.out.print("수정할 채널의 이름을 입력하세요: ");
                                String channelName = sc.nextLine();
                                if(channelService.read(channelName)==null){
                                    System.out.println("존재하지 않는 채널입니다.");
                                    break;
                                }
                                System.out.print("변경할 채널명을 입력하세요: ");
                                String changedName = sc.nextLine();
                                if(channelService.read(changedName)!=null){
                                    System.out.println("이미 존재하는 채널입니다.");
                                    break;
                                }
                                channelService.update(changedName);
                                break;
                            case "5":
                                System.out.print("삭제할 채널을 입력하세요: ");
                                String deleteChannelName = sc.nextLine();
                                if(channelService.read(deleteChannelName)==null){
                                    System.out.println("존재하지 않는 채널입니다");
                                    break;
                                }
                                channelService.delete(deleteChannelName);
                                break;
                            case "6":
                                channelRun = false;
                                break;
                            default:
                                System.out.println("다시 입력해 주세요.");
                        }
                    }
                    break;

                case "3":
                    boolean nope = true;
                    if(userService.readAll().isEmpty()){
                        System.out.println("유저를 먼저 생성해 주세요.");
                        break;
                    }
                    if(channelService.readAll().isEmpty()){
                        System.out.println("채널을 먼저 생성해 주세요.");
                        break;
                    }
                    System.out.println("유저를 선택해 주세요: ");
                    for(User u : userService.readAll().values()){
                        System.out.println(u.getUserName());
                    }
                    User loginUser = null;
                    while(nope) {
                        loginUser = userService.read(sc.nextLine());
                        if (loginUser == null) {
                            System.out.println("잘못된 유저명입니다. 다시 시도해주세요.");
                        }else {
                            nope = false;
                        }
                    }
                    nope = true;
                    System.out.println("채널을 선택해 주세요.");
                    for(Channel c : channelService.readAll().values()){
                        System.out.println(c.getChannelName());
                    }
                    Channel loginChannel = null;
                    while(nope) {
                        loginChannel = channelService.read(sc.nextLine());
                        if (loginChannel == null) {
                            System.out.println("잘못된 채널명입니다. 다시 시도해주세요");
                        }else{
                            nope = false;
                        }
                    }
                    boolean messageRun = true;
                    while (messageRun) {
                        System.out.println("접속한 채널 : " + loginChannel.getChannelName());
                        System.out.println("접속한 유저 : " + loginUser.getUserName());
                        System.out.println("1. 메세지 작성");
                        System.out.println("2. 메세지 조회");
                        System.out.println("3. 전체 메세지");
                        System.out.println("4. 메세지 수정");
                        System.out.println("5. 메세지 삭제");
                        System.out.println("6. 유저 변경");
                        System.out.println("7. 채널 변경");
                        System.out.println("8. 뒤로 가기");
                        System.out.print("선택 : ");
                        String messageChoice = sc.nextLine();
                        switch (messageChoice) {
                            case "1":
                                System.out.print("메세지 입력: ");
                                String writingMessage =  sc.nextLine();
                                messageService.create(writingMessage,loginUser,loginChannel);
                                System.out.println("["+ loginChannel.getChannelName() + "]"
                                        + loginUser.getUserName()+ ": " + writingMessage );
                                break;
                            case "2":
                                System.out.println("1. 유저 필터");
                                System.out.println("2. 채널 필터");
                                System.out.println("3. 전부");
                                System.out.print("입력: ");
                                switch (sc.nextLine()){
                                    case "1":
                                        System.out.print("필터링할 유저 입력: ");
                                        System.out.println(messageService.readUser(sc.nextLine()));
                                        break;
                                    case "2":
                                        System.out.print("필터링할 채널 입력: ");
                                        System.out.println(messageService.readChannel(sc.nextLine()));
                                        break;
                                    case "3":
                                        System.out.println("필터링할 유저, 채널 입력");
                                        System.out.print("유저: ");
                                        String userName = sc.nextLine();
                                        System.out.print("채널: ");
                                        String channelName = sc.nextLine();
                                        System.out.println(messageService.readFullFilter(userName, channelName));
                                        break;
                                    default:
                                        System.out.println("다시 입력해 주세요");
                                }
                                break;
                            case "3":
                                System.out.println(messageService.readAll());
                                break;
                            case "4":
                                for(Message m :messageService.readUser(loginUser.getUserName())){
                                    System.out.println("(" + m.getCreatedAt()+ ") " + m.getMessage());
                                }
                                System.out.println("수정하고 싶은 메세지와 타임스탬프를 입력해 주세요: ");
                                System.out.print("메세지: ");
                                String oldMessage = sc.nextLine();
                                System.out.print("타임스탬프: ");
                                Long updateMessageTimestamp =  sc.nextLong();
                                sc.nextLine();
                                System.out.println("수정한 메세지를 입력해 주세요: ");
                                String newMessage = sc.nextLine();
                                messageService.update(loginUser.getUserName(), updateMessageTimestamp, oldMessage, newMessage);
                                break;
                            case "5":
                                for(Message m :messageService.readUser(loginUser.getUserName())){
                                    System.out.println("(" + m.getCreatedAt()+ ") " + m.getMessage());
                                }
                                System.out.println("삭제할 메세지와 타임스탬프를 입력해 주세요: ");
                                System.out.print("메세지: ");
                                String deleteMessage = sc.nextLine();
                                System.out.print("타임스탬프: ");
                                Long deleteMessageTimestamp =  sc.nextLong();
                                sc.nextLine();
                                messageService.delete(loginUser.getUserName(), deleteMessage, deleteMessageTimestamp);
                                break;
                            case "6":
                                System.out.println("유저를 선택해 주세요: ");
                                for(User u : userService.readAll().values()){
                                    System.out.println(u.getUserName());
                                }
                                nope = true;
                                while(nope) {
                                    loginUser = userService.read(sc.nextLine());
                                    if (loginUser == null) {
                                        System.out.println("잘못된 유저명입니다. 다시 시도해주세요.");
                                    }else {
                                        nope = false;
                                    }
                                }
                                break;
                            case "7":
                                System.out.println("채널을 선택해 주세요.");
                                for(Channel c : channelService.readAll().values()){
                                    System.out.println(c.getChannelName());
                                }
                                nope = true;
                                while(nope) {
                                    loginChannel = channelService.read(sc.nextLine());
                                    if (loginChannel == null) {
                                        System.out.println("잘못된 채널명입니다. 다시 시도해주세요");
                                    }else{
                                        nope = false;
                                    }
                                }
                                break;
                            case "8":
                                messageRun = false;
                                break;
                            default:
                                System.out.println("다시 입력해 주세요.");
                        }
                    }
                    break;

                case "4":
                    run = false;
                    break;
                default:
                    System.out.println("다시 입력해 주세요.");
            }
        }

    }
}
