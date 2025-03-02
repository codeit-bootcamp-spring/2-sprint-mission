package org.example;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.jcf.*;

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    public static void main(String[] args) {


        UserRepository userRepository =  new JCFUserRepositoryimplement();
        MessageRepository messageRepository = new JCFMessageRepositoryimplement();
        UserService userService = new JCFUserServiceimplement(userRepository);
        ChannelRepository channelRepository = new JCFChannelReopsitoryImplement();
        MessageService messageService = new JCFmessageServiceImplement(userRepository, messageRepository);
        ChannelService channelService = new JCFChannelServiceImplement(channelRepository,userRepository);
        // 1. 사용자 생성
        User user1 = new User("user1");
        //
        userService.createdUser(user1);
        User user2 = new User("user1");
        User user3 = new User("user2");
        userService.createdUser(user2);
        userService.createdUser(user3);
        userService.readAllUsers();
        userService.readUser(user1.getuserId());
        Message message = new Message("user1","user2","안녕");
        messageService.sendMessage(message);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@");
        messageService.AllMessageView();
        Message message1 = new Message("user1","user5","ㅇㅎ");
        messageService.sendMessage(message1);
        /// qweqweqweqweqweqweqweqweqweqweqweewqeqw
        channelService.createChannel("공부방","user1");
        channelService.joinChannel("공부방","user2");
        channelService.getChannelList();
        channelService.getChannelUserList("공부방");
        channelService.leaveChannel("공부방","user2");
        channelService.getChannelUserList("공부방");
        channelService.leaveChannel("공부방","user1");
        channelService.updateChannel("공부방","공부방2","user2");
//        channelService.getChannelList();
        channelService.joinChannel("공부방","user2");
        channelService.updateChannel("공부방","공부방2","user1");
        channelService.getChannelList();
        channelService.getChannelUserList("공부방2");
        userService.readAllUsers();
/// / 변경을 하고 올림


//
    }












//        boolean run = true;
//
//        while (run) {
//            System.out.println("\\n=== 메뉴 ===");
//            System.out.println("1. 사용자 등록");
//            System.out.println("2. 사용자 수정");
//            System.out.println("3. 사용자 조회");
//            System.out.println("4. 모든 사용자들 조회");
//            System.out.println("5. 모든 사용자 조회");
//            System.out.println("6. 메세지 조회");
//            System.out.println("7.채널 가입");
//            System.out.print("선택: ");
//
//            int choice = sc.nextInt();
//            sc.nextLine();
//
//            switch (choice) {
//                case 1:
//                    boolean flag = true;
//                    while(flag) {
//                        System.out.println("생성할 아이디를 입력하세요.");
//                        String id = sc.next();
//                        sc.nextLine();
//                        User newUser = new User(id);
//                        if(userService.createdUser(newUser)) {
//                            flag = false;
//                            break;
//                        }
//                    }
//                    break;
//                case 2:
////                    flag = true;
////                    while(flag) {
////                        System.out.println("아이디를 입력해주세요");
////                        String userid=sc.next();
////                        if(!userRepository.containsUser(userid)){
////                            System.out.println("아이디가 존재하지 않습니다.");
////                            continue;
////                        }
////                        sc.nextLine();
////                        System.out.println(" 변경하고 싶은 아이디를 입력하세요 ");
////                        String newName = sc.next();
////                        if(userService.updateUser(userid, newName)){
////                            flag = false;
////                            break;
////                        }
////                        else{
////                            System.out.println("수정 실패");
////                        }
//                    //}
//
//
//                    break;
//
//                case 3:
//                    System.out.println("조회할 사용자를 입력하세요.");
//                    String finduser=sc.next();
//                    sc.nextLine();
//                    userService.readUser(finduser);
//                    System.out.println("#################.");
//                 //   System.out.println("User 정보 : ");
//                    break;
//
//                case 4:
//                    System.out.println("모든 사용자 리스트 #####");
//                    String findAllUser=sc.next();
//                    sc.nextLine();
//                    userService.readAllUsers();
//                    System.out.println("#################.");
//                   break;
//
//                case 5:
//
//                    break;
//
//                case 6:
//                    run = false;
//                    System.out.println("[Info] 종료합니다.");
//                    break;
//
//                default:
//                    System.out.println("[Error] 잘못된 입력입니다.");
//            }
//        }
//
//        sc.close();
//    }
    }




