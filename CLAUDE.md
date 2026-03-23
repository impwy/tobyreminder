## 코딩 관례

### Lombok

- `@Setter` 사용 금지 — 상태 변경은 명시적 도메인 메서드(`update()` 등)로만 수행
- `@Getter` 허용
- `@NoArgsConstructor(access = AccessLevel.PROTECTED)` — JPA 전용, 외부 직접 생성 차단
- `@Builder`는 클래스가 아닌 생성자에 적용 — 생성에 필요한 필드만 포함 (id, timestamps 제외)

### 도메인 엔티티

- 패키지: `domain` (`entity` 아님)

### 서비스 계층

- 인터페이스는 `service/ports/in` 패키지에 정의
- 구현 클래스는 `service` 패키지에 위치, `Default` 접두사 사용 (예: `DefaultReminderListService`)

### 테스트

- 기능 추가/수정 시 반드시 테스트를 함께 작성
- 도메인 엔티티 테스트: 순수 단위 테스트 (JPA, Spring Context 의존 금지)
- 서비스 테스트: `@SpringBootTest` + `@Transactional` 사용 (Mock 금지, 실제 DB 통합 테스트)
- JUnit 5 + AssertJ
- `@DisplayName`으로 테스트 의도를 한글로 명시

### 문서 (.md)

- `spec.md` — 제품 스펙 (기능, API, 데이터 모델, UI/UX)
- `plan.md` — Phase별 개발 계획 + 기술 상세
- `tasks.md` — 체크박스 기반 세부 작업 리스트
- 구현 전 spec/plan 참고, 완료 시 tasks.md 체크

### Spring Boot 4 참고

- `@DataJpaTest` 패키지: `org.springframework.boot.data.jpa.test.autoconfigure`
- `@AutoConfigureMockMvc` 패키지: `org.springframework.boot.webmvc.test.autoconfigure`
- `ObjectMapper` 패키지: `tools.jackson.databind` (`com.fasterxml.jackson` 아님)
- 테스트 의존성: `spring-boot-starter-data-jpa-test`, `spring-boot-starter-webmvc-test`
