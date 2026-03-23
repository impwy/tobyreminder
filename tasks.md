# Toby Reminder - Tasks

## Phase 1 — 리스트 + 리마인더 CRUD (Backend)

- [ ] 1-1. `application.properties` 설정 (H2 인메모리, H2 Console, JPA ddl-auto, JSON 날짜 포맷)
- [ ] 1-2. `Priority` Enum 생성 (NONE, LOW, MEDIUM, HIGH)
- [ ] 1-3. `ReminderList` Entity 생성 (id, name, color, icon, displayOrder, createdAt, updatedAt)
- [ ] 1-4. `Reminder` Entity 생성 (id, title, notes, dueDate, priority, flagged, completed, completedAt, displayOrder, `@ManyToOne` list, createdAt, updatedAt)
- [ ] 1-5. `ReminderListRepository` 생성 (JpaRepository)
- [ ] 1-6. `ReminderRepository` 생성 (JpaRepository + 필터 쿼리: listId, today, flagged, completed, scheduled)
- [ ] 1-7. `ReminderListRequest` / `ReminderListResponse` DTO 생성
- [ ] 1-8. `ReminderRequest` / `ReminderResponse` DTO 생성
- [ ] 1-9. `ReminderListService` 생성 (CRUD + 미완료 카운트)
- [ ] 1-10. `ReminderListController` 생성 (GET, POST, PUT, DELETE)
- [ ] 1-11. `ReminderService` 생성 (CRUD + 완료 토글 + 필터 조회)
- [ ] 1-12. `ReminderController` 생성 (GET with 필터, GET/{id}, POST, PUT, PATCH/complete, DELETE)
- [ ] 1-13. 스마트 리스트 카운트 API (`GET /api/reminders/counts`)
- [ ] 1-14. `data.sql` 샘플 데이터 작성 (리스트 + 리마인더)
- [ ] 1-15. 전체 API 동작 테스트 확인

## Phase 2 — Frontend 기본 UI (Next.js)

- [ ] 2-1. `frontend/` 디렉토리에 Next.js 16 프로젝트 생성 (TypeScript, Tailwind CSS, App Router)
- [ ] 2-2. Tailwind CSS에 Apple 시스템 색상 커스텀 설정
- [ ] 2-3. `next.config.ts` Backend API 프록시 설정 (rewrites → localhost:8080)
- [ ] 2-4. `lib/api.ts` fetch 기반 API 클라이언트 작성
- [ ] 2-5. 루트 레이아웃 구현 (3컬럼: 사이드바 | 메인 | 상세패널, Apple 시스템 폰트, #F2F2F7 배경)
- [ ] 2-6. Apple 스타일 원형 체크박스 컴포넌트 구현
- [ ] 2-7. 사이드바 — 스마트 리스트 카드 그리드 (Today, Scheduled, All, Flagged, Completed)
- [ ] 2-8. 사이드바 — My Lists 섹션 (색상 불릿 + 리스트명 + 카운트)
- [ ] 2-9. 메인 영역 — 리스트 제목 표시 (색상, 34px Bold)
- [ ] 2-10. 메인 영역 — 리마인더 행 표시 (체크박스 + 제목 + 메모 미리보기 + 마감일)
- [ ] 2-11. 메인 영역 — 리마인더 완료 체크/해제 동작
- [ ] 2-12. 메인 영역 — "+ New Reminder" 인라인 입력 기능
- [ ] 2-13. 사이드바 리스트 선택 → 해당 리마인더 목록 연동

## Phase 3 — 리마인더 상세 편집 + 리스트 CRUD (Frontend)

- [ ] 3-1. 상세 패널 기본 구조 (리마인더 클릭 시 우측 인스펙터 표시)
- [ ] 3-2. 상세 패널 — 제목/메모 인라인 편집
- [ ] 3-3. 상세 패널 — Date picker (토글 스위치 + 날짜/시간 선택)
- [ ] 3-4. 상세 패널 — Priority 선택 (None/Low/Medium/High)
- [ ] 3-5. 상세 패널 — Flag 토글 스위치
- [ ] 3-6. 상세 패널 — List 선택 (색상 원형 불릿 포함 드롭다운)
- [ ] 3-7. 상세 패널 — Apple 스타일 폼 그룹 (둥근 흰색 카드)
- [ ] 3-8. 리마인더 삭제 기능 (컨텍스트 메뉴 또는 삭제 버튼)
- [ ] 3-9. 리스트 생성 모달 (이름, 색상 팔레트, 아이콘 선택)
- [ ] 3-10. 리스트 수정 기능
- [ ] 3-11. 리스트 삭제 기능
- [ ] 3-12. 스마트 리스트 뷰 — Today 페이지
- [ ] 3-13. 스마트 리스트 뷰 — Scheduled 페이지
- [ ] 3-14. 스마트 리스트 뷰 — All 페이지
- [ ] 3-15. 스마트 리스트 뷰 — Flagged 페이지
- [ ] 3-16. 스마트 리스트 뷰 — Completed 페이지

## Phase 4 — 서브태스크 (Full Stack)

- [ ] 4-1. `Subtask` Entity 생성 (id, title, completed, displayOrder, reminder FK, timestamps)
- [ ] 4-2. `SubtaskRepository` 생성
- [ ] 4-3. `SubtaskRequest` / `SubtaskResponse` DTO 생성
- [ ] 4-4. `SubtaskService` 생성 (CRUD + 완료 토글)
- [ ] 4-5. `SubtaskController` 생성 (GET, POST, PUT, PATCH, DELETE)
- [ ] 4-6. `ReminderResponse`에 subtasks 리스트 포함
- [ ] 4-7. 서브태스크 API 동작 테스트
- [ ] 4-8. `SubtaskItem.tsx` 컴포넌트 (들여쓰기, 작은 체크박스)
- [ ] 4-9. 리마인더 행에서 서브태스크 펼침/접힘 토글
- [ ] 4-10. 상세 패널에서 서브태스크 추가
- [ ] 4-11. 상세 패널에서 서브태스크 편집/삭제

## Phase 5 — 검색 + 인터랙션 + 반응형

- [ ] 5-1. Backend: 검색 API 구현 (`GET /api/reminders/search?q=`)
- [ ] 5-2. 검색바 컴포넌트 (사이드바 상단, debounce 300ms)
- [ ] 5-3. 검색 결과 메인 영역 표시
- [ ] 5-4. 체크박스 완료 애니메이션 (원형 채워짐 → 0.5초 후 완료)
- [ ] 5-5. 리마인더 행 hover 효과
- [ ] 5-6. 사이드바 선택 하이라이트 효과
- [ ] 5-7. 상세 패널 슬라이드 트랜지션
- [ ] 5-8. 반응형 — Desktop 3컬럼 (>1024px)
- [ ] 5-9. 반응형 — Tablet 2컬럼 + 상세 오버레이 (768~1024px)
- [ ] 5-10. 반응형 — Mobile 1컬럼 + 햄버거 메뉴 (<768px)
- [ ] 5-11. 에러 처리 및 로딩 상태 (스켈레톤 UI)
