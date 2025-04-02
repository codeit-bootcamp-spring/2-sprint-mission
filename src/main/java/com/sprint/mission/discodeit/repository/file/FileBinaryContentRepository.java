package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(
        name = "discordit.repository.type",
        havingValue = "file",
        matchIfMissing = true)
public class FileBinaryContentRepository implements BinaryContentRepository {

    private static final String fileName = "binaryContent.dat";
    private static Map<UUID, BinaryContent> binaryContentMap = new ConcurrentHashMap<>();
    private final FileStorageManager fileStorageManager;
    @Value("${discodeit.repository.file-directory}")
    private String filePath;

    public FileBinaryContentRepository(FileStorageManager fileStorageManager) {
        this.fileStorageManager = fileStorageManager;
        binaryContentMap = fileStorageManager.loadFile(fileName);
    }

    @Override
    public void save() {
        fileStorageManager.saveFile(fileName, binaryContentMap);
    }

    @Override
    public void addBinaryContent(BinaryContent content) {
        binaryContentMap.put(content.getId(), content);
        try {
            if (content.getContentType().startsWith("image/")) {
                saveToFile(content); // 여기서 Files.write()
            }
        } catch (IOException e) {
            e.printStackTrace(); // 혹은 로그로 대체
        }

        save();
    }

    @Override
    public BinaryContent findBinaryContentById(UUID id) {
        return binaryContentMap.get(id);
    }

    @Override
    public List<BinaryContent> findAllBinaryContents() {
        return new ArrayList<>(binaryContentMap.values());
    }

    @Override
    public void deleteBinaryContentById(UUID contentId) {
        binaryContentMap.remove(contentId);
        save();
    }

    @Override
    public boolean existsBinaryContent(UUID contentId) {
        return binaryContentMap.containsKey(contentId);
    }

    private void saveToFile(BinaryContent content) throws IOException {

        String ext = content.getFileName().substring(content.getFileName().lastIndexOf("."));
        String dir = filePath + "uploads/";
        Files.createDirectories(Paths.get(dir));
        Path path = Paths.get(dir + content.getId() + ext);
        Files.write(path, content.getBytes());
    }
}
