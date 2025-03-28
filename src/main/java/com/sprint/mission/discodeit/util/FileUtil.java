package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentFileResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
    private static final String EXTENSION = ".ser";

    public static void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Path resolvePath(Path directory, UUID id) {
        return directory.resolve(id.toString() + EXTENSION);
    }

    public static String saveFile(Path directory, String fileName, MultipartFile file) {
        Path filePath = directory.resolve(fileName);
        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + e);
        }
        return filePath.toString();
    }

    public static <T> T save(Path directory, T object, UUID id) {
        Path filePath = resolvePath(directory, id);
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + e);
        }
        return object;
    }

    public static BinaryContentFileResponse findFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("파일이 존재하지 않음: " + filePath);
        }

        try (InputStream inputStream = new FileInputStream(file)) {
            String contentType = Files.probeContentType(file.toPath());
            byte[] fileBytes = FileCopyUtils.copyToByteArray(inputStream);
            return new BinaryContentFileResponse(contentType, fileBytes);
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는 중 오류 발생: " + filePath, e);
        }
    }

    public static <T> Optional<T> findById(Path directory, UUID id, Class<T> clazz) {
        Path filePath = resolvePath(directory, id);
        if (!Files.exists(filePath)) {
            return Optional.empty();
        }
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            Object object = ois.readObject();
            if (clazz.isInstance(object)) {
                return Optional.of(clazz.cast(object));
            }
            return Optional.empty();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("id에 해당하는 파일 조회 중 오류 발생: " + e);
        }
    }

    public static <T> List<T> findAll(Path directory, Class<T> clazz) {
        List<T> objectList = new ArrayList<>();
        try {
            Files.list(directory)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .forEach(path -> {
                        UUID id = UUID.fromString(path.getFileName().toString().replace(EXTENSION, ""));
                        Optional<T> objectNullable = findById(directory, id, clazz);
                        objectNullable.ifPresent(objectList::add);
                    });
        } catch (IOException e) {
            throw new RuntimeException("모든 파일 조회 중 오류 발생: " + e);
        }
        return objectList;
    }

    public static boolean existsById(Path directory, UUID id) {
        Path filePath = resolvePath(directory, id);
        return Files.exists(filePath);
    }

    public static void deleteFile(Path filePath) {
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 중 오류 발생: ", e);
        }
    }

    public static void delete(Path directory, UUID id) {
        Path filePath = resolvePath(directory, id);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 중 오류 발생: ", e);
        }
    }

    public static boolean isAllowedExtension(String originalFilename) {
        String[] allowedExtensions = {".png", ".jpg", ".jpeg", ".gif", ".pdf"};
        if (originalFilename == null) {
            return false;
        }

        String lowercaseName = originalFilename.toLowerCase();
        for (String ext : allowedExtensions) {
            if (lowercaseName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }


}