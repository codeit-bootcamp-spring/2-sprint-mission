package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // 메시지 생성
    void createMessage(Message message);

    // 메시지 읽기
    // UUID id를 파라미터로 사용해 불러오기엔, UUID는 난수값이라 미리 알기가 힘들고 Message DB를 따로 사용하지 않으므로 알길이 없음
    // 그렇다고 content로 가져오기엔 content는 수정도 가능해야하고 중복도 가능해야함
    // 일단, 여기서는 해당 객체에서 UUID를 추출해서 그걸로 가져와보자.
    Message getMessage(UUID id);

    // 메시지 모두 읽기
    List<Message> getAllMessages();

    // 메시지 수정
    // 내용만 수정하므로 Message 객체를 받기보단 content만 받는게 나을듯
    void updateMessage(UUID id, User user, String content);

    // 메시지 삭제
    void deleteMessage(UUID id, User user);

    // 해당 채널의 해당 유저의 메시지만 검색
    List<Message> searchMessageByChannelAndUser(String channelName, String username);

    // 해당 채널의 메시지를 포함 내용으로 검색
    List<Message> searchMessagesContaining(String channelName, String content);
}
