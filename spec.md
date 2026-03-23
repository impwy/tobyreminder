# Toby Reminder - Spec

## 1. 개요

Apple Reminder App의 핵심 기능을 웹 버전으로 구현한 할 일 관리 애플리케이션.

## 2. 기술 스택

| 구분 | 기술 | 버전 |
|---|---|---|
| Backend | Spring Boot | 4.0.4 |
| ORM | Spring Data JPA | - |
| Database | H2 (embedded) | - |
| Language | Java | 25 |
| Build | Gradle (Kotlin DSL) | 9.4 |
| Frontend | Next.js | 16 (latest) |
| Frontend Language | TypeScript | - |

## 3. 핵심 기능

### 3.1 리스트 관리

- 리마인더 리스트 생성/수정/삭제
- 리스트별 색상 및 아이콘 지정
- 기본 리스트: Today, Scheduled, All, Flagged, Completed

### 3.2 리마인더(할 일) 관리

- 리마인더 생성/수정/삭제
- 완료 체크/해제
- 리마인더 속성:
  - 제목 (필수)
  - 메모
  - 마감일 (날짜 + 시간, 선택)
  - 우선순위 (없음 / 낮음 / 보통 / 높음)
  - 플래그 (on/off)
  - 리스트 소속

### 3.3 하위 작업 (Subtask)

- 리마인더 하위에 서브태스크 추가/수정/삭제
- 서브태스크 완료 체크/해제

### 3.4 스마트 리스트 (필터 뷰)

| 이름 | 조건 |
|---|---|
| Today | 마감일이 오늘인 항목 |
| Scheduled | 마감일이 설정된 항목 |
| All | 모든 미완료 항목 |
| Flagged | 플래그가 켜진 항목 |
| Completed | 완료된 항목 |

### 3.5 검색

- 리마인더 제목 및 메모 기반 검색

## 4. API 설계 (REST)

### 4.1 리스트 API

| Method | Endpoint | 설명 |
|---|---|---|
| GET | /api/lists | 전체 리스트 조회 |
| POST | /api/lists | 리스트 생성 |
| PUT | /api/lists/{id} | 리스트 수정 |
| DELETE | /api/lists/{id} | 리스트 삭제 |

### 4.2 리마인더 API

| Method | Endpoint | 설명 |
|---|---|---|
| GET | /api/reminders | 리마인더 조회 (필터 지원) |
| GET | /api/reminders/{id} | 리마인더 상세 조회 |
| POST | /api/reminders | 리마인더 생성 |
| PUT | /api/reminders/{id} | 리마인더 수정 |
| PATCH | /api/reminders/{id}/complete | 완료 토글 |
| DELETE | /api/reminders/{id} | 리마인더 삭제 |

### 4.3 서브태스크 API

| Method | Endpoint | 설명 |
|---|---|---|
| GET | /api/reminders/{id}/subtasks | 서브태스크 조회 |
| POST | /api/reminders/{id}/subtasks | 서브태스크 생성 |
| PUT | /api/subtasks/{id} | 서브태스크 수정 |
| PATCH | /api/subtasks/{id}/complete | 완료 토글 |
| DELETE | /api/subtasks/{id} | 서브태스크 삭제 |

### 4.4 검색 API

| Method | Endpoint | 설명 |
|---|---|---|
| GET | /api/reminders/search?q={keyword} | 리마인더 검색 |

## 5. 데이터 모델

### ReminderList

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long | PK |
| name | String | 리스트 이름 |
| color | String | 색상 코드 (#hex) |
| icon | String | 아이콘 이름 |
| displayOrder | Integer | 정렬 순서 |
| createdAt | LocalDateTime | 생성 시각 |
| updatedAt | LocalDateTime | 수정 시각 |

### Reminder

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long | PK |
| title | String | 제목 |
| notes | String | 메모 |
| dueDate | LocalDateTime | 마감일시 |
| priority | Enum | NONE, LOW, MEDIUM, HIGH |
| flagged | Boolean | 플래그 여부 |
| completed | Boolean | 완료 여부 |
| completedAt | LocalDateTime | 완료 시각 |
| displayOrder | Integer | 정렬 순서 |
| list | ReminderList | 소속 리스트 (FK) |
| createdAt | LocalDateTime | 생성 시각 |
| updatedAt | LocalDateTime | 수정 시각 |

### Subtask

| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long | PK |
| title | String | 제목 |
| completed | Boolean | 완료 여부 |
| displayOrder | Integer | 정렬 순서 |
| reminder | Reminder | 상위 리마인더 (FK) |
| createdAt | LocalDateTime | 생성 시각 |
| updatedAt | LocalDateTime | 수정 시각 |

## 6. 프론트엔드 구조 (Next.js)

```
frontend/
├── app/
│   ├── layout.tsx          # 루트 레이아웃 (사이드바 + 메인)
│   ├── page.tsx            # 메인 대시보드 (스마트 리스트 카운트)
│   ├── today/
│   ├── scheduled/
│   ├── all/
│   ├── flagged/
│   ├── completed/
│   └── lists/[id]/         # 개별 리스트 뷰
├── components/
│   ├── Sidebar.tsx          # 사이드바 (스마트 리스트 + 사용자 리스트)
│   ├── ReminderItem.tsx     # 리마인더 행
│   ├── ReminderDetail.tsx   # 리마인더 상세/편집 패널
│   ├── SubtaskItem.tsx      # 서브태스크 행
│   ├── ListForm.tsx         # 리스트 생성/수정 모달
│   └── SearchBar.tsx        # 검색
└── lib/
    └── api.ts               # API 클라이언트
```

## 7. UI/UX 디자인 (Apple Reminders 준수)

### 7.1 전체 레이아웃

- **3컬럼 구조**: 사이드바 (280px) | 리마인더 리스트 (메인) | 상세 패널 (인스펙터)
- macOS Apple Reminders와 동일한 레이아웃 구조
- 배경: 밝은 그레이 (`#F2F2F7`) — Apple 시스템 배경색

### 7.2 사이드바

- 상단 검색바 (둥근 모서리, 돋보기 아이콘, `#E5E5EA` 배경)
- **스마트 리스트 그리드** (2x2 + 1 카드 배치)
  - Today: 파란색 원 아이콘 + 미완료 카운트 배지
  - Scheduled: 빨간색 달력 아이콘
  - All: 검정색 트레이 아이콘
  - Flagged: 주황색 깃발 아이콘
  - Completed: 회색 체크 아이콘
- 각 카드: 둥근 모서리 (`border-radius: 12px`), 흰색 배경, 좌측 상단 아이콘+카운트, 하단 라벨
- **My Lists** 섹션: 리스트 이름 좌측에 색상 원형 불릿
- 하단 **Add List** 버튼

### 7.3 리마인더 리스트 (메인 영역)

- 상단: 리스트 제목 (리스트 색상으로 표시, 굵은 34px)
- 각 리마인더 행:
  - 좌측: 빈 원형 체크박스 (리스트 색상 테두리)
  - 완료 시: 채워진 원 + 체크마크, 텍스트에 취소선 + 회색 처리
  - 제목 우측: 플래그 아이콘 (설정 시 주황색)
  - 제목 아래: 메모 미리보기 (회색, 1줄), 마감일 표시
  - 서브태스크가 있으면 들여쓰기하여 표시
- 하단: **+ New Reminder** 텍스트 버튼 (리스트 색상)
- 리마인더 사이 구분선: 얇은 `#E5E5EA` 라인

### 7.4 상세 패널 (인스펙터)

- 리마인더 클릭 시 우측에 슬라이드 표시
- Apple 스타일 폼 그룹:
  - 제목 입력 필드 (큰 텍스트)
  - 메모 입력 필드
  - 구분선
  - Date 토글 스위치 + DatePicker
  - Priority 셀렉트 (None / Low / Medium / High)
  - Flag 토글 스위치
  - List 셀렉트 (색상 원형 불릿 포함)
- 각 필드 그룹: 둥근 흰색 카드 (`border-radius: 10px`)

### 7.5 색상 시스템 (Apple System Colors)

| 이름 | 색상 코드 | 용도 |
|---|---|---|
| Blue | `#007AFF` | 기본 액센트, Today |
| Red | `#FF3B30` | Scheduled |
| Orange | `#FF9500` | Flag |
| Yellow | `#FFCC00` | 리스트 색상 옵션 |
| Green | `#34C759` | 리스트 색상 옵션 |
| Purple | `#AF52DE` | 리스트 색상 옵션 |
| Pink | `#FF2D55` | 리스트 색상 옵션 |
| Gray | `#8E8E93` | Completed, 보조 텍스트 |
| Brown | `#A2845E` | 리스트 색상 옵션 |

### 7.6 타이포그래피

- 폰트: `-apple-system, BlinkMacSystemFont, 'SF Pro', 'Helvetica Neue', sans-serif`
- 리스트 제목: 34px, Bold
- 리마인더 제목: 16px, Regular
- 보조 텍스트 (메모, 날짜): 14px, `#8E8E93`
- 사이드바 라벨: 14px, Medium

### 7.7 인터랙션

- 체크박스 클릭 시: 원형이 채워지는 애니메이션 → 0.5초 후 완료 처리
- 리마인더 행 hover: 연한 회색 배경 (`#F2F2F7`)
- 사이드바 리스트 선택: 파란색 하이라이트 배경 (`#007AFF` 10% opacity)
- 새 리마인더 추가: 리스트 하단에 인라인 입력 필드 생성
- 삭제: 스와이프 또는 우클릭 컨텍스트 메뉴
- 모달/팝오버: Apple 스타일 둥근 모서리, 그림자

### 7.8 반응형

- **Desktop** (>1024px): 3컬럼 (사이드바 + 리스트 + 상세)
- **Tablet** (768~1024px): 2컬럼 (사이드바 + 리스트), 상세는 오버레이
- **Mobile** (<768px): 1컬럼, 사이드바는 햄버거 메뉴로 토글

