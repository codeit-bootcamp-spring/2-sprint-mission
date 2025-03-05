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

    private final Path serverListFile;

    public FileUserRepository() {
        this.serverListFile = Paths.get(System.getProperty("user.dir"), "data", "serverList.ser");
        this.serverList = new LinkedList<>();
        this.messageList = new HashMap<>();
        loadServerList();
    }

    private void init() {
        Path directory = serverListFile.getParent();
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

    @Override
    public void save(Server server) {
        System.out.println("FileUserRepository 저장 시점: " + server.getId());

        // 중복 서버 체크
        if (serverList.stream().noneMatch(s -> s.getId().equals(server.getId()))) {
            serverList.add(server);
            saveServerList();
        }
    }

    private void loadServerList() {
        if (Files.exists(serverListFile)) {
            try (FileInputStream fis = new FileInputStream(serverListFile.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                this.serverList = (List<Server>) ois.readObject();

                System.out.println("서버 리스트 로드 완료: " + serverListFile);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("서버 리스트 로드 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void saveServerList() {
        init();

        try (FileOutputStream fos = new FileOutputStream(serverListFile.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(serverList);

        } catch (IOException e) {
            System.out.println("서버 리스트 저장 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Server> getServerList() {
        loadServerList();
        return serverList;
    }

    @Override
    public void setServerList(List<Server> serverList) {
        this.serverList = serverList;
        saveServerList();
    }

    @Override
    public Map<UUID, Queue<Message>> getMessageList() {
        return messageList;
    }

    @Override
    public void setMessageList(Map<UUID, Queue<Message>> messageList) {
        this.messageList = messageList;
    }

}

