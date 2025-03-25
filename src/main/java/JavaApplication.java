import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.util.FileSerializationUtil;

import java.io.IOException;
import java.util.*;

public class JavaApplication {

    static FileSerializationUtil fileSerializationUtil = new FileSerializationUtil();

//    static UserRepository userRepository = new JCFUserRepository();
//    static ChannelRepository channelRepository = new JCFChannelRepository();
//    static MessageRepository messageRepository = new JCFMessageRepository();
//    static UserRepository userRepository = new FileUserRepository(fileSerializationUtil);
//    static ChannelRepository channelRepository = new FileChannelRepository(fileSerializationUtil);
//    static MessageRepository messageRepository = new FileMessageRepository(fileSerializationUtil);
//
//
//    static UserService userService = new BasicUserService(userRepository);
//    static ChannelService channelService = new BasicChannelService(channelRepository);
//    static MessageService messageService = new BasicMessageService(userService, channelService, messageRepository);

    public static void main(String[] args) {
//        try {
//            System.out.println("====== 테스트 1: 기본 생성 ======");
//            UUID user1Uuid = UUID.randomUUID();
//            UUID channel1Uuid = UUID.randomUUID();
//            UUID message1Uuid = UUID.randomUUID();
//            testCreate(user1Uuid, channel1Uuid, message1Uuid, System.currentTimeMillis());
//
//            System.out.println("\n====== 테스트 2: 생성 후 삭제 ======");
//            UUID user2Uuid = UUID.randomUUID();
//            UUID channel2Uuid = UUID.randomUUID();
//            UUID message2Uuid = UUID.randomUUID();
//            testCreate(user2Uuid, channel2Uuid, message2Uuid, System.currentTimeMillis());
//            delete(user2Uuid, channel2Uuid, message2Uuid);
//
//            System.out.println("\n====== 테스트 3: 생성 후 조회 ======");
//            UUID user3Uuid = UUID.randomUUID();
//            UUID channel3Uuid = UUID.randomUUID();
//            UUID message3Uuid = UUID.randomUUID();
//            testCreate(user3Uuid, channel3Uuid, message3Uuid, System.currentTimeMillis());
//            findById(user3Uuid, channel3Uuid, message3Uuid);
//
//            System.out.println("\n====== 테스트 4: 생성 후 업데이트 ======");
//            UUID user4Uuid = UUID.randomUUID();
//            UUID channel4Uuid = UUID.randomUUID();
//            UUID message4Uuid = UUID.randomUUID();
//            testCreate(user4Uuid, channel4Uuid, message4Uuid, System.currentTimeMillis());
//            update(user4Uuid, channel4Uuid, message4Uuid);
//
//            System.out.println("\n====== 테스트 5: 전체 항목 조회 ======");
//            findAll();
//
//            System.out.println("\n모든 테스트가 성공적으로 완료되었습니다!");
//        } catch (Exception e) {
//            System.err.println("테스트 실패: " + e.getMessage());
//            e.printStackTrace();
//        }
    }

//    public static void testCreate(UUID userUuid, UUID channelUuid, UUID messageUuid, long millis) {
////        userService.create(new User(userUuid, millis, "진호", "yangjinho826@naver.com"));
////        channelService.create(new Channel(channelUuid, millis, "진호채널", "테스트"));
////        messageService.create(new Message(messageUuid, millis, "메신저", channelUuid, userUuid), channelUuid, userUuid);
//    }
//
//    public static void findById(UUID userUuid, UUID channelUuid, UUID messageUuid) {
//        System.out.println(userService.findById(userUuid));
//        System.out.println(channelService.findById(channelUuid));
//        System.out.println(messageService.findById(messageUuid));
//    }
//
//    public static void update(UUID userUuid, UUID channelUuid, UUID messageUuid) throws IOException {
//        userService.update(userUuid, "진호양", "yangjinho@naver.com");
//        channelService.update(channelUuid, "진호양채널", "테스트1");
//        messageService.update(messageUuid, "메신저1",channelUuid, userUuid);
//    }
//
//    public static void delete(UUID userUuid, UUID channelUuid, UUID messageUuid) {
//        userService.delete(userUuid);
//        channelService.delete(channelUuid);
//        messageService.delete(messageUuid);
//    }
//
//    public static void findAll() {
//        System.out.println("===User===");
//        System.out.println(userService.findAll());
//        System.out.println("===Channel===");
//        System.out.println(channelService.findAll());
//        System.out.println("===Message===");
//        System.out.println(messageService.findAll());
//    }

}
