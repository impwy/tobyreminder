## 코딩 관례

### Lombok

- `@Setter` 사용 금지 — 상태 변경은 명시적 도메인 메서드(`update()` 등)로만 수행
- `@Getter` 허용
- `@NoArgsConstructor(access = AccessLevel.PROTECTED)` — JPA 전용, 외부 직접 생성 차단
- `@Builder`는 클래스가 아닌 생성자에 적용 — 생성에 필요한 필드만 포함 (id, timestamps 제외)

### 문서 (.md)

- `spec.md` — 제품 스펙 (기능, API, 데이터 모델, UI/UX)
- `plan.md` — Phase별 개발 계획 + 기술 상세
- `tasks.md` — 체크박스 기반 세부 작업 리스트
- 구현 전 spec/plan 참고, 완료 시 tasks.md 체크

### Spring Boot 4 참고

- `@DataJpaTest` 패키지: `org.springframework.boot.data.jpa.test.autoconfigure`
- 테스트 의존성: `spring-boot-starter-data-jpa-test`, `spring-boot-starter-webmvc-test`
