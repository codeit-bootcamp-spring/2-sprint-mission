package com.sprint.mission.discodeit.mapping;

import com.sprint.mission.discodeit.config.CentralMapperConfig;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(config = CentralMapperConfig.class)
public interface FileMapping {

  @Mapping(target = "compositeIdentifier.id", source = "id")
  @Mapping(target = "compositeIdentifier.ownerId", source = "ownerId")
  @Mapping(target = "fileMetadata.ownerType", source = "ownerType")
  @Mapping(target = "fileMetadata.size", source = "size")
  @Mapping(target = "fileMetadata.fileName", source = "fileName")
  @Mapping(target = "fileMetadata.contentType", source = "contentType")
  @Mapping(target = "createdAt", source = "createdAt")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  BinaryContentDto.Summary binaryContentToSummary(BinaryContent binaryContent);

}
