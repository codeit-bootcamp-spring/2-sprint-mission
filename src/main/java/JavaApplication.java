import com.sprint.mission.discodeit.Repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFContainerRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.*;

public class JavaApplication {


    public static void main(String[] args) {
        JCFUserService userService = JCFUserService.getInstance();
        JCFServerService serverService = JCFServerService.getInstance();

        JCFCategoryService categoryService = JCFCategoryService.getInstance();
        JCFChannelService channelService = JCFChannelService.getInstance();
        JCFMessageService messageService = JCFMessageService.getInstance();

        JCFUserRepository userRepository = JCFUserRepository.getInstance();
        JCFContainerRepository containerRepository = JCFContainerRepository.getInstance();
        JCFChannelRepository channelRepository = JCFChannelRepository.getInstance();


        // 사용자 등록
        User user1 = new User("codeit", "123123");
        User user2 = new User("LYN", "456456");


        // 모든 사용자 조회

        // 특정 데이터 조회

        // 수정

        //서버 등록
        Server testServer1 = userService.createServer("test server1");
        Server testServer2 = userService.createServer("test server2");

        userRepository.add(user1, testServer1);
        userRepository.add(user2, testServer1);

        userRepository.add(user1, testServer2);
        userRepository.add(user2, testServer2);

        //유저가 가지고 있는 서버 조회
        userRepository.print(user1);
        userRepository.print(user2);

        // 채널 등록
        Channel testChannel1= userService.createChannel("test channel1");
        Channel testChannel2 = userService.createChannel("test channel2");

        containerRepository.add(testServer1,testChannel1);
        containerRepository.add(testServer1,testChannel2);

        containerRepository.add(testServer2,testChannel1);
        containerRepository.add(testServer2,testChannel2);

        // 모든 채널 조회
        containerRepository.print(testServer1);
        containerRepository.print(testServer2);

        // 특정 채팅방 조회

        // 사용자가 속한 채팅방 조회

        // 채널명 수정
        containerRepository.update(testServer1);
        containerRepository.update(testServer2);

        // 채널 삭제
        containerRepository.remove(testServer1);
        containerRepository.remove(testServer2);

        // 채널 삭제 후 모든 채널 조회
        containerRepository.print(testServer1);
        containerRepository.print(testServer2);

        // 메시지 생성
        Message message1 = channelService.write("Hello, World");
        Message message2 = channelService.write("Good to see you");
        channelRepository.add(testChannel1,message1);
        channelRepository.add(testChannel1,message2);

        Message message3 = channelService.write("Well...");
        Message message4 = channelService.write("Good luck");
        channelRepository.add(testChannel2,message3);
        channelRepository.add(testChannel2,message4);

//        // 채널의 모든 메시지 출력
//        channelRepository.print(testChannel1);
//        channelRepository.print(testChannel2);
//
//        // 특정 메시지 삭제
//        channelRepository.remove(testChannel1);
//        channelRepository.print(testChannel1);
//
//        // 메시지 삭제 후 채널의 모든 메시지 출력
//        channelRepository.remove(testChannel2);
//        channelRepository.print(testChannel2);


    }
}
