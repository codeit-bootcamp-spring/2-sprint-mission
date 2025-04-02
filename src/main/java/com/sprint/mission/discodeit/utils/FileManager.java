package com.sprint.mission.discodeit.utils;

import com.sprint.mission.discodeit.constant.SubDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class FileManager {

    private final String BASE_DIR;
    private final String DATA_EXTENSION = ".ser";

    public FileManager(@Value("${discodeit.repository.file-directory}") String fileDirectory) {
        this.BASE_DIR = System.getProperty("user.dir") + fileDirectory;
    }

    public <T extends Serializable> T writeToFile(SubDirectory subDirectory, T object, UUID id) {
        String fileName = id.toString() + DATA_EXTENSION;
        String filePath = BASE_DIR + subDirectory.getDirectory() + "\\" + fileName;

        File directory = new File(BASE_DIR + subDirectory.getDirectory());
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    public <T> List<T> readFromFileAll(SubDirectory subDirectory, Class<T> type) {
        List<T> list = new ArrayList<>();

        File directory = new File(BASE_DIR + subDirectory.getDirectory() + "\\");

        if (!directory.exists() || !directory.isDirectory()) {
            return list;
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(DATA_EXTENSION));
        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    T object = type.cast(ois.readObject());
                    list.add(object);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public <T> Optional<T> readFromFileById(SubDirectory subDirectory, UUID id, Class<T> fileType) {
        String directoryPath = BASE_DIR + subDirectory.getDirectory() + "\\";
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            return Optional.empty();
        }

        String fileName = id.toString() + DATA_EXTENSION;
        File file = new File(directoryPath + fileName);

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                T object = fileType.cast(ois.readObject());
                return Optional.of(object);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public boolean deleteFileById(SubDirectory subDirectory, UUID id) {
        String directoryPath = BASE_DIR + subDirectory.getDirectory() + "\\";
        String fileName = id.toString() + DATA_EXTENSION;
        File file = new File(directoryPath + fileName);
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }
}
