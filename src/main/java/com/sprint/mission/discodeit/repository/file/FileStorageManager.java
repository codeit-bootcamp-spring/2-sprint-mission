package com.sprint.mission.discodeit.repository.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FileStorageManager {

    @Value("${discodeit.repository.file-directory}")
    private String filePath;

    public <K, V> Map<K, V> loadFile(String fileName) {
        try {
            File file = new File(filePath + fileName);

            // 디렉토리가 없으면 자동 생성
            File dir = file.getParentFile();
            if (dir != null && !dir.exists()) {
                dir.mkdirs();
            }

            System.out.println("🔎 저장 시도 경로: " + filePath + fileName);

            // 파일이 없으면 새로 만들고 빈 Map 리턴
            if (!file.exists()) {
                System.out.println("📄 파일이 존재하지 않아 새로 생성합니다: " + file.getPath());
                file.createNewFile(); // 빈 파일 생성
                return new ConcurrentHashMap<>();
            }

            // 파일이 존재하면 불러오기 시도
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (ConcurrentHashMap<K, V>) ois.readObject();
            }

        } catch (EOFException e) {
            System.out.println("⚠ 파일이 비어 있습니다. 빈 데이터로 유지합니다.");
            return new ConcurrentHashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드 중 오류 발생", e);
        }
    }


    public <K, V> void saveFile(String fileName, Map<K, V> data) {
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs(); // 경로 자동 생성!
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath + fileName))) {
                oos.writeObject(data);
            }

        } catch (IOException e) {
            throw new RuntimeException("데이터 저장 중 오류 발생", e);
        }
    }
}
