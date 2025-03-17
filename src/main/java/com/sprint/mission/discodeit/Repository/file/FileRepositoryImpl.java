package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.Exception.CommonExceptions;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileRepositoryImpl<T> implements FileRepository<T>{
    private final Path path;

    public FileRepositoryImpl(Path path) {
        this.path = path;
    }

    @Override
    public void init() {
        Path directory = path.getParent();
        if (Files.exists(directory) == false) {
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
    public T load() {
        if (Files.exists(path) == true) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                T savedlist = (T) ois.readObject();

                return savedlist;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("채널 리스트 로드 실패");
                throw new RuntimeException(e);
            }
        }
        throw CommonExceptions.File_NOT_FOUND;
    }

    @Override
    public void save(T list) {
        init();
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(list);

        } catch (IOException e) {
            System.out.println("채널 리스트 저장 실패");
            throw new RuntimeException(e);
        }
    }
}
