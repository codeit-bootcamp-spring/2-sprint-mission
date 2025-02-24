import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.*;

public class JavaApplication {
    public static void main(String[] args) {

        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();


        // 1. JCFUserService 테스트
        // 1.1 등록
        User user = new User("성준", "1234", "고기");
        User user2 = new User("태환", "1234", "무민");
        User user3 = new User("희준", "1234", "생선");
        User user4 = new User("환주", "1234", "빵쥬");
        try {
            // User user3 = new User("성준","4567", "마늘"); - 중복 username 등록 불가
            // User user4 = new User("재문", "", ""); - 필수 입력 항목이 빠졌으므로 등록 불가
            userService.createUser(user);
            userService.createUser(user2);
            userService.createUser(user3);
            userService.createUser(user4);
        } catch (IllegalArgumentException e) {
            System.out.println("유저 등록 예외 발생: " + e.getMessage());
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
            System.out.print("유저 전체 조회 : ");
            findUsers.stream().forEach(m -> System.out.print(m.getUsername() +  " "));
            System.out.println();
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
            System.out.println("유저 수정 조회 : " + findUser);
        } catch (IllegalArgumentException e) {
            System.out.println("유저 조회 예외 발생: " + e.getMessage());
        }

        // 1.5 삭제
        try {
            userService.deleteUser("환주");
        } catch (IllegalArgumentException e) {
            System.out.println("유저 삭제 예외 발생: " + e.getMessage());
        }

        // 1.6 삭제된 데이터 조회
        try {
            List<User> findUsers = userService.getAllUsers();
            System.out.print("유저 삭제 전체 조회 : ");
            findUsers.stream().forEach(m -> System.out.print(m.getUsername() + " "));
            System.out.println();
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
            User channelOwner3 = userService.getUser("희준");
            Channel channel1 = new Channel(channelOwner1, "성준의채널");
            Channel channel2 = new Channel(channelOwner2, "태환의채널");
            Channel channel3 = new Channel(channelOwner3, "희준의채널");
            channelService.createChannel(channel1);
            channelService.createChannel(channel2);
            channelService.createChannel(channel3);

            // 잘 등록됐는지는 조회에서 테스트
        } catch (IllegalArgumentException e) {
            System.out.println("채널 생성 예외 발생: " + e.getMessage());
        }

        // 2.2 조회
        try {
            // 2.2.1 단건 조회
            Channel findChannel = channelService.getChannel("성준의채널");
            System.out.println("채널 조회 : " + findChannel.getChannelName());
            // 2.2.2 다건 조회
            List<Channel> findChannels = channelService.getAllChannels();
            System.out.print("채널 전체 조회 : ");
            findChannels.stream().forEach(ch -> System.out.print(ch.getChannelName() + " "));
            System.out.println();

        } catch (IllegalArgumentException e) {
            System.out.println("채널 조회 예외 발생: " + e.getMessage());
        }

        // 2.3 수정
        User newUser = new User("새로운유저", "1234", "나는야새유저");
        User deleteUser = new User("삭제될유저", "1234", "나는야삭제될유저");
        String channelName1 = "성준의채널";
        String channelName2 = "태환의채널";
        try {
            channelService.addUsersToChannel(user, newUser, channelName1); // "성준의채널"은 "성준"의 채널이므로 정상적으로 수정
            channelService.addUsersToChannel(user, deleteUser, channelName1);
            // channelService.addUsersToChannel(user, deleteUser, channelName2); // "태환의채널"은 "성준"의 채널이 아니므로 예외 발생
        } catch (IllegalArgumentException e) {
            System.out.println("채널 수정 예외 발생: " + e.getMessage());
        }

        try {
            // 유저 삭제
            channelService.removeUsersFromChannel(user, deleteUser, channelName1);
        } catch (IllegalArgumentException e) {
            System.out.println("채널 수정 예외 발생: " + e.getMessage());
        }

        // 2.4 수정된 데이터 조회
        try {
            Channel findChannel = channelService.getChannel("성준의채널");
            System.out.println("채널 수정 조회 : " + findChannel);
        } catch (IllegalArgumentException e) {
            System.out.println("채널 수정 조회 예외 발생: " + e.getMessage());
        }

        // 2.5 삭제
        try {
            channelService.deleteChannel(user3, "희준의채널"); // 채널 주인이므로 정상적으로 삭제 완료
            // channelService.deleteChannel(user2, "성준의채널"); // 예외 발생
        } catch (IllegalArgumentException e) {
            System.out.println("채널 삭제 예외 발생: " + e.getMessage());
        }

        // 2.6 삭제된 데이터 조회
        try {
            List<Channel> findChannels = channelService.getAllChannels();
            System.out.print("채널 삭제 전체 조회 : ");
            findChannels.stream().forEach(ch -> System.out.print(ch.getChannelName() + " "));
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("채널 유저 삭제 예외 발생: " + e.getMessage());
        }


        System.out.println();

        System.out.println("=== 메시지 관련 기능 ===");
        // 3. JCFMessageService 테스트
        Channel channel1 = channelService.getChannel("성준의채널");
        Channel channel2 = channelService.getChannel("태환의채널");
        Message message1 = new Message("메시지1", user, channel1);
        Message message2 = new Message("메시지2", user, channel1);
        Message message3 = new Message("메시지3", user, channel1);
        UUID id1 = message1.getId();
        UUID id2 = message2.getId();
        // 3.1 등록
        try {
            messageService.createMessage(message1);
            messageService.createMessage(message2);
            messageService.createMessage(message3);
        } catch (IllegalArgumentException e) {
            System.out.println("메시지 등록 예외 발생: " + e.getMessage());
        }

        // 3.2 조회
        try {
            // 3.2.1 단건 조회
            Message findMessage = messageService.getMessage(id1);
            System.out.println("메시지 조회 : " + findMessage.getContent());
            // 3.2.2 다건 조회
            List<Message> findMessages = messageService.getAllMessages();
            System.out.print("메시지 전체 조회 : ");
            findMessages.stream().forEach(m -> System.out.print(m.getContent() + " "));
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("메시지 조회 예외 발생: " + e.getMessage());
        }

        // 3.3 수정
        try {
            messageService.updateMessage(id1, user, "메시지수정1");
            // messageService.updateMessage(id2, user2, updateMessage); // 채널 주인이 아니면서, 메시지 주인이 아닌 사람이 수정하려하면 예외 발생
        } catch (IllegalArgumentException e) {
            System.out.println("메시지 수정 예외 발생: " + e.getMessage());
        }

        // 3.4 수정된 데이터 조회
        try {
            Message findMessage = messageService.getMessage(id1);
            System.out.println("메시지 수정 조회 : " + findMessage.getContent());
        } catch (IllegalArgumentException e) {
            System.out.println("메시지 수정 조회 예외 발생: " + e.getMessage());
        }

        // 3.5 삭제
        try {
            messageService.deleteMessage(id1, user);
            // messageService.deleteMessage(id2, user3);// 채널 주인이 아니면서, 메시지 주인이 아닌 사람이 삭제하려하면 예외 발생
        } catch (IllegalArgumentException e) {
            System.out.println("메시지 삭제 예외 발생: " + e.getMessage());
        }

        // 3.6 삭제된 데이터 조회
        try {
            List<Message> findMessages = messageService.getAllMessages();
            System.out.print("메시지 삭제 전체 조회 : ");
            findMessages.stream().forEach(m -> System.out.print(m.getContent() + " "));
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("메시지 삭제 조회 예외 발생: " + e.getMessage());
        }


        // 4. 임의로 추가한 기능
        System.out.println();
        System.out.println("=== 확장 기능 ===");
        System.out.println("=== 유저 - 채널 동기화 ===");
        // 4.1 유저 - 채널 동기화 (채널에 유저를 추가/삭제 하면, 유저의 채널 목록에도 해당 채널 추가)
        try {
            channelService.addUsersToChannel(user, newUser, channelName1);
            channelService.addUsersToChannel(user2, newUser, channelName2);
            System.out.println("유저 - 채널 동기화 확인 ");
            System.out.print("유저명 '새로운유저'의 가입채널목록 : ");
            newUser.getChannels().stream().forEach(ch -> System.out.print(ch.getChannelName() + " "));
            System.out.println();
            channelService.removeUsersFromChannel(user2, newUser, channelName2);
            System.out.println("태환의채널 탈퇴");
            System.out.print("유저명 '새로운유저'의 가입채널목록 : ");;
            newUser.getChannels().stream().forEach(ch -> System.out.print(ch.getChannelName() + " "));
            System.out.println();
            // 채널 삭제시에 User의 채널 목록에도 삭제돼야함.
            channelService.deleteChannel(user,channelName1);
            System.out.println("성준의채널 삭제");
            System.out.print("유저명 '새로운유저'의 가입채널목록 : ");;
            newUser.getChannels().stream().forEach(ch -> System.out.print(ch.getChannelName() + " "));
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("유저 - 채널 동기화 예외 발생: " + e.getMessage());
        }

        // 4.2 유저 - 메시지 동기화 (유저가 메시지를 보내면, 유저의 메시지 목록에도 해당 메시지 추가)
        try {
            System.out.println("=== 유저 - 메시지 동기화 ===");
            channelService.addUsersToChannel(user2, newUser, channelName2); // 채널에 유저가 있어야 메시지 넣기 가능 - 없으면 예외 발생
            Message synMessage1 = new Message("메시지동기화1", newUser, channel2);
            Message synMessage2 = new Message("메시지동기화2", newUser, channel2);
            messageService.createMessage(synMessage1);
            messageService.createMessage(synMessage2);
            System.out.println("유저 - 메시지 동기화 확인 ");
            System.out.print("유저명 '새로운유저'의 메시지목록 : ");
            newUser.getMessages().stream().forEach((m) -> System.out.print(m.getContent() + " "));
            System.out.println();
            messageService.deleteMessage(synMessage1.getId(), newUser);
            System.out.println("메시지동기화1 삭제");
            System.out.print("유저명 '새로운유저'의 메시지목록 : ");
            newUser.getMessages().stream().forEach((m) -> System.out.print(m.getContent() + " "));
            System.out.println();
            // 채널에서 유저 내보낼 시 해당 채널 유저의 메시지도 삭제돼야함
            channelService.removeUsersFromChannel(user2, newUser, channelName2);
            System.out.println("'새로운유저'가 '태환의채널'에서 쓴 메시지 모두 삭제");
            System.out.print("유저명 '새로운유저'의 메시지목록 : ");
            newUser.getMessages().stream().forEach((m) -> System.out.print(m.getContent() + " "));
        } catch(IllegalArgumentException e) {
            System.out.println("유저 - 메시지 동기화 예외 발생: " + e.getMessage()) ;
        }

        // 4.3 채널 - 메시지 동기화 (채널에 메시지가 보내지면, 채널의 메시지 목록에도 해당 메시지 추가)



        // 채널 삭제 시에 메시지도 삭제돼야함


        // 4.4 해당 채널의 해당 유저의 메시지만 검색


        // 4.5 해당 채널의 메시지에 포함된 내용으로 검색







    }
}
