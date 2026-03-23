package toby.ai.tobyreminder.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SubtaskTest {

    private Reminder createReminder() {
        ReminderList list = ReminderList.builder()
                .name("Shopping")
                .color("#FF9500")
                .icon("cart")
                .displayOrder(1)
                .build();
        return Reminder.builder()
                .title("Buy milk")
                .list(list)
                .build();
    }

    @Test
    @DisplayName("Builder로 Subtask를 생성할 수 있다")
    void createWithBuilder() {
        Reminder reminder = createReminder();

        Subtask subtask = Subtask.builder()
                .title("Whole milk")
                .displayOrder(1)
                .reminder(reminder)
                .build();

        assertThat(subtask.getTitle()).isEqualTo("Whole milk");
        assertThat(subtask.isCompleted()).isFalse();
        assertThat(subtask.getDisplayOrder()).isEqualTo(1);
        assertThat(subtask.getReminder()).isEqualTo(reminder);
        assertThat(subtask.getCreatedAt()).isNotNull();
        assertThat(subtask.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("update 메서드로 제목과 순서를 변경할 수 있다")
    void update() {
        Subtask subtask = Subtask.builder()
                .title("Whole milk")
                .displayOrder(1)
                .reminder(createReminder())
                .build();

        subtask.update("Skim milk", 2);

        assertThat(subtask.getTitle()).isEqualTo("Skim milk");
        assertThat(subtask.getDisplayOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("toggleComplete로 완료/미완료를 전환할 수 있다")
    void toggleComplete() {
        Subtask subtask = Subtask.builder()
                .title("Whole milk")
                .displayOrder(1)
                .reminder(createReminder())
                .build();

        assertThat(subtask.isCompleted()).isFalse();

        subtask.toggleComplete();
        assertThat(subtask.isCompleted()).isTrue();

        subtask.toggleComplete();
        assertThat(subtask.isCompleted()).isFalse();
    }
}
