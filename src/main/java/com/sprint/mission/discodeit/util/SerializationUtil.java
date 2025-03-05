package com.sprint.mission.discodeit.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SerializationUtil {

    // 디렉토리가 존재하지 않으면 생성
    public static void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    // 직렬화할 땐 하나씩 받아서 파일에 추가
    public static <T> void serialization(Path filePath, T t) {
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(t);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 역직렬화할 땐 리스트로 읽어오기
    public static <T> List<T> reverseSerialization(Path directory)  {
        if (Files.exists(directory)) {
            try {
                List<T> list = Files.list(directory)
                        .map(path -> {
                            try (
                                    FileInputStream fis = new FileInputStream(path.toFile());
                                    ObjectInputStream ois = new ObjectInputStream(fis)
                                    ) {
                                Object data = ois.readObject();
                                return (T) data;
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());
                return list;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    // 파일 하나만 역직렬화
    public static <T> T reverseOneSerialization(Path directory, UUID id) {
        if (!Files.exists(directory)) {
            return null;
        }
        try {
            // ID와 일치하는 파일 찾기
            Path filePath = Files.list(directory)
                    .filter(path -> path.getFileName().toString().equals(id + ".ser")) //
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("UUID값에 해당하는 데이터가 존재하지 않습니다.")); //

            // 파일이 존재하면 역직렬화 수행
            try (FileInputStream fis = new FileInputStream(filePath.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                return (T) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 역직렬화 실패: " + e.getMessage(), e);
        }
    }


}
