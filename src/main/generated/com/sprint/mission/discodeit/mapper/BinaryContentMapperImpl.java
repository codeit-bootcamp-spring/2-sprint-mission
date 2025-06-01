package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.binarycontent.FindBinaryContentResponseDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.CreateBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T17:24:59+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class BinaryContentMapperImpl implements BinaryContentMapper {

    @Override
    public FindBinaryContentResponseDTO toFindBinaryContentResponseDTO(FindBinaryContentResult findBinaryContentResult) {
        if ( findBinaryContentResult == null ) {
            return null;
        }

        UUID id = null;
        String filename = null;
        long size = 0L;
        String contentType = null;

        id = findBinaryContentResult.id();
        filename = findBinaryContentResult.filename();
        size = findBinaryContentResult.size();
        contentType = findBinaryContentResult.contentType();

        FindBinaryContentResponseDTO findBinaryContentResponseDTO = new FindBinaryContentResponseDTO( id, filename, size, contentType );

        return findBinaryContentResponseDTO;
    }

    @Override
    public CreateBinaryContentResult toCreateBinaryContentResult(BinaryContent binaryContent) {
        if ( binaryContent == null ) {
            return null;
        }

        UUID id = null;
        String filename = null;
        long size = 0L;
        String contentType = null;

        id = binaryContent.getId();
        filename = binaryContent.getFilename();
        size = binaryContent.getSize();
        contentType = binaryContent.getContentType();

        CreateBinaryContentResult createBinaryContentResult = new CreateBinaryContentResult( id, filename, size, contentType );

        return createBinaryContentResult;
    }

    @Override
    public FindBinaryContentResult toFindBinaryContentResult(BinaryContent binaryContent) {
        if ( binaryContent == null ) {
            return null;
        }

        UUID id = null;
        String filename = null;
        long size = 0L;
        String contentType = null;

        id = binaryContent.getId();
        filename = binaryContent.getFilename();
        size = binaryContent.getSize();
        contentType = binaryContent.getContentType();

        FindBinaryContentResult findBinaryContentResult = new FindBinaryContentResult( id, filename, size, contentType );

        return findBinaryContentResult;
    }
}
