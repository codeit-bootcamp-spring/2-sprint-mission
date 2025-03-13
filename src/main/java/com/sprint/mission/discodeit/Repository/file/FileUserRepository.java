package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private List<Server> serverList;
    private Map<UUID, Queue<Message>> messageList;

    private final Path path =  Paths.get(System.getProperty("user.dir"), "data", "serverList.ser");

    public FileUserRepository() {
        this.serverList = new LinkedList<>();
        this.messageList = new HashMap<>();
        //이미 저장된 데이터 불러오기
        loadServerList();
    }

    // 서버 리스트를 저장할 디렉토리가 있는지 확인
    private void init() {
        Path directory = path.getParent();
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                System.out.println("디렉토리 생성 완료: " + directory);
            } catch (IOException e) {
                System.out.println("디렉토리 생성 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void loadServerList() {
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                List<Server> list = (List<Server>) ois.readObject();
                for (Server server : list) {
                    Server s = new Server(server.getServerId(), server.getCreatedAt(), server.getName());

                    this.serverList.add(s);
                    System.out.println("서버 로드 완료 - ID 유지: " + s.getServerId());
                }

                System.out.println("서버 리스트 로드 완료: " + path);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("서버 리스트 로드 실패");
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void save(Server server) {
        // 중복 서버 체크
        if (serverList.stream().noneMatch(s -> s.getServerId().equals(server.getServerId()))) {
            serverList.add(server);
            //현재 리스트에 저장과 동시에 디스크에 기록
            saveServerList();
        }
    }

    private void saveServerList() {
        init();

        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(serverList);

        } catch (IOException e) {
            System.out.println("서버 리스트 저장 실패");
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Server> getServerList() {
        return serverList;
    }

    @Override
    public void updateServerList(List<Server> serverList) {
        this.serverList = serverList;
        saveServerList();
    }

    @Override
    public Map<UUID, Queue<Message>> getMessageList() {
        return messageList;
    }

    @Override
    public void updateMessageList(Map<UUID, Queue<Message>> messageList) {
        this.messageList = messageList;
    }

}

