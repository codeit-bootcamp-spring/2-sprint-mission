package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;

public class RepositoryType {
    public static final String TYPE = "FILE";

    public static UserRepository getUserRepository() {
        return TYPE.equals("FILE") ? new FileUserRepository() : new JCFUserRepository();
    }

    public static ChannelRepository getChannelRepository() {
        return TYPE.equals("FILE") ? new FileChannelRepository() : new JCFChannelRepository();
    }

    public static MessageRepository getMessageRepository() {
        return TYPE.equals("FILE") ? new FileMessageRepository() : new JCFMessageRepository();
    }
}
