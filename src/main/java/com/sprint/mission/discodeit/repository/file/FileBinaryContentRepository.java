package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path storagePath = Paths.get("binary_content");

    public FileBinaryContentRepository() throws IOException {
        Files.createDirectories(storagePath);
    }

    @Override
    public BinaryContent save(BinaryContent entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        Path filePath = storagePath.resolve(entity.getId().toString());
        try {
            Files.write(filePath, entity.getContent());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save binary content", e);
        }
        return entity;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        Path filePath = storagePath.resolve(id.toString());
        if (!Files.exists(filePath)) {
            return Optional.empty();
        }
        try {
            byte[] data = Files.readAllBytes(filePath);
            return Optional.of(new BinaryContent(id, data, "unknown"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read binary content", e);
        }
    }

    @Override
    public List<BinaryContent> findAllById(List<UUID> ids) {
        List<BinaryContent> contents = new ArrayList<>();
        for (UUID id : ids) {
            findById(id).ifPresent(contents::add);
        }
        return contents;
    }

    @Override
    public void deleteById(UUID id) {
        Path filePath = storagePath.resolve(id.toString());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete binary content", e);
        }
    }

    public List<String> findFileByMessageId(UUID messageId) {
        try {
            return Files.list(storagePath)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(name -> name.startsWith(messageId.toString()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to find files by messageId", e);
        }
    }
}