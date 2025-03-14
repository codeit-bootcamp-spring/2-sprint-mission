package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.repository.AbstractRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public abstract class AbstractFileRepository<T extends BaseEntity> extends AbstractRepository<T> {
    protected Path directory;

    protected AbstractFileRepository(Class<T> entityClass, Path directory) {
        super(entityClass, new ConcurrentHashMap<>());
        this.directory = directory;
        init(directory);
        loadFromFile();             // 파일로 저장된 user들이 있다면 users에 불러오기, repository가 만들어질때만 파일을 불러옴. 파일 추가, 삭제는 계속 진행
    }

    public void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveToFile(Path filePath, T entity) {
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(entity);
        } catch (IOException e) {
            e.printStackTrace();        //예외 발생 위치 출력
        }
    }

    public void loadFromFile() {
        if (Files.exists(directory)) {
            try (Stream<Path> paths = Files.list(directory)
            ) {
                paths.filter(Files::isRegularFile)
                        .forEach(path -> {
                            try (FileInputStream fis = new FileInputStream(path.toFile());
                                 ObjectInputStream ois = new ObjectInputStream(fis)
                            ) {
                                T inputEntity = (T) ois.readObject();
                                storage.put(inputEntity.getId(), inputEntity);
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFile(UUID id) {
        //file 삭제
        try {
            if (!Files.deleteIfExists(directory.resolve(id.toString() + ".ser"))) {
                throw new NoSuchElementException("해당 Id를 가진 파일이 존재하지 않습니다. : " + id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
