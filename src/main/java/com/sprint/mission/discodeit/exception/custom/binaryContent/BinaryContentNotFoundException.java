package com.sprint.mission.discodeit.exception.custom.binaryContent;

import java.util.NoSuchElementException;

public class BinaryContentNotFoundException extends NoSuchElementException {
    public BinaryContentNotFoundException(String message) {
        super(message);
    }
}
