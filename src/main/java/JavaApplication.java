import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JavaApplication {
    static UserService userService = JCFUserService.getInstance();
    static ChannelService channelService = JCFChannelService.getInstance();
    static MessageService messageService = JCFMessageService.getInstance(userService, channelService);;

    public static void main(String[] args) {


        for(int i = 0; i<5; i++){
            UUID userUuid = UUID.randomUUID();
            UUID channelUuid = UUID.randomUUID();
            UUID messageUuid = UUID.randomUUID();
            long millis = System.currentTimeMillis();

            testCreate(userUuid, channelUuid, messageUuid, millis);
            if( i == 2 ){
                delete(userUuid, channelUuid, messageUuid);
            }

            if( i == 3 ){
                findById(userUuid, channelUuid, messageUuid);
            }

            if( i == 4 ){
                update(userUuid, channelUuid, messageUuid);
            }

        }

        findAll();

    }

    public static void testCreate(UUID userUuid, UUID channelUuid, UUID messageUuid, long millis) {
        userService.create(new User(userUuid, millis, "진호", "yangjinho826@naver.com"));
        channelService.create(new Channel(channelUuid, millis, "진호채널", "테스트"));
        messageService.create(new Message(messageUuid, millis, "메신저", channelUuid, userUuid), channelUuid, userUuid);
    }

    public static void findById(UUID userUuid, UUID channelUuid, UUID messageUuid) {
        System.out.println(userService.findById(userUuid));
        System.out.println(channelService.findById(channelUuid));
        System.out.println(messageService.findById(messageUuid));
    }

    public static void update(UUID userUuid, UUID channelUuid, UUID messageUuid) {
        userService.update(userUuid, "진호양", "yangjinho@naver.com");
        channelService.update(channelUuid, "진호양채널", "테스트1");
        messageService.update(messageUuid, "메신저1",channelUuid, userUuid);
    }

    public static void delete(UUID userUuid, UUID channelUuid, UUID messageUuid) {
        userService.delete(userUuid);
        channelService.delete(channelUuid);
        messageService.delete(messageUuid);
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
