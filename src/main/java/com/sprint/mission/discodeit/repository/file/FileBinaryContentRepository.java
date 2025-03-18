package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {

    private static final Path DIRECTORY_PATH = Paths.get(System.getProperty("user.dir"), "data",
            "fileBinaryContents");

    public FileBinaryContentRepository() {
        init();
    }

    private void init() {
        try {
            Files.createDirectories(DIRECTORY_PATH);
        } catch (IOException e) {
            throw new RuntimeException("BinaryContent 디렉토리 생성을 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public BinaryContent save(BinaryContent fileBinaryContent) {
        Path filePath = getFilePath(fileBinaryContent.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(fileBinaryContent);
            return fileBinaryContent;
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " BinaryContent 저장을 실패했습니다: " + e.getMessage());
        }
    }

    private Path getFilePath(UUID fileBinaryContentId) {
        return DIRECTORY_PATH.resolve(fileBinaryContentId + ".ser");
    }

    @Override
    public List<BinaryContent> findAll() {
        try (Stream<Path> paths = Files.list(DIRECTORY_PATH)) {
            return paths.map(this::readUserFromFile).toList();
        } catch (IOException e) {
            throw new RuntimeException("BinaryContents 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    private BinaryContent readUserFromFile(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (BinaryContent) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(filePath.getFileName() + " BinaryContent 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public BinaryContent findById(UUID fileBinaryContentId) {
        Path filePath = getFilePath(fileBinaryContentId);

        if (!Files.exists(filePath)) {
            throw new RuntimeException("BinaryContent ID " + fileBinaryContentId + "에 해당하는 파일을 찾을 수 없습니다.");
        }

        return readUserFromFile(filePath);
    }

    @Override
    public void delete(UUID binaryContentId) {
        Path filePath = getFilePath(binaryContentId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " BinaryContentId 삭제를 실패했습니다: " + e.getMessage());
        }
    }
}
