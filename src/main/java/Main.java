import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ServerService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFServerService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();
        ServerService server = new JCFServerService();



        server.initServer();


        for (int i = 0; i < 5; i++) {
            User user = userService.randomRegister();
            Message message = messageService.randomWrite();
            server.addUser(user);
//            server.addChannel(channelService.testRegister(message));
        }

        server.printAllChannels();
        server.printAllUsers();


    }
}
