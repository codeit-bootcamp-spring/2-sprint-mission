package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.entity.Message;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class FileChannelRepository  {
//    private List<Message> messageList;
//    private final Path path =  Paths.get(System.getProperty("user.dir"), "data", "MessageList.ser");
//
//    public FileChannelRepository() {
//        messageList = new LinkedList<>();
//        loadMessageList();
//    }
//
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
//    private void loadMessageList() {
//        if (Files.exists(path)) {
//            try (FileInputStream fis = new FileInputStream(path.toFile());
//                 ObjectInputStream ois = new ObjectInputStream(fis)) {
//
//                List<Message> list = (List<Message>) ois.readObject();
//                for (Message data : list) {
//                    Message message = new Message(data.getId(), data.getCreatedAt(), data.getStr());
//                    this.messageList.add(message);
//                    System.out.println("메시지 로드 완료 - ID 유지: " + message.getId());
//                }
//
//                System.out.println("메시지 리스트 로드 완료: " + path);
//            } catch (IOException | ClassNotFoundException e) {
//                System.out.println("메시지 리스트 로드 실패");
//                throw new RuntimeException(e);
//            }
//        }
//    }
//    @Override
//    public void save(Message message) {
//        messageList.add(message);
//        saveMessageList();
//    }
//
//    private void saveMessageList() {
//        init();
//
//        try (FileOutputStream fos = new FileOutputStream(path.toFile());
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//
//            oos.writeObject(messageList);
//
//        } catch (IOException e) {
//            System.out.println("메시지 리스트 저장 실패");
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    @Override
//    public void updateMessageList(List<Message> list) {
//        this.messageList = list;
//        saveMessageList();
//    }
//
//    @Override
//    public List<Message> getList() {
//        return messageList;
//    }
}
