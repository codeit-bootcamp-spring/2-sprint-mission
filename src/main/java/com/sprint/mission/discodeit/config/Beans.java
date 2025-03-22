package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.HashMap;
import java.util.Map;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_ACCESS_DENIED_TO_BEAN_STORAGE;

public class Beans {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public Beans() {
        initializeJCFBeans();
        initializeBeans();
    }

    private void initializeBeans() {
        saveBean(BinaryContentService.class,
                new BasicBinaryContentService(findBean(BinaryContentRepository.class)));
        saveBean(UserService.class,
                new BasicUserService(findBean(UserRepository.class), findBean(UserStatusRepository.class)));
        saveBean(MessageService.class,
                new BasicMessageService(findBean(MessageRepository.class), findBean(UserRepository.class)));
        saveBean(ChannelService.class,
                new BasicChannelService(findBean(ChannelRepository.class), findBean(ReadStatusRepository.class)));

        saveBean(UserController.class, new UserController(findBean(UserService.class), findBean(BinaryContentService.class)));
        saveBean(MessageController.class, new MessageController(findBean(MessageService.class)));
        saveBean(ChannelController.class, new ChannelController(findBean(ChannelService.class), findBean(MessageService.class), findBean(UserService.class), findBean(ReadStatusService.class)));
    }

    private void initializeFileBeans() {
        saveBean(UserRepository.class, new FileUserRepository());
        saveBean(ChannelRepository.class, new FileChannelRepository());
        saveBean(MessageRepository.class, new FileMessageRepository());
    }

    private void initializeJCFBeans() {
        saveBean(ReadStatusRepository.class, new JCFReadStatusRepository());
        saveBean(UserStatusRepository.class, new JCFUserStatusRepository());
        saveBean(BinaryContentRepository.class, new JCFBinaryContentRepository());
        saveBean(UserRepository.class, new JCFUserRepository());
        saveBean(ChannelRepository.class, new JCFChannelRepository());
        saveBean(MessageRepository.class, new JCFMessageRepository());
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
