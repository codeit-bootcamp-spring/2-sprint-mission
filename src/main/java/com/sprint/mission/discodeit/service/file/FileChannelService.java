package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private final File file;
    private final Map<UUID, Channel> data;

    public FileChannelService(String filename) {
        this.file = new File(filename);
        this.data = loadData(); // 기존 데이터 불러오기
    }

    /**
     * 데이터를 파일에 저장하는 메서드
     */
    private void saveData() {
        try (FileOutputStream fos = new FileOutputStream("channel.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data); // 채널 map 객체 내용 다 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
//            oos.writeObject(data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 파일에서 데이터를 불러오는 메서드
     */
    private Map<UUID, Channel> loadData() {
        if (!file.exists()) {
            return new HashMap<>(); // 파일이 없으면 빈 맵 객체 반환
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object data = ois.readObject();
            return (Map<UUID, Channel>) data; // 저장된 데이터 로드(불러오기)

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


    /**
     * 새로운 채널 생성 및 저장
     */
    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        data.put(channel.getId(), channel);
        /**
         * 1) 채널 객체를 생성 후, map에 객체 담음.
         * 2) 파일에 입력할 수 있도록 자바 객체를 바이트 스트림으로 변환 = 객체 직렬화
         * 3) ObjectOutputStream 직렬화 클래스 (saveData())
         */
        saveData(); // 파일에 저장
        return channel;
    }

    /**
     * ID로 채널 조회
     */
    @Override
    public Channel findById(UUID channelId) {
        return Optional.ofNullable(data.get(channelId))
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    /**
     * 모든 채널 조회
     */
    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    /**
     * 채널 이름 업데이트 후 저장
     */
    @Override
    public Channel updateName(UUID channelId, String newName) {
        Channel channel = findById(channelId);
        channel.updateName(newName);
        saveData();
        return channel;
    }

    /**
     * 채널 설명 업데이트 후 저장
     */
    @Override
    public Channel updateDesc(UUID channelId, String newDescription) {
        Channel channel = findById(channelId);
        channel.updateDesc(newDescription);
        saveData();
        return channel;
    }

    /**
     * 채널 삭제
     */
    @Override
    public void delete(UUID channelId) {
        if (!data.containsKey(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        data.remove(channelId);
        saveData(); // 삭제 후 파일 업데이트
    }
}
