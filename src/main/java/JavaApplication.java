import com.sprint.mission.discodeit.entity.Channel;
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

public class JavaApplication {
    public static void main(String[] args) {

        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();


        // 1. JCFUserService 테스트
        // 1.1 등록
        try {
            User user = new User("성준", "1234", "고기");
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
            User user = new User(username, password, nickname);

            userService.updateUser(user);

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
        try {
            String channelName = "성준의채널";
            // 유저 추가
            User user1 = new User("새로운유저", "1234", "나는야새유저");
            User user2 = new User("삭제될유저", "1234", "나는야삭제될유저");
            Channel channel = channelService.getChannel(channelName);
            channelService.addUsersToChannel(user1, channel);
            // 유저 삭제
            channelService.removeUsersFromChannel(user2, channel);

        } catch(IllegalArgumentException e) {
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
            channelService.deleteChannel("태환의채널");
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







    }
}
