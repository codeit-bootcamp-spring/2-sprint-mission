package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private static final JCFMessageService INSTANCE = new JCFMessageService();
    private final Map<UUID, Message> data;

    private JCFMessageService() {
        data = new HashMap<>();
    }

    public static JCFMessageService getInstance() {
        return INSTANCE;
    }

    @Override
    public UUID create(String content, UUID userUuid, UUID channelUuid) {
        Message text = createMessage(content, userUuid, channelUuid);
        data.put(text.getUuid(), text);
        return text.getUuid();
    }

    @Override
    public Message read(UUID channelUuid) {
        Message lastMessage = findLastMessage(channelUuid);
        if (lastMessage == null) {
            System.out.println("[Error] 읽을 메시지가 없습니다)");
            return null;
        }
        return lastMessage;
    }

    @Override
    public List<Message> readAll(UUID channelUuid) {
        List<Message> allMessage = findAllMessage(channelUuid);
        if (allMessage == null) {
            System.out.println("[Error] 읽을 메시지가 없습니다.");
            return List.of();
        }
        return findAllMessage(channelUuid);
    }

    @Override
    public void update(int num, String content) {
        UUID messageUuid = findUuidByNum(num);
        if (messageUuid == null) {
            System.out.println("[Error] 해당 메시지가 존재하지 않습니다");
            return;
        }
        data.get(findUuidByNum(num)).updateContent(content);
        System.out.println("[Info] 메시지가 정상 수정 되었습니다.");
    }

    @Override
    public void delete(int num) {
        UUID messageUuid = findUuidByNum(num);
        if (messageUuid == null) {
            System.out.println("[Error] 삭제할 메시지가 존재하지 않습니다");
            return;
        }
        data.remove(findUuidByNum(num));
        System.out.println("[Info] 메시지가 정상 삭제 처리 되었습니다.");
    }

    public List<Message> getMyMessage(UUID userUuid, UUID channelUuid) {
        List<Message> myMessage = data.values().stream()
                .filter(
                        m -> m.getChannelName().equals(JCFChannelService.getInstance().getChannelName(channelUuid))
                                && m.getUserName().equals(JCFUserService.getInstance().getUserName(userUuid))
                )
                .toList();
        if (myMessage.isEmpty()) {
            System.out.println("[Error] 내 메시지가 없습니다");
            return List.of();
        }

        return myMessage;
    }

    public boolean isMessageNotExist(int num, String userName) {
        boolean notExists = data.values().stream()
                .noneMatch(m -> m.getMessageNum() == num && m.getUserName().equals(userName));
        if (notExists) {
            System.out.println("[Error] 메시지가 존재하지 않습니다");
        }
        return notExists;
    }

    private Message createMessage(String content, UUID userUuid, UUID channelUuid) {
        return new Message(content, JCFUserService.getInstance().getUserName(userUuid), JCFChannelService.getInstance().getChannelName(channelUuid));
    }

    private Message findLastMessage(UUID channelUuid) {
        return data.values().stream()
                .filter(m -> m.getChannelName().equals(JCFChannelService.getInstance().getChannelName(channelUuid)))
                .max(Comparator.comparing(Message::getCreatedAt))
                .orElse(null);
    }

    private List<Message> findAllMessage(UUID channelUuid) {
        return data.values().stream()
                .filter(m -> m.getChannelName().equals(JCFChannelService.getInstance().getChannelName(channelUuid)))
                .toList();
    }

    private UUID findUuidByNum(int num) {
        return data.values().stream()
                .filter(m -> m.getMessageNum() == num)
                .map(Message::getUuid)
                .findFirst()
                .orElse(null);
    }
}
