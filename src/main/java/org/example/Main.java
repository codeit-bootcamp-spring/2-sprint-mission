//package org.example;
//
//import com.sprint.mission.discodeit.dto.UserDto;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.entity.UserStatus;
//import com.sprint.mission.discodeit.file.FileChannelRepositoryImplement;
//import com.sprint.mission.discodeit.file.FileMessageRepositoryImplement;
//import com.sprint.mission.discodeit.file.FileUserRepositoryImplement;
//import com.sprint.mission.discodeit.basic.serviceimpl.BasicChannelService;
//import com.sprint.mission.discodeit.basic.serviceimpl.BasicMessageService;
//import com.sprint.mission.discodeit.basic.serviceimpl.BasicUserService;
//import com.sprint.mission.discodeit.basic.serviceimpl.UserChannelService;
//import com.sprint.mission.discodeit.service.*;
//
//import java.io.File;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//
//public class Main {
//
//    // 서비스 객체
//    private static UserService userService;
//    private static ChannelService channelService;
//    private static MessageService messageService;
//
//    // 테스트에 사용할 ID
//    private static UUID userId;
//    private static UUID channelId;
//    private static UUID messageId;
//
//    public static void main(String[] args) {
//        System.out.println("===== 서비스 계층 테스트 시작 =====");
//
//        // 테스트 전 data 디렉토리 생성
//        File dataDir = new File("discodeit/data");
//        if (!dataDir.exists()) {
//            dataDir.mkdirs();
//        }
//
//        // 서비스 객체 초기화
//        initializeServices();
//
//        // 1. 유저 서비스 테스트
//        testUserService();
//
//        // 2. 채널 서비스 테스트
//        testChannelService();
//
//        // 3. 메시지 서비스 테스트
//        testMessageService();
//
//        System.out.println("===== 서비스 계층 테스트 완료 =====");
//    }
//
//    private static void initializeServices() {
//        // 레포지토리 객체 생성 - 싱글톤 패턴 사용
//        UserRepository userRepository = FileUserRepositoryImplement.getInstance();
//        ChannelRepository channelRepository = FileChannelRepositoryImplement.getInstance();
//        MessageRepository messageRepository = FileMessageRepositoryImplement.getInstance();
//
//        UserChannelService userChannelService = new UserChannelService(userRepository, channelRepository);
//        UserStatusRepository userStatus = new UserStatusRepositoryImplement();
//        userService = new BasicUserService(userRepository, userChannelService,userStatus);
//        channelService = new BasicChannelService(channelRepository, userChannelService);
//        messageService = new BasicMessageService(messageRepository, userRepository, channelRepository);
//
//        System.out.println("서비스 객체 초기화 완료");
//    }
//
//    private static void testUserService() {
//        System.out.println("\n----- 유저 서비스 테스트 -----");
//
//        // 유저 생성
//        String email = "test@example.com";
//        String password = "password123";
//
//        try {
//            // 유저 생성
//            userService.createdUser(email, password);
//            System.out.println("유저 생성 성공: " + email);
//
////            // 생성된 유저 ID 가져오기
////            Set<UserDto.Summary> allUsers = userService.findByAllUsersId();
////            if (!allUsers.isEmpty()) {
////                userId = allUsers.iterator().next();
////                System.out.println("유저 ID: " + userId);
//          //  }
//
//            // 유저 조회
//            User foundUser = userService.findByUserId(userId);
//            System.out.println("유저 조회: " + foundUser.getEmail());
//
////            // 유저 이메일 수정
////            String newEmail = "updated@example.com";
////            userService.updateEmail(userId, newEmail);
////            System.out.println("이메일 수정: " + newEmail);
//
//            // 유저 삭제 테스트 이후 수행을 위해 삭제 코드 주석 처리
//            // userService.deleteUser(userId);
//            // System.out.println("유저 삭제 완료");
//
//        } catch (Exception e) {
//            System.out.println("유저 서비스 테스트 중 오류: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
////
////    private static void testChannelService() {
////        System.out.println("\n----- 채널 서비스 테스트 -----");
////
////        // userId가 null이면 유저 서비스 테스트가 실패한 것이므로 새 유저 생성
////        if (userId == null) {
////            String email = "channel_test@example.com";
////            String password = "password123";
////            try {
////                userService.createdUser(email, password);
////                Set<UUID> allUsers = userService.findByAllUsersId();
////                if (!allUsers.isEmpty()) {
////                    userId = allUsers.iterator().next();
////                    System.out.println("채널 테스트용 새 유저 생성: " + userId);
////                }
////            } catch (Exception e) {
////                System.out.println("채널 테스트용 유저 생성 실패: " + e.getMessage());
////                return;
////            }
////        }
//
////        try {
////            // 채널 생성
////            String channelName = "테스트채널";
////            channelService.createChannel(channelName, userId);
////            System.out.println("채널 생성 성공: " + channelName);
////
////            // 채널 목록 조회
////            Set<UUID> channels = channelService.findByAllChannel();
////            if (!channels.isEmpty()) {
////                channelId = channels.iterator().next();
////                System.out.println("채널 ID: " + channelId);
////            }
////
//////            // 채널 이름 변경
//////            String newChannelName = "수정된채널";
////            channelService.setChannelName(channelId,newChannelName,userId);
////            System.out.println("채널 이름 변경: " + newChannelName);
//
//            // 채널 사용자 목록
//            Set<UUID> channelUsers = channelService.getChannelUserList(channelId);
//            System.out.println("채널 사용자 수: " + channelUsers.size());
//
//            // 채널 탈퇴 (소유자는 탈퇴가 불가능하므로 실행하지 않음)
//
//        } catch (Exception e) {
//            System.out.println("채널 서비스 테스트 중 오류: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private static void testMessageService() {
//        System.out.println("\n----- 메시지 서비스 테스트 -----");
//
//        if (userId == null || channelId == null) {
//            System.out.println("유저 또는 채널이 없어 메시지 테스트를 진행할 수 없습니다.");
//            return;
//        }
//
//        try {
//            // 메시지 생성
//            String content = "테스트 메시지입니다.";
//            Message message = messageService.create(content, channelId, userId);
//            messageId = message.getId();
//            System.out.println("메시지 생성 성공: " + content);
//
//            // 메시지 조회
//            Message foundMessage = messageService.findByMessage(messageId);
//            System.out.println("메시지 조회: " + foundMessage.getMessage());
//
//            // 메시지 내용 수정
//            String newContent = "수정된 메시지입니다.";
//            messageService.updateMessage(messageId, newContent);
//            System.out.println("메시지 수정: " + newContent);
//
//            // 전체 메시지 조회
//            List<Message> allMessages = messageService.findAllMessage();
//            System.out.println("전체 메시지 수: " + allMessages.size());
//
//            // 메시지 삭제
//            boolean deleted = messageService.deleteMessage(messageId);
//            System.out.println("메시지 삭제 " + (deleted ? "성공" : "실패"));
//
//            // 테스트 종료 후 정리
//            // 채널 삭제
//            // channelService.removeChannel(channelId, userId);
//            // System.out.println("채널 삭제 완료");
//
//            // 유저 삭제
//            // userService.deleteUser(userId);
//            // System.out.println("유저 삭제 완료");
//
//        } catch (Exception e) {
//            System.out.println("메시지 서비스 테스트 중 오류: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}