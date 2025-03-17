import com.sprint.mission.discodeit.DTO.Server.ServerCRUDDTO;
import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFServerRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ServerService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicServerService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.List;
import java.util.UUID;

public class ServerServiceTest {
    public static void main(String[] args) {
        UserRepository userRepository = new JCFUserRepository();
        ServerRepository serverRepository = new JCFServerRepository();

        BinaryContentRepository binaryContentRepository = new JCFBinaryContentRepository();
        UserStatusRepository userStatusRepository = new JCFUserStatusRepository();
        BinaryContentService binaryContentService = new BasicBinaryContentService(binaryContentRepository);
        UserService userService = new BasicUserService(userRepository, binaryContentRepository, userStatusRepository);
        ServerService serverService = new BasicServerService(userRepository, serverRepository);

        BinaryContent content1 = binaryContentService.create();
        UserCRUDDTO userDTO1 = UserCRUDDTO.create("test1", "test1", "123",content1);
        UUID test1 = userService.register(userDTO1);
        System.out.println("create----------------------------------------");
        ServerCRUDDTO serverTest1 = ServerCRUDDTO.create(test1, "test1");
        ServerCRUDDTO serverTest2 = ServerCRUDDTO.create(test1, "test2");
        ServerCRUDDTO serverTest3 = ServerCRUDDTO.create(test1, "test3");

        UUID serverId1 = serverService.create(serverTest1);
        UUID serverId2 = serverService.create(serverTest2);
        UUID serverId3 = serverService.create(serverTest3);

        System.out.println("find All----------------------------------------");
        List<Server> servers = serverService.findServerAll(test1.toString());
        for (Server server : servers) {
            System.out.println(server);
        }

        System.out.println("find----------------------------------------");
        Server find = serverService.find(serverId2.toString());
        System.out.println(find);

        System.out.println("delete----------------------------------------");
        ServerCRUDDTO delete = ServerCRUDDTO.delete(serverId3, test1);
        serverService.delete(delete);

        System.out.println("find All----------------------------------------");
        List<Server> servers2 = serverService.findServerAll(test1.toString());
        for (Server server : servers2) {
            System.out.println(server);
        }

        System.out.println("update----------------------------------------");
        ServerCRUDDTO update = ServerCRUDDTO.update(null, null, "test5");
        serverService.update(serverId2.toString(), update);


        System.out.println("find All----------------------------------------");
        List<Server> servers3 = serverService.findServerAll(test1.toString());
        for (Server server : servers3) {
            System.out.println(server);
        }

    }
}
