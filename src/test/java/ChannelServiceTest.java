import com.sprint.mission.discodeit.DTO.Channel.ChannelCRUDDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerCRUDDTO;
import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.Repository.*;
import com.sprint.mission.discodeit.Repository.jcf.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ServerService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicServerService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.UUID;

public class ChannelServiceTest {
    public static void main(String[] args) {
        UserRepository userRepository = new JCFUserRepository();
        ServerRepository serverRepository = new JCFServerRepository();
        ChannelRepository channelRepository = new JCFChannelRepository();
        MessageRepository messageRepository = new JCFMessageRepository();

        BinaryContentRepository binaryContentRepository = new JCFBinaryContentRepository();
        UserStatusRepository userStatusRepository = new JCFUserStatusRepository();
        ReadStatusRepository readStatusRepository = new JCFReadStatusRepository();



        UserService userService = new BasicUserService(userRepository, binaryContentRepository, userStatusRepository);
        ServerService serverService = new BasicServerService(userRepository, serverRepository);
        ChannelService channelService = new BasicChannelService(userRepository,serverRepository,channelRepository,messageRepository,readStatusRepository);
        BinaryContentService binaryContentService = new BasicBinaryContentService(binaryContentRepository);

        BinaryContent content1 = binaryContentService.create();
        UserCRUDDTO userDTO1 = UserCRUDDTO.create("test1", "test1", "123",content1);
        UUID userId1 = userService.register(userDTO1);
        ServerCRUDDTO serverTest1 = ServerCRUDDTO.create(userId1, "test1");
        UUID serverId1 = serverService.create(serverTest1);

        System.out.println("create----------------------------------------");
        ChannelCRUDDTO publicChannel1 = ChannelCRUDDTO.create(serverId1, userId1, "test1", ChannelType.PUBLIC);
        ChannelCRUDDTO publicChannel2 = ChannelCRUDDTO.create(serverId1, userId1, "test2", ChannelType.PUBLIC);
        ChannelCRUDDTO privateChannel1 = ChannelCRUDDTO.create(serverId1, userId1, "test2", ChannelType.PRIVATE);
        ChannelCRUDDTO privateChannel2 = ChannelCRUDDTO.create(serverId1, userId1, "test2", ChannelType.PRIVATE);
        channelService.create(publicChannel1);
        channelService.create(publicChannel2);
    }
}
