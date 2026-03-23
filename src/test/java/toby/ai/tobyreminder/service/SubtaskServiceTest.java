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
import toby.ai.tobyreminder.service.ports.in.SubtaskService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class SubtaskServiceTest {

    @Autowired
    private SubtaskService subtaskService;

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ReminderListService reminderListService;

    private Long reminderId;

    @BeforeEach
    void setUp() {
        ReminderListResponse list = reminderListService.create(
                ReminderListRequest.builder()
                        .name("Shopping")
                        .color("#FF9500")
                        .icon("cart")
                        .displayOrder(1)
                        .build());

        ReminderResponse reminder = reminderService.create(
                ReminderRequest.builder()
                        .title("Buy groceries")
                        .priority(Priority.NONE)
                        .listId(list.getId())
                        .build());

        reminderId = reminder.getId();
    }

    @Test
    @DisplayName("서브태스크를 생성한다")
    void create() {
        SubtaskResponse result = subtaskService.create(reminderId,
                SubtaskRequest.builder().title("Milk").displayOrder(1).build());

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Milk");
        assertThat(result.isCompleted()).isFalse();
        assertThat(result.getReminderId()).isEqualTo(reminderId);
    }

    @Test
    @DisplayName("리마인더별 서브태스크를 조회한다")
    void findByReminderId() {
        subtaskService.create(reminderId, SubtaskRequest.builder().title("Milk").displayOrder(1).build());
        subtaskService.create(reminderId, SubtaskRequest.builder().title("Bread").displayOrder(2).build());

        List<SubtaskResponse> result = subtaskService.findByReminderId(reminderId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Milk");
        assertThat(result.get(1).getTitle()).isEqualTo("Bread");
    }

    @Test
    @DisplayName("서브태스크를 수정한다")
    void update() {
        SubtaskResponse created = subtaskService.create(reminderId,
                SubtaskRequest.builder().title("Milk").displayOrder(1).build());

        SubtaskResponse result = subtaskService.update(created.getId(),
                SubtaskRequest.builder().title("Skim milk").displayOrder(2).build());

        assertThat(result.getTitle()).isEqualTo("Skim milk");
        assertThat(result.getDisplayOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("서브태스크 완료를 토글한다")
    void toggleComplete() {
        SubtaskResponse created = subtaskService.create(reminderId,
                SubtaskRequest.builder().title("Milk").displayOrder(1).build());

        SubtaskResponse completed = subtaskService.toggleComplete(created.getId());
        assertThat(completed.isCompleted()).isTrue();

        SubtaskResponse uncompleted = subtaskService.toggleComplete(created.getId());
        assertThat(uncompleted.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("서브태스크를 삭제한다")
    void delete() {
        SubtaskResponse created = subtaskService.create(reminderId,
                SubtaskRequest.builder().title("Milk").displayOrder(1).build());

        subtaskService.delete(created.getId());

        List<SubtaskResponse> result = subtaskService.findByReminderId(reminderId);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 서브태스크 수정 시 예외가 발생한다")
    void updateNotFound() {
        assertThatThrownBy(() -> subtaskService.update(999L,
                SubtaskRequest.builder().title("X").build()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("리마인더 상세 조회 시 서브태스크가 포함된다")
    void reminderResponseIncludesSubtasks() {
        subtaskService.create(reminderId, SubtaskRequest.builder().title("Milk").displayOrder(1).build());
        subtaskService.create(reminderId, SubtaskRequest.builder().title("Bread").displayOrder(2).build());

        ReminderResponse reminder = reminderService.findById(reminderId);

        assertThat(reminder.getSubtasks()).hasSize(2);
        assertThat(reminder.getSubtasks().get(0).getTitle()).isEqualTo("Milk");
    }
}
