package com.sprint.mission.discodeit.config;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_ACCESS_DENIED_TO_BEAN_STORAGE;
import static com.sprint.mission.discodeit.constant.FilePath.CHANNEL_FILE;
import static com.sprint.mission.discodeit.constant.FilePath.MESSAGE_FILE;
import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.discodeit.constant.FilePath.USER_FILE;

import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.controller.MessageController;
import com.sprint.mission.discodeit.controller.UserController;
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
import java.util.HashMap;
import java.util.Map;

public class Beans {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public Beans() {
        initializeFileBeans();
        initializeBeans();
    }

    private void initializeBeans() {
        saveBean(UserService.class,
                new BasicUserService(findBean(UserRepository.class)));
        saveBean(MessageService.class,
                new BasicMessageService(findBean(MessageRepository.class), findBean(UserRepository.class)));
        saveBean(ChannelService.class,
                new BasicChannelService(findBean(ChannelRepository.class), findBean(UserRepository.class)));

        saveBean(UserController.class, new UserController(findBean(UserService.class)));
        saveBean(MessageController.class, new MessageController(findBean(MessageService.class)));
        saveBean(ChannelController.class, new ChannelController(findBean(ChannelService.class)));
    }

    private void initializeFileBeans() {
        saveBean(UserRepository.class, new FileUserRepository(STORAGE_DIRECTORY.resolve(USER_FILE)));
        saveBean(ChannelRepository.class, new FileChannelRepository(STORAGE_DIRECTORY.resolve(CHANNEL_FILE)));
        saveBean(MessageRepository.class, new FileMessageRepository(STORAGE_DIRECTORY.resolve(MESSAGE_FILE)));
    }

    private void initializeJCFBeans() {
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
