package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.entity.Container.Container;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class FileServerRepository implements ServerRepository {
    private List<Container> containerList;
    private final Path path =  Paths.get(System.getProperty("user.dir"), "data", "ContainerList.ser");

    public FileServerRepository() {
        containerList = new LinkedList<>();
        loadContainerList();
    }

    // 채널 리스트를 저장할 디렉토리가 있는지 확인
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

    private void loadContainerList() {
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                List<Container> list = (List<Container>) ois.readObject();
                for (Container data : list) {
                    Container c = new Container(data.getId(), data.getCreatedAt(), data.getName());
                    this.containerList.add(c);
                    System.out.println("채널 로드 완료 - ID 유지: " + c.getId());
                }

                System.out.println("채널 리스트 로드 완료: " + path);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("채널 리스트 로드 실패");
                throw new RuntimeException(e);
            }
        }
    }

    public void save(Container container) {
        containerList.add(container);
        saveChannelList();
    }

    private void saveChannelList() {
        init();

        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(containerList);

        } catch (IOException e) {
            System.out.println("채널 리스트 저장 실패");
            throw new RuntimeException(e);
        }
    }

    public void updateContainerList(List<Container> containerList) {
        this.containerList = containerList;
        saveChannelList();
    }

    public List<Container> getContainerList() {
        return containerList;
    }


}
