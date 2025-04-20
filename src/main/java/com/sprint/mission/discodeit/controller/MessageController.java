package com.sprint.mission.discodeit.controller;

// import com.sprint.mission.discodeit.controller.api.MessageApi; // MessageApi 인터페이스는 변경된 메소드 시그니처 반영 필요

import com.sprint.mission.discodeit.dto.data.MessageDto; // MessageDto 임포트 (반환 타입 변경)
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest; // 첨부파일 생성 요청 DTO
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest; // 메시지 생성 요청 DTO
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest; // 메시지 업데이트 요청 DTO
import com.sprint.mission.discodeit.dto.response.PageResponse; // 페이지네이션 응답 DTO 임포트
// import com.sprint.mission.discodeit.entity.Message; // Entity 직접 반환 대신 DTO 사용
import com.sprint.mission.discodeit.mapper.PageResponseMapper; // PageResponseMapper 임포트
import com.sprint.mission.discodeit.service.MessageService; // MessageService 인터페이스
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable; // Pageable 임포트 (findAllByChannelId 파라미터 추가)
import org.springframework.data.domain.Slice; // Slice 임포트 (서비스 반환 타입)
import org.springframework.data.domain.Sort; // 정렬 설정
import org.springframework.data.web.PageableDefault; // Pageable 기본값 설정
import org.springframework.http.HttpStatus; // HTTP 상태 코드
import org.springframework.http.MediaType; // 미디어 타입
import org.springframework.http.ResponseEntity; // 응답 객체
import org.springframework.web.bind.annotation.*; // 각종 매핑 어노테이션
import org.springframework.web.multipart.MultipartFile; // MultipartFile 처리

import java.io.IOException; // I/O 예외
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor // final 필드 의존성 주입
@RestController // REST 컨트롤러
@RequestMapping("/api/messages") // 기본 요청 경로 유지
// public class MessageController implements MessageApi { // MessageApi 인터페이스는 시그니처 변경 필요
public class MessageController { // 인터페이스 구현 선언 제거 (시그니처 불일치 방지)

  private final MessageService messageService; // MessageService 주입
  private final PageResponseMapper pageResponseMapper; // PageResponseMapper 주입

  /**
   * 새로운 메시지를 생성하는 API 엔드포인트. POST /api/messages 기존 로직을 유지하되 서비스의 변경 사항에 맞춰 수정합니다. 첨부파일 데이터 처리는
   * BinaryContentService에 위임됩니다.
   *
   * @param messageCreateRequest 메시지 생성 요청 DTO (메시지 내용, 채널/작성자 ID 등).
   * @param attachments          첨부파일 목록 (MultipartFile).
   * @return 생성된 메시지 DTO와 HTTP 201 Created 상태.
   */
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 기존 소비 타입 유지
  public ResponseEntity<MessageDto> create( // 반환 타입 Message -> MessageDto
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              // 기존 byte[] 변환 대신 MultipartFile 자체를 DTO에 담아 전달
              // BinaryContentCreateRequest DTO에 MultipartFile 필드가 있다고 가정
              return new BinaryContentCreateRequest(
                  file.getOriginalFilename(),
                  file.getContentType(),
                  file // <<< MultipartFile 객체 자체를 전달
              );
              // catch (IOException e) 블록은 이제 필요 없음 (getBytes() 사용 안 함)
            })
            .toList()) // Java 16+
        .orElse(new ArrayList<>());

    try {
      // 서비스에게 메시지 생성 및 첨부파일 처리 로직 위임
      // 서비스 create 메소드는 MessageDto를 반환합니다.
      MessageDto createdMessageDto = messageService.create(messageCreateRequest,
          attachmentRequests); // 서비스 호출

      // 생성 성공 시 201 Created 응답 반환
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(createdMessageDto); // 생성된 MessageDto 반환

    } catch (IllegalArgumentException e) {
      // 채널/작성자 없음 등 서비스에서 발생한 IllegalArgumentException 처리
      // logger.error("Bad request during message creation", e);
      return ResponseEntity.badRequest().body(null); // 400 Bad Request
    } catch (RuntimeException e) {
      // 서비스에서 첨부파일 저장 오류 등으로 발생시킨 RuntimeException 처리
      // logger.error("Internal server error during message creation", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null); // 500 Internal Server Error
    } catch (Exception e) {
      // 그 외 예상치 못한 오류
      // logger.error("Unexpected error during message creation", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null); // 500 Internal Server Error
    }
  }

  /**
   * 특정 ID의 메시지 내용을 업데이트하는 API 엔드포인트. PATCH /api/messages/{messageId} (서비스 메소드 시그니처 및 반환 타입에 맞춰 수정)
   *
   * @param messageId 업데이트할 메시지의 ID (UUID).
   * @param request   업데이트 요청 DTO.
   * @return 업데이트된 메시지 DTO. 메시지가 없을 경우 404 Not Found.
   */
  @PatchMapping(path = "{messageId}") // 기존 경로 유지
  public ResponseEntity<MessageDto> update( // 반환 타입 Message -> MessageDto
      @PathVariable("messageId") UUID messageId, // 경로 변수 이름 유지
      @RequestBody MessageUpdateRequest request) { // 요청 본문 타입 유지
    // 서비스에게 메시지 업데이트 로직 위임
    // 서비스 update 메소드는 MessageDto를 반환합니다.
    MessageDto updatedMessageDto = messageService.update(messageId, request); // 서비스 호출

    // 업데이트 성공 시 200 OK와 함께 업데이트된 메시지 DTO 반환
    return ResponseEntity
        .status(HttpStatus.OK) // 기존 상태 코드 유지
        .body(updatedMessageDto); // 업데이트된 MessageDto 반환


  }

  /**
   * 특정 ID의 메시지를 삭제하는 API 엔드포인트. DELETE /api/messages/{messageId} (서비스 메소드 시그니처 및 로직에 맞춰 수정)
   *
   * @param messageId 삭제할 메시지의 ID (UUID).
   * @return 삭제 성공 시 204 No Content. 메시지가 없을 경우 404 Not Found.
   */
  @DeleteMapping(path = "{messageId}") // 기존 경로 유지
  public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) { // 경로 변수 이름 유지
    // 서비스에게 메시지 삭제 로직 위임
    // 서비스 delete 메소드는 void를 반환합니다.
    messageService.delete(messageId); // 서비스 호출

    // 삭제 성공 시 204 No Content 반환
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT) // 기존 상태 코드 유지
        .build(); // 본문 없음
  }

  /**
   * 특정 채널의 메시지 목록을 페이지네이션 및 정렬하여 조회하는 API 엔드포인트. GET
   * /api/messages?channelId={channelId}&page={page}&size={size}&sort={property,direction} 기존
   * findAllByChannelId 메소드에 페이징을 추가하고 반환 타입을 변경합니다. 기본값: size=50, sort=createdAt,desc (최신 메시지 순) 전체
   * 메시지 개수 정보는 포함하지 않습니다 (Slice 사용).
   *
   * @param channelId 조회할 채널의 ID (UUID).
   * @param pageable  클라이언트로부터 받은 페이지네이션 및 정렬 정보.
   * @return 페이지네이션된 특정 채널의 메시지 목록 (PageResponse<MessageDto>).
   */
  @GetMapping // 기존 경로 유지 (/api/messages)
  public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId( // 메소드 이름 유지, 반환 타입 변경
      @RequestParam("channelId") UUID channelId, // 요청 파라미터 이름 및 타입 유지
      // <<< 페이지네이션을 위해 Pageable 인자 추가 및 기본값 설정
      @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

    // 1. 서비스의 'readAllByChannelId' 메소드 호출 (이제 Slice<MessageDto>와 Pageable을 받음)
    Slice<MessageDto> messagesSlice = messageService.readAllByChannelId(channelId,
        pageable); // 서비스 호출

    // 2. 서비스로부터 받은 MessageDto Slice를 공통 PageResponse DTO로 변환합니다.
    // PageResponseMapper를 사용하여 변환합니다.
    PageResponse<MessageDto> response = pageResponseMapper.fromSlice(messagesSlice); // 매퍼 사용

    // 3. 변환된 PageResponse DTO를 응답 본문에 담아 200 OK 응답을 반환합니다.
    // 메시지가 없더라도 빈 Slice -> 빈 PageResponse로 변환되어 정상적으로 반환됩니다.
    return ResponseEntity
        .status(HttpStatus.OK) // 기존 상태 코드 유지
        .body(response); // PageResponse DTO 반환

  }
}
