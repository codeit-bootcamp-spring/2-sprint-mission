package com.sprint.mission.discodeit.repository.file;

import java.io.*;

public class FileDataManager {
    private final String fileName;

    public FileDataManager(String fileName) {
        this.fileName = fileName;
    }

    public <T> void saveObjectToFile(T data) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("데이터를 저장하는데 실패했습니다.", e);
        }
    }

    public <T> T loadObjectFromFile() {
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (T) ois.readObject();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일 읽기 중 오류 발생: " + e.getMessage());  // 로깅 추가
            throw new RuntimeException("데이터를 불러오는데 실패했습니다.", e);
        }
    }
}
