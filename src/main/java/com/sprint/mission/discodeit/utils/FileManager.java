package com.sprint.mission.discodeit.utils;

import com.sprint.mission.discodeit.constant.ImageType;
import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.dto.SaveFileDto;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
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

    public SaveFileDto writeToFile(SubDirectory subDirectory, byte[] fileData, String originalFileName) {
        String extension = getFileExtension(originalFileName);
        if (subDirectory.equals(SubDirectory.PROFILE)&&!isValidImageExtension(extension)) {
            throw new IllegalArgumentException("[실패] 허용되지 않은 파일 형식");
        }

        String fileName = UUID.randomUUID().toString() + "." + extension;
        String filePath = BASE_DIR + subDirectory.getDirectory() + "\\" + fileName;

        File directory = new File(BASE_DIR + subDirectory.getDirectory());
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileData);
            fos.flush();
        } catch (IOException e) {
            throw new RuntimeException("[실패] 파일 저장 실패", e);
        }
        return new SaveFileDto(filePath, fileName);
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1) {
            throw new IllegalArgumentException("[실패] 확장자 오류");
        }
        return fileName.substring(lastIndex + 1).toLowerCase();
    }

    private boolean isValidImageExtension(String extension) {
        return Arrays.stream(ImageType.values())
                .anyMatch(type -> type.name().equalsIgnoreCase(extension));
    }
}
