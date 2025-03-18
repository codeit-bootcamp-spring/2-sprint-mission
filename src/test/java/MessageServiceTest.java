import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.DTO.Channel.ChannelCRUDDTO;
import com.sprint.mission.discodeit.DTO.Message.MessageCRUDDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerCRUDDTO;
import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.Repository.*;
import com.sprint.mission.discodeit.Repository.jcf.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MessageServiceTest {
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
        ChannelService channelService = new BasicChannelService(userRepository, serverRepository, channelRepository, messageRepository, readStatusRepository);
        MessageService messageService = new BasicMessageService(userRepository, channelRepository, messageRepository, binaryContentRepository);
        BinaryContentService binaryContentService = new BasicBinaryContentService(binaryContentRepository);

        BinaryContentDTO binaryContentDTO = BinaryContentDTO.create("test1", null, null);
        UserCRUDDTO userDTO1 = UserCRUDDTO.create("test1", "test1", "123");
        User user = userService.register(userDTO1, Optional.of(binaryContentDTO));
        UUID userId1 = user.getId();

        UserCRUDDTO userDTO2 = UserCRUDDTO.create("test2", "test2", "123");
        User user1 = userService.register(userDTO2, Optional.empty());
        UUID userId2 = user1.getId();

        ServerCRUDDTO serverTest1 = ServerCRUDDTO.create(userId1, "test1");
        UUID serverId1 = serverService.create(serverTest1);
        ServerCRUDDTO serverJoin = ServerCRUDDTO.join(serverId1, userId2);
        serverService.join(serverJoin);

        ChannelCRUDDTO publicChannel1 = ChannelCRUDDTO.create(serverId1, userId1, "test1", ChannelType.PUBLIC);
        UUID publicId1 = channelService.create(publicChannel1);
        ChannelCRUDDTO channelJoin = ChannelCRUDDTO.join(userId2, publicId1, ChannelType.PUBLIC);
        channelService.join(channelJoin);

        System.out.println("create----------------------------------------");
        MessageCRUDDTO messageCRUDDTO1 = MessageCRUDDTO.create(userId1, publicId1, "hello", null);
        MessageCRUDDTO messageCRUDDTO2 = MessageCRUDDTO.create(userId2, publicId1, "hello2", null);
        MessageCRUDDTO messageCRUDDTO3 = MessageCRUDDTO.create(userId1, publicId1, "delete", null);
        MessageCRUDDTO messageCRUDDTO4 = MessageCRUDDTO.create(userId2, publicId1, "update", null);
        Message message1 = messageService.create(messageCRUDDTO1);
        Message message2 = messageService.create(messageCRUDDTO2);
        Message message3 = messageService.create(messageCRUDDTO3);
        Message message4 = messageService.create(messageCRUDDTO4);

        System.out.println("find All----------------------------------------");
        List<Message> list = messageService.findAllByChannelId(publicId1.toString());
        for (Message message : list) {
            System.out.println(message);
        }

        System.out.println("find----------------------------------------");
        Message find = messageService.find(message1.getMessageId().toString());
        System.out.println(find);

        System.out.println("delete----------------------------------------");
        MessageCRUDDTO delete = MessageCRUDDTO.delete(serverId1, publicId1, message3.getMessageId());
        messageService.delete(delete);

        System.out.println("find All----------------------------------------");
        List<Message> list2 = messageService.findAllByChannelId(publicId1.toString());
        for (Message message : list2) {
            System.out.println(message);
        }

        System.out.println("update----------------------------------------");
        MessageCRUDDTO update = MessageCRUDDTO.update(null, "Update!!!!");
        messageService.update(message4.getMessageId().toString(), update);


        System.out.println("find All----------------------------------------");
        List<Message> list3 = messageService.findAllByChannelId(publicId1.toString());
        for (Message message : list2) {
            System.out.println(message);
        }

    }
}
