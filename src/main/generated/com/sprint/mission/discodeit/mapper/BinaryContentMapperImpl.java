package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-20T19:10:10+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (JetBrains s.r.o.)"
)
@Component
public class BinaryContentMapperImpl implements BinaryContentMapper {

    @Override
    public BinaryContentDto toDto(BinaryContent binaryContent) {
        if ( binaryContent == null ) {
            return null;
        }

        String fileName = null;
        String contentType = null;
        UUID id = null;
        Long size = null;

        fileName = binaryContent.getFileName();
        contentType = binaryContent.getContentType();
        id = binaryContent.getId();
        size = binaryContent.getSize();

        BinaryContentDto binaryContentDto = new BinaryContentDto( id, fileName, size, contentType );

        return binaryContentDto;
    }
}
