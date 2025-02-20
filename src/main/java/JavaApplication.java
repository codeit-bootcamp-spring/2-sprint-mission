import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.jcf.JCFChannel;
import com.sprint.mission.discodeit.jcf.JCFMessage;
import com.sprint.mission.discodeit.jcf.JCFService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JavaApplication {
    static UserService userService = new JCFService();
    static ChannelService channelService = new JCFChannel();
    static MessageService messageService = new JCFMessage(userService, channelService);

    public static void main(String[] args) {
        for(int i = 0; i<5; i++){
            UUID uuid = UUID.randomUUID();
            long millis = System.currentTimeMillis();
            testCreate(uuid, millis);
            if( i == 2 ){
                delete(uuid);
            }

            if( i == 3 ){
                findById(uuid);
            }

            if( i == 4 ){
                update(uuid);
            }

        }

        findAll();

    }

    public static void testCreate(UUID uuid, long millis) {
        userService.create(new User(uuid, millis));
        channelService.create(new Channel(uuid, millis));
        messageService.create(new Message(uuid, millis));
    }

    public static void findById(UUID uuid) {
        userService.findById(uuid);
        channelService.findById(uuid);
        messageService.findById(uuid);
    }

    public static void update(UUID uuid) {
        userService.update(uuid);
        channelService.update(uuid);
        messageService.update(uuid);
    }

    public static void delete(UUID uuid) {
        userService.delete(uuid);
        channelService.delete(uuid);
        messageService.delete(uuid);
    }

    public static void findAll() {
        System.out.println("===User===");
        System.out.println(userService.findAll());
        System.out.println("===Channel===");
        System.out.println(channelService.findAll());
        System.out.println("===Message===");
        System.out.println(messageService.findAll());
    }

}
