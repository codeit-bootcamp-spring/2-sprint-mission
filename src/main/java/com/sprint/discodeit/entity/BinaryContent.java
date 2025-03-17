package com.sprint.discodeit.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BinaryContent {

    private UUID id;
    private String filename;
    private String imgUrl;
    private Instant creatAt;

}
