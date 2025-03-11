package com.sprint.mission;

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
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class AppConfig {

    public UserService userService(){
        return BasicUserService.getInstance(userRepository());
    }

    public ChannelService channelService(){
        return BasicChannelService.getInstance(channelRepository());
    }

    public MessageService messageService(){
        return BasicMessageService.getInstance(userService(), channelService(), messageRepository());
    }

    public UserRepository userRepository(){
        return JCFUserRepository.getInstance();
    }

    public ChannelRepository channelRepository(){
        return JCFChannelRepository.getInstance();
    }

    public MessageRepository messageRepository(){
        return JCFMessageRepository.getInstance();
    }

}
