# Toby Reminder - Tasks

## Phase 1 — 리마인더 기본 CRUD (Backend)

- [ ] 1-1. `application.properties` 설정 (H2 인메모리, H2 Console, JPA ddl-auto, JSON 날짜 포맷)
- [ ] 1-2. `Priority` Enum 생성 (NONE, LOW, MEDIUM, HIGH)
- [ ] 1-3. `Reminder` Entity 생성 (id, title, notes, dueDate, priority, flagged, completed, completedAt, displayOrder, createdAt, updatedAt)
- [ ] 1-4. `ReminderRepository` 생성 (JpaRepository)
- [ ] 1-5. `ReminderRequest` DTO 생성
- [ ] 1-6. `ReminderResponse` DTO 생성
- [ ] 1-7. `ReminderService` 생성 (CRUD + 완료 토글)
- [ ] 1-8. `ReminderController` 생성 (GET, POST, PUT, PATCH, DELETE)
- [ ] 1-9. `data.sql` 샘플 데이터 작성
- [ ] 1-10. API 동작 테스트 확인

## Phase 2 — 리스트 관리 + 리마인더 연관 (Backend)

- [ ] 2-1. `ReminderList` Entity 생성 (id, name, color, icon, displayOrder, createdAt, updatedAt)
- [ ] 2-2. `Reminder`에 `@ManyToOne` ReminderList 관계 추가
- [ ] 2-3. `ReminderListRepository` 생성
- [ ] 2-4. `ReminderListRequest` / `ReminderListResponse` DTO 생성
- [ ] 2-5. `ReminderListService` 생성 (CRUD + 미완료 카운트)
- [ ] 2-6. `ReminderListController` 생성 (GET, POST, PUT, DELETE)
- [ ] 2-7. `ReminderRepository`에 필터 쿼리 메서드 추가 (listId, today, flagged, completed, scheduled)
- [ ] 2-8. `ReminderController`에 필터 파라미터 적용
- [ ] 2-9. 스마트 리스트 카운트 API 추가 (`GET /api/reminders/counts`)
- [ ] 2-10. `data.sql`에 리스트 샘플 데이터 추가
- [ ] 2-11. 필터링 및 카운트 API 동작 테스트

## Phase 3 — Frontend 기본 UI (Next.js)

- [ ] 3-1. `frontend/` 디렉토리에 Next.js 16 프로젝트 생성 (TypeScript, Tailwind CSS, App Router)
- [ ] 3-2. Tailwind CSS에 Apple 시스템 색상 커스텀 설정
- [ ] 3-3. `next.config.ts` Backend API 프록시 설정 (rewrites → localhost:8080)
- [ ] 3-4. `lib/api.ts` fetch 기반 API 클라이언트 작성
- [ ] 3-5. 루트 레이아웃 구현 (3컬럼: 사이드바 | 메인 | 상세패널, Apple 시스템 폰트, #F2F2F7 배경)
- [ ] 3-6. Apple 스타일 원형 체크박스 컴포넌트 구현
- [ ] 3-7. 사이드바 — 스마트 리스트 카드 그리드 (Today, Scheduled, All, Flagged, Completed)
- [ ] 3-8. 사이드바 — My Lists 섹션 (색상 불릿 + 리스트명 + 카운트)
- [ ] 3-9. 메인 영역 — 리스트 제목 표시 (색상, 34px Bold)
- [ ] 3-10. 메인 영역 — 리마인더 행 표시 (체크박스 + 제목 + 메모 미리보기 + 마감일)
- [ ] 3-11. 메인 영역 — 리마인더 완료 체크/해제 동작
- [ ] 3-12. 메인 영역 — "+ New Reminder" 인라인 입력 기능
- [ ] 3-13. 사이드바 리스트 선택 → 해당 리마인더 목록 연동

## Phase 4 — 리마인더 상세 편집 + 리스트 CRUD (Frontend)

- [ ] 4-1. 상세 패널 기본 구조 (리마인더 클릭 시 우측 인스펙터 표시)
- [ ] 4-2. 상세 패널 — 제목/메모 인라인 편집
- [ ] 4-3. 상세 패널 — Date picker (토글 스위치 + 날짜/시간 선택)
- [ ] 4-4. 상세 패널 — Priority 선택 (None/Low/Medium/High)
- [ ] 4-5. 상세 패널 — Flag 토글 스위치
- [ ] 4-6. 상세 패널 — List 선택 (색상 원형 불릿 포함 드롭다운)
- [ ] 4-7. 상세 패널 — Apple 스타일 폼 그룹 (둥근 흰색 카드)
- [ ] 4-8. 리마인더 삭제 기능 (컨텍스트 메뉴 또는 삭제 버튼)
- [ ] 4-9. 리스트 생성 모달 (이름, 색상 팔레트, 아이콘 선택)
- [ ] 4-10. 리스트 수정 기능
- [ ] 4-11. 리스트 삭제 기능
- [ ] 4-12. 스마트 리스트 뷰 — Today 페이지
- [ ] 4-13. 스마트 리스트 뷰 — Scheduled 페이지
- [ ] 4-14. 스마트 리스트 뷰 — All 페이지
- [ ] 4-15. 스마트 리스트 뷰 — Flagged 페이지
- [ ] 4-16. 스마트 리스트 뷰 — Completed 페이지

## Phase 5 — 서브태스크 (Full Stack)

- [ ] 5-1. `Subtask` Entity 생성 (id, title, completed, displayOrder, reminder FK, timestamps)
- [ ] 5-2. `SubtaskRepository` 생성
- [ ] 5-3. `SubtaskRequest` / `SubtaskResponse` DTO 생성
- [ ] 5-4. `SubtaskService` 생성 (CRUD + 완료 토글)
- [ ] 5-5. `SubtaskController` 생성 (GET, POST, PUT, PATCH, DELETE)
- [ ] 5-6. `ReminderResponse`에 subtasks 리스트 포함
- [ ] 5-7. 서브태스크 API 동작 테스트
- [ ] 5-8. `SubtaskItem.tsx` 컴포넌트 (들여쓰기, 작은 체크박스)
- [ ] 5-9. 리마인더 행에서 서브태스크 펼침/접힘 토글
- [ ] 5-10. 상세 패널에서 서브태스크 추가
- [ ] 5-11. 상세 패널에서 서브태스크 편집/삭제

## Phase 6 — 검색 + 인터랙션 + 반응형

- [ ] 6-1. Backend: 검색 API 구현 (`GET /api/reminders/search?q=`)
- [ ] 6-2. 검색바 컴포넌트 (사이드바 상단, debounce 300ms)
- [ ] 6-3. 검색 결과 메인 영역 표시
- [ ] 6-4. 체크박스 완료 애니메이션 (원형 채워짐 → 0.5초 후 완료)
- [ ] 6-5. 리마인더 행 hover 효과
- [ ] 6-6. 사이드바 선택 하이라이트 효과
- [ ] 6-7. 상세 패널 슬라이드 트랜지션
- [ ] 6-8. 반응형 — Desktop 3컬럼 (>1024px)
- [ ] 6-9. 반응형 — Tablet 2컬럼 + 상세 오버레이 (768~1024px)
- [ ] 6-10. 반응형 — Mobile 1컬럼 + 햄버거 메뉴 (<768px)
- [ ] 6-11. 에러 처리 및 로딩 상태 (스켈레톤 UI)
