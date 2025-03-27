package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(value="/{binaryContentId}",method = RequestMethod.GET)
    public BinaryContent getBinaryContent(@PathVariable UUID binaryContentId) {
        return binaryContentService.find(binaryContentId);
    }
    @RequestMapping(method=RequestMethod.GET)
    public List<BinaryContent> getBinaryContents(List<UUID> binaryContentIds) {
        return binaryContentService.findAllByIdIn(binaryContentIds);
    }

}
