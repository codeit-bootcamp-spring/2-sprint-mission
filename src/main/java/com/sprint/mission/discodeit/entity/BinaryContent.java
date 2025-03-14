package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.constant.SubDirectory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class BinaryContent extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private SubDirectory subDirectory;
}
