package com.sprint.mission.discodeit.mapping;

import com.sprint.mission.discodeit.config.CentralMapperConfig;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(config = CentralMapperConfig.class)
public interface FileMapping {

  @Mapping(target = "CompositeIdentifier.id", source = "id")
  @Mapping(target = "CompositeIdentifier.ownerId", source = "ownerId")
  @Mapping(target = "metadata.ownerType", source = "ownerType")
  @Mapping(target = "metadata.size", source = "size")
  @Mapping(target = "metadata.fileName", source = "fileName")
  @Mapping(target = "metadata.contentType", source = "contentType")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  BinaryContentDto.Summary binaryContentToSummary(BinaryContent binaryContent);

  @Mapping(target = "fileName", source = "binaryContent.fileName")
  @Mapping(target = "message", expression = "java(message)")
  BinaryContentDto.DeleteResponse binaryContentToDeleteResponse(BinaryContent binaryContent,
      @Context String message);
}
