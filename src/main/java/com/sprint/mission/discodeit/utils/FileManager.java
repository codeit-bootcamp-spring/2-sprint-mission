package com.sprint.mission.discodeit.utils;

import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.entity.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileManager {

    private final String BASE_DIR = System.getProperty("user.dir") + "\\src\\main\\resources\\dir\\";

    public <T extends Serializable> void writeToFile(SubDirectory subDirectory, T object, UUID id) {
        String fileName = id.toString() + ".ser";
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
    }

    public <T> List<T> readFromFileAll(SubDirectory subDirectory, Class<T> type) {
        List<T> list = new ArrayList<>();

        File directory = new File(BASE_DIR + subDirectory.getDirectory() + "\\");

        if (!directory.exists() || !directory.isDirectory()) {
            return list;
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".ser"));
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

    public List<Message> readFromMessageOfChannel(UUID id) {
        List<Message> list = new ArrayList<>();

        File directory = new File(BASE_DIR + SubDirectory.MESSAGE + "\\");

        if (!directory.exists() || !directory.isDirectory()) {
            return list;
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".ser"));
        if (files != null) {
            for (File file : files) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    Message message = (Message) ois.readObject();
                    if(message.getChannelUUID() == id) {
                        list.add(message);
                    }
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

        String fileName = id.toString() + ".ser";
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
        String fileName = id.toString() + ".ser";
        File file = new File(directoryPath + fileName);
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }
}
