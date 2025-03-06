package main.com.sprint.mission.discodeit.service;

import main.com.sprint.mission.discodeit.entity.Channel;

import java.util.Optional;
import java.util.UUID;

// interface(인터페이스)
// 하위모듈에서 어떤 기능들을 구현할지 선언만 해놓은 공간
// CRUD(생성, 읽기, 수정, 삭제)
public interface ChannelService {
    // Create - 생성
    void createChannel(String name, String topic);
    // Read - 읽기, 조회
    Optional<Channel> getOneChannel(UUID id);
    List<Channel> getAllChannel();
    // Update - 수정
    void updateChannel(String newname, String newtopic, UUID id);
    // Delete - 삭제
    void deleteChannel(UUID id);
}
