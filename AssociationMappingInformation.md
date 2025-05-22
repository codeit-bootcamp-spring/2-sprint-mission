# 연관관계 매핑 정보

엔티티 관계 : 비교하는 두 엔티티 명

| 엔티티 관계                  | 다중성   | 방향성                         | 부모-자식 관계                        | 연관관계의 주인   |
|:------------------------|:------|:----------------------------|:--------------------------------|:-----------|
| User : UserStatus       | 1 : 1 | UserStatus ↔ User 양방향       | 부모 : User, 자식: UserStatus       | UserStatus 
| User : ReadStatus       | 1 : N | ReadStatus → User 단방향       | 부모 : User, 자식: ReadStatus       | ReadStatus 
| User : Message          | 1 : N | Message → User 단방향          | 부모 : User, 자식: Message          | Message    
| User : BinaryContent    | 1 : 1 | User → BinaryContent 단방향    | 부모 : User, 자식: BinaryContent    | User       
| Channel : Message       | 1 : N | Message → Channel 단방향       | 부모 : Channel, 자식: Message       | Message    
| Channel : ReadStatus    | 1 : N | ReadStatus → Channel 단방향    | 부모 : Channel, 자식: ReadStatus    | ReadStatus 
| Message : BinaryContent | 1 : N | Message → BinaryContent 단방향 | 부모 : Message, 자식: BinaryContent | Message    
