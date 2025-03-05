import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.List;

public class FileApplication {
    public static void main(String[] args) {
        UserService userService = FileUserService.getInstance();
        ChannelService channelService = FileChannelService.getInstance(userService);


        // 1. 유저
        // 1.1 유저 생성
        User user1 = new User("yyjjmm2003@naver.com", "1234", "고기");
        User user2 = new User("sjrnfldbcjs@naver.com", "1234", "무민");
        User user3 = new User("test1@naver.com", "1234", "성준");
        User user4 = new User("test2@naver.com", "1234", "태환");
        // User user5 = new User("yyjjmm2003@naver.com", "1234", "고기"); - 중복 유저이므로 예외 발생
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        userService.createUser(user4);


        // 1.2 유저 조회
        User findUser = userService.getUser("yyjjmm2003@naver.com");
        List<User> findUsers = userService.getAllUsers();
        System.out.println(findUser);
        System.out.println(findUsers);


        // 1.3 유저 수정
        User updateUser1 = new User("yyjjmm2003@naver.com", "수정1234", "수정고기");
        userService.updateUser(updateUser1); // 파일까지 잘 변경됨
        // Role 추가 및 삭제
        userService.addRole("Customer", updateUser1.getUsername());
        userService.removeRole("Customer", updateUser1.getUsername());
        User findUser2 = userService.getUser("yyjjmm2003@naver.com");
        System.out.println(findUser2);


        // 1.4 유저 삭제
        userService.deleteUser("test2@naver.com");
        System.out.println(userService.getAllUsers());


        // 2. 채널
        // 2.1 채널 생성
        Channel channel1 = new Channel(user1, "고기채널");
        Channel channel2 = new Channel(user2, "무민채널");
        // Channel channel3 = new Channel(user2, "무민채널"); - 중복 유저이므로 예외 발생
        channelService.createChannel(channel1);
        channelService.createChannel(channel2);
        // channelService.createChannel(channel3);

        // 2.2 채널 조회
        Channel findChannel = channelService.getChannel("고기채널");
        List<Channel> findChannels = channelService.getAllChannels();
        System.out.println(findChannel);
        System.out.println(findChannels);

        // 2.3 채널 수정
        // 유저 추가 및 삭제
        channelService.addUsersToChannel(user1, user3, channel1.getChannelName());
        channelService.removeUsersFromChannel(user1, user3, channel1.getChannelName());
        System.out.println(channelService.getChannel(channel1.getChannelName()).getUsers());

        // 2.4 채널 삭제
        // 채널 삭제 후 유저 확인
        // channelService.deleteChannel(user1, channel1.getChannelName());
        System.out.println(user3.getChannels());

        // 3. 메시지
        // 3.1 메시지 생성
        Message message1 = new Message("메시지1", user1, channel1);
        Message message2 = new Message("메시지2", user2, channel2);










    }
}
