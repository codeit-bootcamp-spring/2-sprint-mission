package com.sprint.mission.discodeit.controller.basic;


import com.sprint.mission.discodeit.controller.BinaryContentRequestHandler;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.basic.MessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class BasicMessageController {

    private final MessageService messageService;
    private final BinaryContentRequestHandler binaryContentRequestHandler;
    Logger logger = LoggerFactory.getLogger(BasicMessageController.class);

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Message> create(
            @RequestPart("message") MessageCreateRequest request,
            @RequestPart(value = "binaryContent", required = false) Optional<List<MultipartFile>> binaryContents) {

        List<BinaryContentCreateRequest> binaryContentCreateRequests = binaryContents
                .map(contents -> contents.stream()
                        .map(binaryContentRequestHandler::handle)
                        .flatMap(Optional::stream)
                        .collect(Collectors.toList()))
                .orElse(List.of());

        Message response = messageService.create(request, binaryContentCreateRequests);
        logger.info("Successfully created message: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{messageId}/update")
    public ResponseEntity<Message> update(@PathVariable String messageId,@RequestBody MessageUpdateRequest request){
        Message response = messageService.update(UUID.fromString(messageId), request);
        logger.info("Successfully updated message: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{messageId}/delete")
    public ResponseEntity<String> delete(@PathVariable String messageId){
        messageService.delete(UUID.fromString(messageId));
        logger.info("Successfully deleted message: {}", messageId);
        return ResponseEntity.ok("Successfully deleted message: " + messageId);
    }

    @GetMapping("/{channelId}/findAll")
    public ResponseEntity<List<Message>> findAll(@PathVariable String channelId){
        List<Message> response = messageService.findAllByChannelId(UUID.fromString(channelId));
        logger.info("Successfully found all messages: {}", response);
        return ResponseEntity.ok(response);
    }
}
