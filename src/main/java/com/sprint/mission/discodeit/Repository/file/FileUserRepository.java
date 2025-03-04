package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private List<Server> serverList;
    private Map<UUID, Queue<Message>> messageList;

    public FileUserRepository() {
        serverList = new LinkedList<>();
        messageList = new HashMap<>();
    }

    @Override
    public List<Server> getServerList() {
        return serverList;
    }

    @Override
    public void setServerList(List<Server> serverList) {
        this.serverList = serverList;
    }

    @Override
    public Map<UUID, Queue<Message>> getMessageList() {
        return messageList;
    }

    @Override
    public void setMessageList(Map<UUID, Queue<Message>> messageList) {
        this.messageList = messageList;
    }

    @Override
    public void save(Server server) {
        //디렉토리 체크 및 파일 경로 생성하기
        Path directory = Paths.get(System.getProperty("user.dir"), "data/server");
        init(directory);
        Path filsPath = directory.resolve(server.getId().toString().concat(".ser"));

        //해당 파일이 이미 만들어졌는지 체크하기
        try (FileOutputStream fos = new FileOutputStream(filsPath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(server);
        } catch (IOException e){
            //로그 찍기
            System.out.println("User Repository 저장 실패");
            throw new RuntimeException(e);
        }
        serverList.add(server);
    }

    private void init(Path directory ) {
        if (!Files.exists(directory)) {
            try{
                Files.createDirectories(directory);
            } catch (IOException e){
                System.out.println("디렉토리 저장 실패");
                throw new RuntimeException(e);
            }
        }
    }
}
