package com.sprint.mission.config;

import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_ACCESS_DENIED_TO_BEAN_STORAGE;

import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.infra.ChannelRepository;
import com.sprint.mission.discodeit.infra.MessageRepository;
import com.sprint.mission.discodeit.infra.UserRepository;
import com.sprint.mission.discodeit.infra.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.infra.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.infra.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import java.util.HashMap;
import java.util.Map;

public class Beans {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public Beans() {
        setUpFileBeans();
    }

    private void setUpFileBeans() {
        saveBean(UserService.class, new FileUserService());
        saveBean(MessageService.class,
                new FileMessageService(findBean(UserService.class)));
        saveBean(ChannelService.class,
                new FileChannelService(findBean(UserService.class)));

        saveBean(UserController.class, new UserController(findBean(UserService.class)));
        saveBean(MessageController.class, new MessageController(findBean(MessageService.class)));
        saveBean(ChannelController.class, new ChannelController(findBean(ChannelService.class)));
    }

    private void setUpJCFBeans() {
        saveBean(UserRepository.class, new JCFUserRepository());
        saveBean(ChannelRepository.class, new JCFChannelRepository());
        saveBean(MessageRepository.class, new JCFMessageRepository());

        saveBean(UserService.class, new JCFUserService(findBean(UserRepository.class)));
        saveBean(MessageService.class,
                new JCFMessageService(findBean(MessageRepository.class), findBean(UserService.class)));
        saveBean(ChannelService.class,
                new JCFChannelService(findBean(ChannelRepository.class), findBean(UserService.class)));

        saveBean(UserController.class, new UserController(findBean(UserService.class)));
        saveBean(MessageController.class, new MessageController(findBean(MessageService.class)));
        saveBean(ChannelController.class, new ChannelController(findBean(ChannelService.class)));
    }

    private <T> void saveBean(Class<T> clazz, T instance) {
        beans.put(clazz, instance);
    }

    public <T> T findBean(Class<T> clazz) {
        return clazz.cast(beans.get(clazz));
    }

    public Map<Class<?>, Object> getBeans() {
        throw new UnsupportedOperationException(ERROR_ACCESS_DENIED_TO_BEAN_STORAGE.getMessageContent());
    }
}
