package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
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
    public BinaryContent create(BinaryContentCreateDto request) {
        String fileName = request.fileName();
        String contentType = request.contentType();
        byte[] bytes = request.bytes();
        BinaryContent binaryContent = new BinaryContent(
                fileName,
                (long) bytes.length,
                contentType,
                bytes
        );
        return binaryContentRepository.save(binaryContent);
    }


    @Override
    public BinaryContent getUser(BinaryContentFindDto binaryContentFindDto) {
        return binaryContentRepository.load().stream()
                .filter(m -> m.getId().equals(binaryContentFindDto.Id()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Profile not found."));
    }


    @Override
    public List<BinaryContent> getAllUser() {
        List<BinaryContent> binaryContentList = binaryContentRepository.load();
        if (binaryContentList.isEmpty()) {
            throw new NotFoundException("Profile not found.");
        }
        return binaryContentList;
    }


    @Override
    public BinaryContent updateByUserId(BinaryContentUpdateDto binaryContentUpdateDto) {
        BinaryContent matchingBinaryContent = binaryContentRepository.load().stream()
                .filter(m -> m.getId().equals(binaryContentUpdateDto.Id()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Profile not found."));

        String fileName = binaryContentUpdateDto.newFileName();
        String contentType = binaryContentUpdateDto.newContentType();
        byte[] bytes = binaryContentUpdateDto.newBytes();
        BinaryContent binaryContent = new BinaryContent(
                fileName,
                (long) bytes.length,
                contentType,
                bytes
        );

        matchingBinaryContent.updateBinaryContent(fileName, (long) bytes.length, contentType, bytes);
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
