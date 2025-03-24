package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;


    @Override
    public BinaryContent create(BinaryContentCreateDto binaryContentCreateDto) {
        Path proFilePath = binaryContentCreateDto.path();
        if (proFilePath != null && !proFilePath.toString().trim().isEmpty()) {
            try {
                byte[] bytes = Files.readAllBytes(proFilePath);
                BinaryContent binaryContent = new BinaryContent(bytes);
                BinaryContent createdContent = binaryContentRepository.save(binaryContent);
                System.out.println(createdContent);
                return createdContent;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    @Override
    public BinaryContent getUser(BinaryContentFindDto binaryContentFindDto) {
        return binaryContentRepository.load().stream()
                .filter(m -> m.getId().equals(binaryContentFindDto.Id()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Profile not found."));
    }


    @Override
    public List<BinaryContent> getAllUser() {
        List<BinaryContent> binaryContentList = binaryContentRepository.load();
        if (binaryContentList.isEmpty()) {
            throw new NoSuchElementException("Profile not found.");
        }
        return binaryContentList;
    }


    @Override
    public BinaryContent updateByUserId(BinaryContentUpdateDto binaryContentUpdateDto) {
        BinaryContent matchingBinaryContent = binaryContentRepository.load().stream()
                .filter(m -> m.getId().equals(binaryContentUpdateDto.Id()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Profile not found."));

        Path proFilePath = binaryContentUpdateDto.newProfilePath();
        try {
            byte[] imageToBytes = Files.readAllBytes(proFilePath);
            matchingBinaryContent.updateBinaryContent(imageToBytes);
        } catch (IOException e) {
            throw new NoSuchElementException(e);
        }
        return binaryContentRepository.save(matchingBinaryContent);
    }



    @Override
    public void delete(BinaryContentDeleteDto binaryContentDeleteDto) {
        BinaryContent matchingBinaryContent = binaryContentRepository.load().stream()
                .filter(m -> m.getId().equals(binaryContentDeleteDto.Id()))
                .findAny()
                .orElse(null);
        binaryContentRepository.remove(matchingBinaryContent);
    }
}
