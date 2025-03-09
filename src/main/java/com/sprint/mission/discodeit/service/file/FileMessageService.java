package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private final File file;
    private final Map<UUID, Message> data;
    private final ChannelService channelService;
    private final UserService userService;

    public FileMessageService(String filename, ChannelService channelService, UserService userService) {
        this.file = new File(filename);
        this.channelService = channelService;
        this.userService = userService;
        this.data = loadData();
    }

    private void saveData() {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<UUID, Message> loadData() {
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object data = ois.readObject();
            return (Map<UUID, Message>) data;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // 파일 읽기 실패해서, 빈 맵 객체 생성 (그럼 만약 파일이 있는데 못 읽은거라면, 기존 데이터가 없어지는건가?)
            return new HashMap<>();
        }
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        try {
            // 해당 채널과, 유저가 존재하는지 확인, 메세지는 채널과 유저에게 의존하기 때문.
//            System.out.println("DEBUG: 채널 존재 여부 확인 - " + channelId);
            channelService.findById(channelId);
//            System.out.println("DEBUG: 채널 존재 여부 확인 - " + authorId);
            userService.findById(authorId);
        } catch (NoSuchElementException e) {
            System.err.println("메세지를 보낼 유저 혹은 채널이 존재하지 않음.");
            e.printStackTrace();  // 예외 발생 위치 확인
            throw e;
        }

        // 존재한다면, 메세지 객체 생성하고, 내용 저장 실행
        Message message = new Message(content, channelId, authorId);
        data.put(message.getId(), message);
        saveData();
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId))
                .orElseThrow(() -> new NoSuchElementException(messageId + " 존재하지 않는 메세지 아이디 입니다."));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
        // new는 객체로 생성하는 것 아닌가?
        // 동작 방식
        // data.values() → Collection<Message> 형태의 데이터 가져옴
        // new ArrayList<>(data.values()) → 이 데이터를 기반으로 새로운 ArrayList<Message> 생성
        // 새로운 ArrayList가 반환됨! 즉, 원본 데이터를 수정하지 않고, 새로운 리스트를 만들어 안전하게 반환하는 역할!
        // 복사해서 반환
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = findById(messageId);
        message.update(newContent);
        saveData();
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        if (!data.containsKey(messageId)) {
            throw new NoSuchElementException(messageId + " 삭제할 대상이 존재하지 않습니다.");
        }
        data.remove(messageId);
        saveData();
    }
}
