package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.entity.Channel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class FileServerRepository   {
//    private List<Channel> channelList;
//    private final Path path =  Paths.get(System.getProperty("user.dir"), "data", "ContainerList.ser");
//
//    public FileServerRepository() {
//        channelList = new LinkedList<>();
//        loadContainerList();
//    }
//
//    // 채널 리스트를 저장할 디렉토리가 있는지 확인
//    private void init() {
//        Path directory = path.getParent();
//        if (!Files.exists(directory)) {
//            try {
//                Files.createDirectories(directory);
//                System.out.println("디렉토리 생성 완료: " + directory);
//            } catch (IOException e) {
//                System.out.println("디렉토리 생성 실패");
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    private void loadContainerList() {
//        if (Files.exists(path)) {
//            try (FileInputStream fis = new FileInputStream(path.toFile());
//                 ObjectInputStream ois = new ObjectInputStream(fis)) {
//
//                List<Channel> list = (List<Channel>) ois.readObject();
//                for (Channel data : list) {
//                    Channel c = new Channel(data.getId(), data.getCreatedAt(), data.getName());
//                    this.channelList.add(c);
//                    System.out.println("채널 로드 완료 - ID 유지: " + c.getId());
//                }
//
//                System.out.println("채널 리스트 로드 완료: " + path);
//            } catch (IOException | ClassNotFoundException e) {
//                System.out.println("채널 리스트 로드 실패");
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    public void save(Channel channel) {
//        channelList.add(channel);
//        saveChannelList();
//    }
//
//    private void saveChannelList() {
//        init();
//
//        try (FileOutputStream fos = new FileOutputStream(path.toFile());
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//
//            oos.writeObject(channelList);
//
//        } catch (IOException e) {
//            System.out.println("채널 리스트 저장 실패");
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void updateContainerList(List<Channel> channelList) {
//        this.channelList = channelList;
//        saveChannelList();
//    }
//
//    public List<Channel> getContainerList() {
//        return channelList;
//    }
//

}
