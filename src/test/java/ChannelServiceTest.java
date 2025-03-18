import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.DTO.Channel.ChannelCRUDDTO;
import com.sprint.mission.discodeit.DTO.Channel.ChannelDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerCRUDDTO;
import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.Repository.*;
import com.sprint.mission.discodeit.Repository.jcf.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ServerService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicServerService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.List;
import java.util.Optional;
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

        BinaryContentDTO binaryContentDTO = BinaryContentDTO.create("test1", null, null);
        UserCRUDDTO userDTO1 = UserCRUDDTO.create("test1", "test1", "123");
        User user = userService.register(userDTO1, Optional.of(binaryContentDTO));
        UUID userId1 = user.getId();
        ServerCRUDDTO serverTest1 = ServerCRUDDTO.create(userId1, "test1");
        UUID serverId1 = serverService.create(serverTest1);

        System.out.println("create----------------------------------------");
        ChannelCRUDDTO publicChannel1 = ChannelCRUDDTO.create(serverId1, userId1, "test1", ChannelType.PUBLIC);
        ChannelCRUDDTO publicChannel2 = ChannelCRUDDTO.create(serverId1, userId1, "test2", ChannelType.PUBLIC);
        ChannelCRUDDTO privateChannel1 = ChannelCRUDDTO.create(serverId1, userId1, "test2", ChannelType.PRIVATE);
        ChannelCRUDDTO privateChannel2 = ChannelCRUDDTO.create(serverId1, userId1, "test2", ChannelType.PRIVATE);
        UUID publicId1 = channelService.create(publicChannel1);
        UUID publicId2 = channelService.create(publicChannel2);
        UUID privateId1 = channelService.create(privateChannel1);
        UUID privateId2 = channelService.create(privateChannel2);
        System.out.println("find All----------------------------------------");
        List<ChannelDTO> channelDTOList = channelService.findAllByServerAndUser(serverId1.toString());
        for (ChannelDTO channelDTO : channelDTOList) {
            System.out.println(channelDTO);
        }

        System.out.println("find----------------------------------------");
        ChannelDTO channelDTO1 = channelService.find(publicId1.toString());
        ChannelDTO channelDTO2 = channelService.find(privateId1.toString());
        System.out.println("public = " + channelDTO1);
        System.out.println("private = " + channelDTO2);

        System.out.println("delete----------------------------------------");
        ChannelCRUDDTO publicDelete = ChannelCRUDDTO.delete(serverId1, userId1, publicId2);
        ChannelCRUDDTO privateDelete = ChannelCRUDDTO.delete(serverId1, userId1, privateId2);
        channelService.delete(publicDelete);
        channelService.delete(privateDelete);

        System.out.println("find All----------------------------------------");
        List<ChannelDTO> channelDTOList2 = channelService.findAllByServerAndUser(serverId1.toString());
        for (ChannelDTO channelDTO : channelDTOList2) {
            System.out.println(channelDTO);
        }

        System.out.println("update----------------------------------------");
        ChannelCRUDDTO publicUpdateKey1 = ChannelCRUDDTO.updateKey(serverId1, userId1, publicId1);
        ChannelCRUDDTO privateUpdateKey2 = ChannelCRUDDTO.updateKey(serverId1, userId1, privateId1);
        ChannelCRUDDTO publicUpdate = ChannelCRUDDTO.update(null, "hello", ChannelType.PRIVATE);
        ChannelCRUDDTO privateUpdate = ChannelCRUDDTO.update(null, "good", ChannelType.PUBLIC);

        channelService.update(publicUpdateKey1, publicUpdate);
        channelService.update(privateUpdateKey2, privateUpdate);

        System.out.println("find All----------------------------------------");
        List<ChannelDTO> channelDTOList3 = channelService.findAllByServerAndUser(serverId1.toString());
        for (ChannelDTO channelDTO : channelDTOList3) {
            System.out.println(channelDTO);
        }
    }
}
