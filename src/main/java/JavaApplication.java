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

        UserService userService = JCFUserService.getInstance();
        ChannelService channelService = JCFChannelService.getInstance();
        MessageService messageService = JCFMessageService.getInstance();


        // 1. JCFUserService 테스트
        User user = new User("성준", "1234", "고기");
        User user2 = new User("태환", "1234", "무민");
        User user3 = new User("희준", "1234", "생선");
        User user4 = new User("환주", "1234", "빵쥬");
        User user5 = new User("유저5", "1234", "검색확인용");

        // 1.1 등록
        try {
            // User user3 = new User("성준","4567", "마늘"); - 중복 username 등록 불가
            // User user4 = new User("재문", "", ""); - 필수 입력 항목이 빠졌으므로 등록 불가
            userService.createUser(user);
            userService.createUser(user2);
            userService.createUser(user3);
            userService.createUser(user4);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("유저 등록 예외 발생: " + e.getMessage());
        }

        // 1.2 조회
        // 1.2.1 단건 조회
        try {
            User findUser = userService.getUser("성준");
            // User findUser = userService.getUser("없는유저"); - 없는 유저를 찾으려하면 예외 발생
            System.out.println("=== 유저 관련 기능 ===");
            System.out.println("유저 단건 조회 : " + findUser.getUsername());
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.out.println("유저 단건 조회 예외 발생: " + e.getMessage());
        }

        // 1.2.2 다건 조회
        List<User> findUsers = userService.getAllUsers();
        System.out.print("유저 전체 조회 : ");
        findUsers.stream().forEach(m -> System.out.print(m.getUsername() + " "));
        System.out.println();

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
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.out.println("유저 수정 예외 발생: " + e.getMessage());
        }

        // 1.4 수정된 데이터 조회
        try {
            User findUser = userService.getUser("성준");
            System.out.println("유저 수정 조회 : " + findUser);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.out.println("유저 수정 조회 예외 발생: " + e.getMessage());
        }

        // 1.5 삭제
        try {
            userService.deleteUser("환주");
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.out.println("유저 삭제 예외 발생: " + e.getMessage());
        }

        // 1.6 삭제된 데이터 조회
        System.out.print("유저 삭제 전체 조회 : ");
        findUsers.stream().forEach(m -> System.out.print(m.getUsername() + " "));
        System.out.println();
        System.out.println();


        // 2. JCFChannelService 테스트
        System.out.println("=== 채널 관련 기능 ===");
        User channelOwner1 = userService.getUser("성준");
        User channelOwner2 = userService.getUser("태환");
        User channelOwner3 = userService.getUser("희준");
        Channel channel1 = new Channel(channelOwner1, "성준의채널");
        Channel channel2 = new Channel(channelOwner2, "태환의채널");
        Channel channel3 = new Channel(channelOwner3, "희준의채널");
        Channel channel4 = new Channel(user5, "검색확인용채널");

        // 2.1 등록
        try {
            channelService.createChannel(channel1);
            channelService.createChannel(channel2);
            channelService.createChannel(channel3);

            // 잘 등록됐는지는 조회에서 테스트
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("채널 생성 예외 발생: " + e.getMessage());
        }

        // 2.2 조회
        try {
            // 2.2.1 단건 조회
            Channel findChannel = channelService.getChannel("성준의채널");
            System.out.println("채널 조회 : " + findChannel.getChannelName());
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.out.println("채널 조회 예외 발생: " + e.getMessage());
        }
        // 2.2.2 다건 조회
        List<Channel> findChannels = channelService.getAllChannels();
        System.out.print("채널 전체 조회 : ");
        findChannels.stream().forEach(ch -> System.out.print(ch.getChannelName() + " "));
        System.out.println();

        // 2.3 수정
        User newUser = new User("새로운유저", "1234", "나는야새유저");
        User deleteUser = new User("삭제될유저", "1234", "나는야삭제될유저");
        try {
            channelService.addUsersToChannel(user, newUser, channel1.getChannelName()); // "성준의채널"은 "성준"의 채널이므로 정상적으로 수정
            channelService.addUsersToChannel(user, deleteUser, channel1.getChannelName());
            // channelService.addUsersToChannel(user, deleteUser, channel2.getChannelName()); // "태환의채널"은 "성준"의 채널이 아니므로 예외 발생
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            System.out.println("채널 수정 예외 발생: " + e.getMessage());
        }

        try {
            // 유저 삭제
            channelService.removeUsersFromChannel(user, deleteUser, channel1.getChannelName());
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            System.out.println("채널 수정 예외 발생: " + e.getMessage());
        }

        // 2.4 수정된 데이터 조회
        try {
            Channel findChannel = channelService.getChannel("성준의채널");
            System.out.println("채널 수정 조회 : " + findChannel);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.out.println("채널 수정 조회 예외 발생: " + e.getMessage());
        }

        // 2.5 삭제
        try {
            channelService.deleteChannel(user3, "희준의채널"); // 채널 주인이므로 정상적으로 삭제 완료
            // channelService.deleteChannel(user2, "성준의채널"); // 예외 발생
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.out.println("채널 삭제 예외 발생: " + e.getMessage());
        }

        // 2.6 삭제된 데이터 조회
        System.out.print("채널 삭제 전체 조회 : ");
        findChannels.stream().forEach(ch -> System.out.print(ch.getChannelName() + " "));
        System.out.println();

        System.out.println();

        System.out.println("=== 메시지 관련 기능 ===");
        // 3. JCFMessageService 테스트
        Message message1 = new Message("메시지1", user, channel2);
        Message message2 = new Message("메시지2", user, channel2);
        Message message3 = new Message("메시지3", user, channel2);
        Message message4 = new Message("검색확인", user5, channel4);
        Message message5 = new Message("전체메시지", user2, channel4);
        Message message6 = new Message("검색확인2", user2, channel4);
        UUID id1 = message1.getId();

        // 3.1 등록
        try {
            channelService.addUsersToChannel(user2, user, channel2.getChannelName());
            messageService.createMessage(message1);
            messageService.createMessage(message2);
            messageService.createMessage(message3);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("메시지 등록 예외 발생: " + e.getMessage());
        }

        // 3.2 조회
        try {
            // 3.2.1 단건 조회
            Message findMessage = messageService.getMessage(id1);
            System.out.println("메시지 조회 : " + findMessage.getContent());
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.out.println("메시지 조회 예외 발생: " + e.getMessage());
        }
        // 3.2.2 다건 조회
        List<Message> findMessages = messageService.getAllMessages();
        System.out.print("메시지 전체 조회 : ");
        findMessages.stream().forEach(m -> System.out.print(m.getContent() + " "));
        System.out.println();

        // 3.3 수정
        try {
            messageService.updateMessage(id1, user, "메시지수정1");
            // messageService.updateMessage(id2, user2, updateMessage); // 채널 주인이 아니면서, 메시지 주인이 아닌 사람이 수정하려하면 예외 발생
        } catch (IllegalArgumentException | NoSuchElementException | IllegalStateException e) {
            System.out.println("메시지 수정 예외 발생: " + e.getMessage());
        }

        // 3.4 수정된 데이터 조회
        try {
            Message findMessage = messageService.getMessage(id1);
            System.out.println("메시지 수정 조회 : " + findMessage.getContent());
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.out.println("메시지 수정 조회 예외 발생: " + e.getMessage());
        }

        // 3.5 삭제
        try {
            messageService.deleteMessage(id1, user);
            // messageService.deleteMessage(id2, user3);// 채널 주인이 아니면서, 메시지 주인이 아닌 사람이 삭제하려하면 예외 발생
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            System.out.println("메시지 삭제 예외 발생: " + e.getMessage());
        }

        // 3.6 삭제된 데이터 조회
        findMessages = messageService.getAllMessages();
        System.out.print("메시지 삭제 전체 조회 : ");
        findMessages.stream().forEach(m -> System.out.print(m.getContent() + " "));
        System.out.println();


        // 4. 임의로 추가한 기능 (의존성, 연관관계)
        System.out.println();
        System.out.println("=== 확장 기능 ===");
        System.out.println("=== 유저 - 채널 동기화 ===");
        // 4.1 유저 - 채널 동기화
        // JPA를 사용할 때, 매핑을 하면 자동으로 동기화됨 - Java에선 직접 구현?
        try {
            // 채널에 유저를 추가하면, 유저의 채널 목록에도 해당 채널 추가
            channelService.addUsersToChannel(user, newUser, channel1.getChannelName());
            channelService.addUsersToChannel(user2, newUser, channel2.getChannelName());
            System.out.print("채널에 유저 추가 - 유저명 '새로운유저'의 가입채널목록 : ");
            newUser.getChannels().stream().forEach(ch -> System.out.print(ch.getChannelName() + " "));
            System.out.println();

            // 채널에 유저를 삭제하면, 유저의 채널 목록에서 해당 채널 삭제
            channelService.removeUsersFromChannel(user2, newUser, channel2.getChannelName());
            System.out.print("채널에 유저 삭제 - 유저명 '새로운유저'의 가입채널목록 : ");
            newUser.getChannels().stream().forEach(ch -> System.out.print(ch.getChannelName() + " "));
            System.out.println();

            // 채널을 아예 삭제했을 때, User의 채널 목록에도 삭제돼야함.
            channelService.deleteChannel(user, channel1.getChannelName());
            System.out.print("채널 삭제 - 유저명 '새로운유저'의 가입채널목록 : ");
            newUser.getChannels().stream().forEach(ch -> System.out.print(ch.getChannelName() + " "));
            System.out.println();
            System.out.println();
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            System.out.println("유저 - 채널 동기화 예외 발생: " + e.getMessage());
        }


        // 4.2 유저 - 메시지 동기화
        try {
            System.out.println("=== 유저 - 메시지 동기화 ===");
            // 유저가 메시지를 보내면, 유저의 메시지 목록에도 해당 메시지 추가
            channelService.addUsersToChannel(user2, newUser, channel2.getChannelName()); // 채널에 유저가 있어야 메시지 넣기 가능 - 없으면 예외 발생
            Message synMessage1 = new Message("메시지동기화1", newUser, channel2);
            Message synMessage2 = new Message("메시지동기화2", newUser, channel2);
            messageService.createMessage(synMessage1);
            messageService.createMessage(synMessage2);
            System.out.print("유저 메시지 발송 - 유저명 '새로운유저'의 메시지목록 : ");
            newUser.getMessages().stream().forEach((m) -> System.out.print(m.getContent() + " "));
            System.out.println();

            // 메시지가 삭제되면, 유저의 메시지 목록에도 해당 메시지 삭제
            messageService.deleteMessage(synMessage1.getId(), newUser);
            System.out.print("유저 메시지 삭제 - 유저명 '새로운유저'의 메시지목록 : ");
            newUser.getMessages().stream().forEach((m) -> System.out.print(m.getContent() + " "));
            System.out.println();

            // 채널에서 유저 내보낼 시 해당 채널 유저의 메시지도 삭제돼야함
            channelService.removeUsersFromChannel(user2, newUser, channel2.getChannelName());
            System.out.print("채널 유저 삭제 - 유저명 '새로운유저'의 메시지목록 : ");
            newUser.getMessages().stream().forEach((m) -> System.out.print(m.getContent() + " "));
            System.out.println();
            System.out.println();
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            System.out.println("유저 - 메시지 동기화 예외 발생: " + e.getMessage());
        }


        // 4.3 채널 - 메시지 동기화
        try {
            System.out.println("=== 채널 - 메시지 동기화 ===");
            // 채널에 메시지가 보내지면, 채널의 메시지 목록에도 해당 메시지 추가
            channelService.addUsersToChannel(user2, newUser, channel2.getChannelName());
            Message newMessage = new Message("새메시지1", newUser, channel2);
            messageService.createMessage(newMessage);
            System.out.print("채널 메시지 생성 - 채널명 '태환의채널'의 메시지목록 : ");
            channel2.getMessages().stream().forEach(m -> System.out.print(m.getContent() + " "));
            System.out.println();
            // 채널 삭제 시에 유저의 해당 채널 메시지도 전부 삭제돼야함
            channelService.deleteChannel(user2, channel2.getChannelName());
            System.out.print("'채널 삭제 - 새로운유저'의 메시지 목록 : ");
            Message otherMessage = new Message("다른채널의메시지", newUser, channel1);
            channelService.createChannel(channel1);
            channelService.addUsersToChannel(user, newUser, channel1.getChannelName());
            messageService.createMessage(otherMessage);
            newUser.getMessages().stream().forEach(m -> System.out.print(m.getContent() + " "));
            System.out.println();
            System.out.println();
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            System.out.println("채널 - 메시지 동기화 예외 발생: " + e.getMessage());
        }


        // 4.4  해당 채널의 해당 유저의 메시지만 검색
        try {
            System.out.println("=== 채널 + 유저 검색 ===");
            // === 사전 작업 ===
            userService.createUser(user5);
            channelService.createChannel(channel4);
            channelService.addUsersToChannel(user5, user2, channel4.getChannelName());
            messageService.createMessage(message4);
            messageService.createMessage(message5);
            messageService.createMessage(message6);

            List<Message> findMessageByChannel = messageService.getAllMessagesByChannel(channel4.getChannelName());
            System.out.print("'검색확인용채널'의 전체 메시지 : ");
            findMessageByChannel.stream().forEach(m -> System.out.print(m.getContent() + "-" + m.getSender().getUsername() + " "));
            System.out.println();
            List<Message> findMessageByChannelAndUser = messageService.searchMessageByChannelAndUser("검색확인용채널", "유저5");
            System.out.print("'검색확인용채널'에서 '유저5' 유저명으로 검색 : ");
            findMessageByChannelAndUser.stream().forEach(m -> System.out.print(m.getContent() + " "));
            System.out.println();
            System.out.println();
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            System.out.println("채널 - 유저 검색 예외 발생: " + e.getMessage());
        }


        // 4.5 해당 채널의 메시지에 포함된 내용으로 검색
        try {
            System.out.println("=== 채널 + 메시지 검색 === ");
            List<Message> findMessagesContainingContent = messageService.searchMessagesContaining("검색확인용채널", "검색");
            System.out.print("'검색확인용채널'에서 '검색' 키워드가 들어간 메시지 : ");
            findMessagesContainingContent.stream().forEach(m -> System.out.print(m.getContent() + " "));
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("채널 - 내용 검색 예외 발생: " + e.getMessage());
        }
    }
}
