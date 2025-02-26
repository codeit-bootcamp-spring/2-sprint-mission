package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

public class MessageController {
    private final MessageService messageService = new JCFMessageService();
}