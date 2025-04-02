package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.binarycontent.FindBinaryContentResponseDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-02T10:45:01+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class BinaryContentMapperImpl implements BinaryContentMapper {

    @Override
    public FindBinaryContentResponseDTO toBinaryContentResponseDTO(BinaryContentDTO binaryContentDTO) {
        if ( binaryContentDTO == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        String filename = null;
        long size = 0L;
        String contentType = null;
        byte[] bytes = null;

        id = binaryContentDTO.id();
        createdAt = binaryContentDTO.createdAt();
        filename = binaryContentDTO.filename();
        size = binaryContentDTO.size();
        contentType = binaryContentDTO.contentType();
        byte[] bytes1 = binaryContentDTO.bytes();
        if ( bytes1 != null ) {
            bytes = Arrays.copyOf( bytes1, bytes1.length );
        }

        FindBinaryContentResponseDTO findBinaryContentResponseDTO = new FindBinaryContentResponseDTO( id, createdAt, filename, size, contentType, bytes );

        return findBinaryContentResponseDTO;
    }

    @Override
    public BinaryContentDTO toBinaryContentDTO(BinaryContent binaryContent) {
        if ( binaryContent == null ) {
            return null;
        }

        UUID id = null;
        Instant createdAt = null;
        String filename = null;
        long size = 0L;
        String contentType = null;
        byte[] bytes = null;

        id = binaryContent.getId();
        createdAt = binaryContent.getCreatedAt();
        filename = binaryContent.getFilename();
        size = binaryContent.getSize();
        contentType = binaryContent.getContentType();
        byte[] bytes1 = binaryContent.getBytes();
        if ( bytes1 != null ) {
            bytes = Arrays.copyOf( bytes1, bytes1.length );
        }

        BinaryContentDTO binaryContentDTO = new BinaryContentDTO( id, createdAt, filename, size, contentType, bytes );

        return binaryContentDTO;
    }
}
