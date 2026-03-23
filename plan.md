# Toby Reminder - 개발 계획

## 기술 상세

### Backend

| 항목 | 기술 | 비고 |
|---|---|---|
| Framework | Spring Boot 4.0.4 | Java 25, Gradle Kotlin DSL |
| ORM | Spring Data JPA | Hibernate 7 |
| Database | H2 (embedded) | 개발용 인메모리, H2 Console 활성화 |
| Validation | spring-boot-starter-validation | Jakarta Validation |
| Lombok | 1.18.x | @Data, @Builder, @NoArgsConstructor 등 |
| API 형식 | REST JSON | Jackson 자동 직렬화 |
| 패키지 구조 | `toby.ai.tobyreminder` | entity, repository, service, controller, dto |
| Port | 8080 | |

### Frontend

| 항목 | 기술 | 비고 |
|---|---|---|
| Framework | Next.js 16 (App Router) | React 19, TypeScript |
| 스타일링 | Tailwind CSS 4 | Apple 디자인 시스템 커스텀 |
| HTTP 클라이언트 | fetch (Next.js 내장) | API Route 또는 직접 호출 |
| 상태 관리 | React hooks (useState, useContext) | 별도 라이브러리 없이 시작 |
| 아이콘 | Lucide React | SF Symbol과 유사한 라인 아이콘 |
| Port | 3000 | Backend 8080으로 프록시 |

### 프로젝트 디렉토리 구조

```
tobyreminder/
├── src/main/java/toby/ai/tobyreminder/   # Spring Boot backend
├── src/main/resources/
│   └── application.properties
├── frontend/                              # Next.js frontend
│   ├── app/
│   ├── components/
│   └── lib/
├── build.gradle.kts
├── spec.md
└── plan.md
```

---

## Phase 1 — 리마인더 기본 CRUD (Backend)

> 가장 단순한 형태. 리마인더 1개 엔티티만으로 생성/조회/수정/삭제/완료 동작 확인.

### Backend 작업

1. `application.properties` 설정
   - H2 인메모리 DB, H2 Console, JPA ddl-auto, JSON 날짜 포맷
2. `Reminder` Entity 생성
   - 필드: id, title, notes, dueDate, priority(Enum), flagged, completed, completedAt, displayOrder, createdAt, updatedAt
   - `@PrePersist`, `@PreUpdate`로 타임스탬프 자동 처리
3. `ReminderRepository` (JpaRepository)
4. `ReminderService` — CRUD + 완료 토글 로직
5. `ReminderController` — REST endpoints
   - `GET /api/reminders` — 전체 조회
   - `GET /api/reminders/{id}` — 상세 조회
   - `POST /api/reminders` — 생성
   - `PUT /api/reminders/{id}` — 수정
   - `PATCH /api/reminders/{id}/complete` — 완료 토글
   - `DELETE /api/reminders/{id}` — 삭제
6. DTO 분리: `ReminderRequest`, `ReminderResponse`
7. `data.sql`로 샘플 데이터 삽입

### 완료 기준
- H2 Console에서 데이터 확인 가능
- curl 또는 HTTP client로 모든 CRUD 동작 확인

---

## Phase 2 — 리스트 관리 + 리마인더 연관 (Backend)

> 리스트 엔티티 추가. 리마인더가 리스트에 소속되도록 확장.

### Backend 작업

1. `ReminderList` Entity 생성
   - 필드: id, name, color, icon, displayOrder, createdAt, updatedAt
2. `Reminder`에 `@ManyToOne` 관계 추가 (`list` 필드)
3. `ReminderListRepository`, `ReminderListService`, `ReminderListController`
   - `GET /api/lists` — 전체 조회 (각 리스트의 미완료 카운트 포함)
   - `POST /api/lists` — 생성
   - `PUT /api/lists/{id}` — 수정
   - `DELETE /api/lists/{id}` — 삭제
4. `GET /api/reminders`에 필터 파라미터 추가
   - `?listId=`, `?today=true`, `?flagged=true`, `?completed=true`, `?scheduled=true`
5. 스마트 리스트 카운트 API
   - `GET /api/reminders/counts` — today, scheduled, all, flagged, completed 각 카운트
6. 샘플 데이터에 리스트 추가

### 완료 기준
- 리스트별 리마인더 필터링 동작
- 스마트 리스트 카운트 응답 확인

---

## Phase 3 — Frontend 기본 UI (Next.js)

> Next.js 프로젝트 생성. Apple Reminders 스타일 레이아웃과 리마인더 목록 표시.

### Frontend 작업

1. `frontend/` 디렉토리에 Next.js 16 프로젝트 생성 (TypeScript, Tailwind CSS, App Router)
2. `next.config.ts`에 Backend API 프록시 설정 (`rewrites` → `localhost:8080`)
3. `lib/api.ts` — fetch 기반 API 클라이언트
4. 루트 레이아웃 (`app/layout.tsx`)
   - 3컬럼 레이아웃: 사이드바 | 메인 | 상세패널
   - Apple 시스템 폰트, `#F2F2F7` 배경
5. 사이드바 (`components/Sidebar.tsx`)
   - 스마트 리스트 카드 그리드 (Today, Scheduled, All, Flagged, Completed)
   - My Lists 섹션 (색상 불릿 + 리스트명 + 카운트)
6. 메인 리마인더 리스트 뷰
   - 리스트 제목 (색상, 34px Bold)
   - 리마인더 행: 원형 체크박스 + 제목 + 메모 미리보기 + 마감일
   - 완료 체크/해제 동작
   - **+ New Reminder** 인라인 입력
7. Apple 스타일 체크박스 컴포넌트 (리스트 색상 테두리 원형)

### 완료 기준
- 사이드바에서 리스트 선택 → 해당 리마인더 목록 표시
- 리마인더 추가/완료 토글 동작
- Apple Reminders와 유사한 외관

---

## Phase 4 — 리마인더 상세 편집 + 리스트 CRUD (Frontend)

> 리마인더 상세 패널과 리스트 관리 UI 추가.

### Frontend 작업

1. 상세 패널 (`components/ReminderDetail.tsx`)
   - 리마인더 클릭 시 우측 인스펙터 패널 표시
   - 제목/메모 편집, Date picker, Priority 선택, Flag 토글, List 선택
   - Apple 스타일 폼 그룹 (둥근 흰색 카드)
2. 리마인더 삭제 (컨텍스트 메뉴 또는 상세 패널 내 삭제 버튼)
3. 리스트 생성 모달 (`components/ListForm.tsx`)
   - 리스트 이름, 색상 선택 (Apple 색상 팔레트), 아이콘 선택
4. 리스트 수정/삭제
5. 스마트 리스트 뷰 (Today, Scheduled, All, Flagged, Completed 각 페이지)

### 완료 기준
- 리마인더 전체 속성 편집 가능
- 리스트 생성/수정/삭제 동작
- 스마트 리스트 필터링 정상 동작

---

## Phase 5 — 서브태스크 (Full Stack)

> 리마인더 하위에 서브태스크 기능 추가.

### Backend 작업

1. `Subtask` Entity (id, title, completed, displayOrder, reminder FK, timestamps)
2. `SubtaskRepository`, `SubtaskService`, `SubtaskController`
   - `GET /api/reminders/{id}/subtasks`
   - `POST /api/reminders/{id}/subtasks`
   - `PUT /api/subtasks/{id}`
   - `PATCH /api/subtasks/{id}/complete`
   - `DELETE /api/subtasks/{id}`
3. `ReminderResponse`에 subtasks 포함

### Frontend 작업

1. `components/SubtaskItem.tsx` — 서브태스크 행 (들여쓰기, 작은 체크박스)
2. 리마인더 행에서 서브태스크 펼침/접힘
3. 상세 패널에서 서브태스크 추가/편집/삭제

### 완료 기준
- 서브태스크 CRUD 전체 동작
- 리마인더 목록에서 서브태스크 인라인 표시

---

## Phase 6 — 검색 + 인터랙션 + 반응형

> 검색 기능, 애니메이션, 반응형 디자인으로 완성도 향상.

### Backend 작업

1. `GET /api/reminders/search?q={keyword}` — 제목/메모 LIKE 검색

### Frontend 작업

1. 검색바 (`components/SearchBar.tsx`)
   - 사이드바 상단, 실시간 검색 (debounce 300ms)
   - 검색 결과 메인 영역에 표시
2. 인터랙션 애니메이션
   - 체크박스 완료 애니메이션 (원형 채워짐 → 0.5초 후 완료 처리)
   - 리마인더 행 hover 효과
   - 사이드바 선택 하이라이트
   - 상세 패널 슬라이드 트랜지션
3. 반응형 디자인
   - Desktop (>1024px): 3컬럼
   - Tablet (768~1024px): 2컬럼, 상세는 오버레이
   - Mobile (<768px): 1컬럼, 사이드바 햄버거 메뉴
4. 에러 처리 및 로딩 상태 (스켈레톤 UI)

### 완료 기준
- 검색 동작 확인
- 모든 인터랙션 애니메이션 자연스러움
- 모바일/태블릿에서 정상 동작
