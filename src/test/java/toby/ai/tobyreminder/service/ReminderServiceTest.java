package toby.ai.tobyreminder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toby.ai.tobyreminder.domain.Priority;
import toby.ai.tobyreminder.dto.*;
import toby.ai.tobyreminder.service.ports.in.ReminderListService;
import toby.ai.tobyreminder.service.ports.in.ReminderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReminderServiceTest {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ReminderListService reminderListService;

    private Long listId;

    @BeforeEach
    void setUp() {
        ReminderListResponse list = reminderListService.create(
                ReminderListRequest.builder()
                        .name("Shopping")
                        .color("#FF9500")
                        .icon("cart")
                        .displayOrder(1)
                        .build());
        listId = list.getId();
    }

    private ReminderRequest createRequest(String title, Priority priority, boolean flagged, LocalDateTime dueDate) {
        return ReminderRequest.builder()
                .title(title)
                .priority(priority)
                .flagged(flagged)
                .dueDate(dueDate)
                .listId(listId)
                .build();
    }

    @Test
    @DisplayName("리마인더를 생성한다")
    void create() {
        ReminderResponse result = reminderService.create(
                createRequest("Buy milk", Priority.HIGH, false, null));

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Buy milk");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(result.getListId()).isEqualTo(listId);
    }

    @Test
    @DisplayName("ID로 리마인더를 조회한다")
    void findById() {
        ReminderResponse created = reminderService.create(
                createRequest("Buy milk", Priority.HIGH, false, null));

        ReminderResponse result = reminderService.findById(created.getId());

        assertThat(result.getTitle()).isEqualTo("Buy milk");
    }

    @Test
    @DisplayName("리스트별 리마인더를 조회한다")
    void findByListId() {
        reminderService.create(createRequest("Buy milk", Priority.NONE, false, null));
        reminderService.create(createRequest("Buy bread", Priority.NONE, false, null));

        List<ReminderResponse> result = reminderService.findByListId(listId);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("미완료 리마인더 전체를 조회한다")
    void findAll() {
        reminderService.create(createRequest("Task 1", Priority.NONE, false, null));
        ReminderResponse completed = reminderService.create(
                createRequest("Task 2", Priority.NONE, false, null));
        reminderService.toggleComplete(completed.getId());

        List<ReminderResponse> result = reminderService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("플래그된 리마인더를 조회한다")
    void findFlagged() {
        reminderService.create(createRequest("Normal", Priority.NONE, false, null));
        reminderService.create(createRequest("Flagged", Priority.NONE, true, null));

        List<ReminderResponse> result = reminderService.findFlagged();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Flagged");
    }

    @Test
    @DisplayName("오늘 마감 리마인더를 조회한다")
    void findToday() {
        LocalDateTime todayDue = LocalDate.now().atTime(15, 0);
        LocalDateTime tomorrowDue = LocalDate.now().plusDays(1).atTime(10, 0);

        reminderService.create(createRequest("Today task", Priority.NONE, false, todayDue));
        reminderService.create(createRequest("Tomorrow task", Priority.NONE, false, tomorrowDue));

        List<ReminderResponse> result = reminderService.findToday();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Today task");
    }

    @Test
    @DisplayName("리마인더를 수정한다")
    void update() {
        ReminderResponse created = reminderService.create(
                createRequest("Buy milk", Priority.LOW, false, null));

        ReminderRequest updateRequest = ReminderRequest.builder()
                .title("Buy organic milk")
                .priority(Priority.HIGH)
                .flagged(true)
                .listId(listId)
                .build();

        ReminderResponse result = reminderService.update(created.getId(), updateRequest);

        assertThat(result.getTitle()).isEqualTo("Buy organic milk");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(result.isFlagged()).isTrue();
    }

    @Test
    @DisplayName("완료 토글로 리마인더를 완료/미완료 전환한다")
    void toggleComplete() {
        ReminderResponse created = reminderService.create(
                createRequest("Buy milk", Priority.NONE, false, null));

        ReminderResponse completed = reminderService.toggleComplete(created.getId());
        assertThat(completed.isCompleted()).isTrue();
        assertThat(completed.getCompletedAt()).isNotNull();

        ReminderResponse uncompleted = reminderService.toggleComplete(created.getId());
        assertThat(uncompleted.isCompleted()).isFalse();
        assertThat(uncompleted.getCompletedAt()).isNull();
    }

    @Test
    @DisplayName("리마인더를 삭제한다")
    void delete() {
        ReminderResponse created = reminderService.create(
                createRequest("Buy milk", Priority.NONE, false, null));

        reminderService.delete(created.getId());

        assertThatThrownBy(() -> reminderService.findById(created.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("스마트 리스트 카운트를 조회한다")
    void getCounts() {
        LocalDateTime todayDue = LocalDate.now().atTime(15, 0);

        reminderService.create(createRequest("Today flagged", Priority.NONE, true, todayDue));
        reminderService.create(createRequest("Scheduled", Priority.NONE, false, LocalDate.now().plusDays(1).atTime(10, 0)));
        ReminderResponse done = reminderService.create(createRequest("Done", Priority.NONE, false, null));
        reminderService.toggleComplete(done.getId());

        SmartListCountResponse counts = reminderService.getCounts();

        assertThat(counts.getToday()).isEqualTo(1);
        assertThat(counts.getScheduled()).isEqualTo(2);
        assertThat(counts.getAll()).isEqualTo(2);
        assertThat(counts.getFlagged()).isEqualTo(1);
        assertThat(counts.getCompleted()).isEqualTo(1);
    }
}
