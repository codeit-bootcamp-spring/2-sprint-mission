package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Api data 전송 객체")
public class ApiDataResponse<T> {

  private boolean success;
  private String message;
  private T data;

  public static <T> ApiDataResponse<T> success(T data) {
    return new ApiDataResponse<>(true, "요청이 성공적으로 처리되었습니다.", data);
  }

  public static ApiDataResponse<Void> success() {
    return new ApiDataResponse<>(true, "요청이 성공적으로 처리되었습니다.", null);
  }

  public static ApiDataResponse<?> error(String message) {
    return new ApiDataResponse<>(false, message, null);
  }
}
