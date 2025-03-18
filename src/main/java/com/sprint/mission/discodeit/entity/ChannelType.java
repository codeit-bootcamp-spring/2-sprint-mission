package com.sprint.mission.discodeit.entity;

/*
enum 타입이란?
enum(열거형)은 서로 연관된 상수들의 집합을 정의하는 특별한 데이터 타입입니다.
즉, 변하지 않는 특정 값들만 가질 수 있도록 제한된 타입입니다.

[enum의 특징]
- 미리 정의된 상수값만 가질 수 있음
- 값이 변하지 않음 (불변성 유지)
- 타입 안정성 제공 (허용된 값만 사용할 수 있음)
- 가독성이 좋아지고, 코드의 안정성이 증가함
 */

public enum ChannelType {
    PUBLIC,
    PRIVATE,
}
