import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {

        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();


        // 1. JCFUserService 테스트
        // 1.1 등록
        User user = new User("성준", "1234", "고기"); // 계속 써야하므로 여기에 선언
        try {
            User user2 = new User("희준", "1234", "생선");
            User user3 = new User("태환", "1234", "무민");
            // User user3 = new User("성준","4567", "마늘"); - 중복 username 등록 불가
            // User user4 = new User("재문", "", ""); - 필수 입력 항목이 빠졌으므로 등록 불가
            userService.createUser(user);
            userService.createUser(user2);
            userService.createUser(user3);
        } catch (IllegalArgumentException e) {
            System.out.println("유저 등록 예외 발생: " +e.getMessage());
        }

        // 1.2 조회
        // 1.2.1 단건 조회
        try {
            User findUser = userService.getUser("성준");
            // User findUser = userService.getUser("없는유저"); - 없는 유저를 찾으려하면 예외 발생
            System.out.println("=== 유저 관련 기능 ===");
            System.out.println("유저 조회 : " + findUser.getUsername());
            // 1.2.2 다건 조회
            List<User> findUsers = userService.getAllUsers();
            System.out.println("유저 전체 조회 : " + findUsers);
        } catch (IllegalArgumentException e) {
            System.out.println("유저 조회 예외 발생: " + e.getMessage());
        }

        // 1.3 수정
        try {
            String username = "성준"; // username값을 알아야 수정 가능, 얘는 수정 X, 만약 username을 틀리게 입력할 경우 -> 예외 발생
            String password = "수정1234"; // 어떻게 수정할지 값을 받음, 입력 안하면 예외 발생
            String nickname = "수정고기"; // 어떻게 수정할지 값을 받음, 입력 안하면 예외 발생
            User updateUser = new User(username, password, nickname);

            userService.updateUser(updateUser);

            // 권한 추가
            String role1 = "admin";
            String role2 = "user";
            userService.addRole(role1, username);
            userService.addRole(role2, username);
            // 권한 삭제
            userService.removeRole(role2, username);

        } catch (IllegalArgumentException e) {
            System.out.println("유저 수정 예외 발생: " + e.getMessage());
        }

        // 1.4 수정된 데이터 조회
        try {
            User findUser = userService.getUser("성준");
            System.out.println("유저 수정 조회 : " + findUser.getUsername());
            List<User> findUsers = userService.getAllUsers();
            System.out.println("유저 수정 전체 조회 : " + findUsers);
        } catch (IllegalArgumentException e) {
            System.out.println("유저 조회 예외 발생: " + e.getMessage());
        }

        // 1.5 삭제
        try {
            userService.deleteUser("희준");
        } catch(IllegalArgumentException e) {
            System.out.println("유저 삭제 예외 발생: " + e.getMessage());
        }

        // 1.6 삭제된 데이터 조회
        try {
            List<User> findUsers = userService.getAllUsers();
            System.out.println("유저 삭제 전체 조회 : " + findUsers);
        } catch (IllegalArgumentException e) {
            System.out.println("유저 삭제 예외 발생: " + e.getMessage());
        }

        System.out.println();




        // 2. JCFChannelService 테스트
        System.out.println("=== 채널 관련 기능 ===");
        // 2.1 등록
        try {
            // 채널 주인 (여기선 user Data에서 꺼내와보자)
            User channelOwner1 = userService.getUser("성준");
            User channelOwner2 = userService.getUser("태환");
            Channel channel1 = new Channel(channelOwner1, "성준의채널");
            Channel channel2 = new Channel(channelOwner2, "태환의채널");
            channelService.createChannel(channel1);
            channelService.createChannel(channel2);

            // 잘 등록됐는지는 조회에서 테스트
        } catch(IllegalArgumentException e) {
            System.out.println("채널 생성 예외 발생: " + e.getMessage());
        }

        // 2.2 조회
        try {
            // 2.2.1 단건 조회
            Channel findChannel = channelService.getChannel("성준의채널");
            System.out.println("채널 조회 : " + findChannel.getChannelName());
            // 2.2.2 다건 조회
            List<Channel> findChannels = channelService.getAllChannels();
            System.out.println("채널 전체 조회 : " + findChannels);

        } catch(IllegalArgumentException e) {
            System.out.println("채널 조회 예외 발생: " + e.getMessage());
        }

        // 2.3 수정
        User user1 = new User("새로운유저", "1234", "나는야새유저");
        User user2 = new User("삭제될유저", "1234", "나는야삭제될유저");
        String channelName1 = "성준의채널";
        String channelName2 = "태환의채널";
        try {
            // 권한에 대한 고민 - 채널을 변경하려는 유저에게 관리자 권한이 있어야 채널 수정이 되는게 아닐까?
            channelService.addUsersToChannel(user, user1, channelName1); // "성준의채널"은 "성준"의 채널이므로 정상적으로 수정
            channelService.addUsersToChannel(user, user2, channelName1);
            channelService.addUsersToChannel(user, user2, channelName2); // "태환의채널"은 "성준"의 채널이 아니므로 예외 발생
        } catch(IllegalArgumentException e) {
            System.out.println("채널 수정 예외 발생: " + e.getMessage());
        }

        try {
            // 유저 삭제
            channelService.removeUsersFromChannel(user, user2, channelName1);
        } catch (IllegalArgumentException e) {
            System.out.println("채널 수정 예외 발생: " + e.getMessage());
        }

        // 2.4 수정된 데이터 조회
        try {
            Channel findChannel = channelService.getChannel("성준의채널");
            System.out.println("채널 수정 조회 : " + findChannel.getChannelName());
            List<Channel> findChannels = channelService.getAllChannels();
            System.out.println("채널 수정 전체 조회 : " + findChannels);

        } catch(IllegalArgumentException e) {
            System.out.println("채널 수정 조회 예외 발생: " + e.getMessage());
        }

        // 2.5 삭제
        try {
            channelService.deleteChannel(user,"태환의채널");
        } catch(IllegalArgumentException e) {
            System.out.println("채널 삭제 예외 발생: " + e.getMessage());
        }

        // 2.6 삭제된 데이터 조회
        try {
            List<Channel> findChannels = channelService.getAllChannels();
            System.out.println("유저 삭제 전체 조회 : " + findChannels);
        } catch (IllegalArgumentException e) {
            System.out.println("유저 삭제 예외 발생: " + e.getMessage());
        }



        System.out.println();

        System.out.println("=== 메시지 관련 기능 ===");
        // 3. JCFMessageService 테스트
        Channel channel = channelService.getChannel("성준의채널");
        Message message1 = new Message("안녕하세요 메시지 테스트1", user, channel);
        UUID id = message1.getId();
        // 3.1 등록
        try {
            Message message2 = new Message("안녕하세요 메시지 테스트2", user, channel);
            messageService.createMessage(message1);
            messageService.createMessage(message2);
        } catch(IllegalArgumentException e) {
            System.out.println("메시지 등록 예외 발생: " + e.getMessage());
        }

        // 3.2 조회
        try {
            // 3.2.1 단건 조회
            Message findMessage = messageService.getMessage(id);
            System.out.println("메시지 조회 : " + findMessage.getContent());
            // 3.2.2 다건 조회
            List<Message> findMessages = messageService.getAllMessages();
            System.out.println("메시지 다건 조회 : " + findMessages);
        } catch (IllegalArgumentException e) {
            System.out.println("메시지 조회 예외 발생: " + e.getMessage());
        }

        // 3.3 수정
        try {
            Message updateMessage = new Message("안녕하세요 메시지 수정됐는지 테스트1", user, channel);
            messageService.updateMessage(id, updateMessage);
        } catch(IllegalArgumentException e) {
            System.out.println("메시지 수정 예외 발생: " + e.getMessage());
        }

        // 3.4 수정된 데이터 조회
        try {
            Message findMessage = messageService.getMessage(id);
            System.out.println("메시지 수정 조회 : " + findMessage.getContent());
        } catch(IllegalArgumentException e) {
            System.out.println("메시지 수정 조회 예외 발생: " + e.getMessage());
        }

        // 3.5 삭제
        try {
            messageService.deleteMessage(id);
        } catch(IllegalArgumentException e) {
            System.out.println("메시지 삭제 예외 발생: " + e.getMessage());
        }

        // 3.6 삭제된 데이터 조회
        try {
            List<Message> findMessages = messageService.getAllMessages();
            System.out.println("메시지 삭제 조회 : " + findMessages);
        } catch (IllegalArgumentException e) {
            System.out.println("메시지 삭제 조회 예외 발생: " + e.getMessage());
        }



    }
}
