package com.sprint.mission.discodeit;

/*import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.Repository;
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
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;*/


public class JavaApplication {
    /*static User setupUser(UserService userService) {
        User user = userService.createUser("testUser1", "test1@codeit.com", "1234");
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.createChannel("testChannel1");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.createMessage(author.getId(), "testMessage1", channel.getId());
        System.out.println("메시지 생성: " + message.getId());
    }*/

    public static void main(String[] args) {
        // JCF
        /*UserRepository jcfUserRepository = JCFUserRepository.getInstance();
        ChannelRepository jcfChannelRepository = JCFChannelRepository.getInstance();
        MessageRepository jcfMessageRepository = JCFMessageRepository.getInstance();

        UserService jcfUserService = new JCFUserService(jcfUserRepository);
        ChannelService jcfChannelService = new JCFChannelService(jcfUserRepository, jcfChannelRepository, jcfMessageRepository);
        MessageService jcfMessageService = new JCFMessageService(jcfUserRepository, jcfChannelRepository, jcfMessageRepository);

        User user1 = setupUser(jcfUserService);
        Channel channel1 = setupChannel(jcfChannelService);
        jcfChannelService.addChannelParticipant(channel1.getId(), user1.getId());
        messageCreateTest(jcfMessageService, channel1, user1);*/


        // File
        /*UserRepository fileUserRepository = FileUserRepository.getInstance();
        ChannelRepository fileChannelRepository = FileChannelRepository.getInstance();
        MessageRepository fileMessageRepository = FileMessageRepository.getInstance();

        UserService fileUserService = new FileUserService(fileUserRepository);
        ChannelService fileChannelService = new FileChannelService(fileUserRepository, fileChannelRepository, fileMessageRepository);
        MessageService fileMessageService = new FileMessageService(fileUserRepository, fileChannelRepository, fileMessageRepository);

        User user2 = setupUser(fileUserService);
        Channel channel2 = setupChannel(fileChannelService);
        messageCreateTest(fileMessageService, channel2, user2);*/
    }
}
