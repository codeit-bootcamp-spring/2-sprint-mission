package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository("fileBinaryContentRepository")
public class FileBinaryContentRepositoryImpl implements BinaryContentRepository {
    private String DATA_DIR = "data";
    private String BINARY_DATA_FILE = "binary_content.dat";
    
    private final Map<UUID, BinaryContent> binaryContentMap;
    
    public FileBinaryContentRepositoryImpl() {
        binaryContentMap = loadData();
    }
    
    public FileBinaryContentRepositoryImpl(String dataDir) {
        DATA_DIR = dataDir;
        BINARY_DATA_FILE = "binary_content.dat";
        binaryContentMap = loadData();
    }
    
    @SuppressWarnings("unchecked")
    private Map<UUID, BinaryContent> loadData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, BINARY_DATA_FILE);
        
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<UUID, BinaryContent>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        
        return new HashMap<>();
    }
    
    private synchronized void saveData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, BINARY_DATA_FILE);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(binaryContentMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public boolean register(BinaryContent binaryContent) {
        binaryContentMap.put(binaryContent.getId(), binaryContent);
        saveData();
        return true;
    }

    @Override
    public boolean update(BinaryContent binaryContent) {
        if (binaryContentMap.containsKey(binaryContent.getId())) {
            binaryContentMap.put(binaryContent.getId(), binaryContent);
            saveData();
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(BinaryContent binaryContent) {
        boolean result = binaryContentMap.remove(binaryContent.getId()) != null;
        if (result) {
            saveData();
        }
        return result;
    }

    @Override
    public List<UUID> findAll() {
        return new ArrayList<>(binaryContentMap.keySet());
    }

    @Override
    public List<BinaryContent> findAllByOwnerId(UUID ownerId) {
        return binaryContentMap.values().stream()
                .filter(content -> content.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(UUID id) {
        boolean result = binaryContentMap.remove(id) != null;
        if (result) {
            saveData();
        }
        return result;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(binaryContentMap.get(id));
    }
} 