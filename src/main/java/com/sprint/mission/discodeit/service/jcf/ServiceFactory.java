package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public class ServiceFactory {
    private static final ChannelService channelService = JCFChannelService.getInstance();
    private static final UserService userService = JCFUserService.getInstance();
    private static final MessageService messageService = JCFMessageService.getInstance(channelService, userService);

    //싱글톤으로 유지되는 서비스 객체 반환 -> 팩토리 패턴 -> 의존성 주입 문제 해결
    //의존성 관리를 한 곳에
    public static ChannelService getChannelService() {
        return channelService;
    }

    public static UserService getUserService() {
        return userService;
    }

    public static MessageService getMessageService() {
        return messageService;
    }
}
