package com.sprint.mission.discodeit.swagger;

import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusRequestDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.FindReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.UpdateReadStatusRequestDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.UpdateReadStatusResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;


@Tag(name = "Read-Status-Controller", description = "ReadStatus 관련 API")
public interface ReadStatusApi {

  @Operation(summary = "읽음상태 생성",
      description = "userId와 channelId를 기반으로 읽음상태를 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "읽음상태 생성 성공"),
          @ApiResponse(responseCode = "404", description = "userId, channelId에 해당하는 리소스가 존재하지 않음")
      })
  ResponseEntity<CreateReadStatusResponseDTO> createReadStatus(
      CreateReadStatusRequestDTO createReadStatusRequestDTO);

  @Operation(summary = "읽음상태 수정",
      description = "readStatusId에 해당하는 읽음상태를 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "읽음상태 수정 성공"),
          @ApiResponse(responseCode = "404", description = "readStatusId에 해당하는 ReadStatus가 존재하지 않음")
      })
  ResponseEntity<UpdateReadStatusResponseDTO> updateReadStatus(UUID id,
      UpdateReadStatusRequestDTO request);

  @Operation(summary = "유저의 읽음상태 조회",
      description = "userId에 해당하는 읽음상태들을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저의 읽음상태 조회 성공")
      })
  ResponseEntity<List<FindReadStatusResponseDTO>> getUserReadStatus(UUID userId);

}
