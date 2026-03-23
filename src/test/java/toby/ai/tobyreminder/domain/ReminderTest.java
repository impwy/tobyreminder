package toby.ai.tobyreminder.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderTest {

    private ReminderList createList() {
        return ReminderList.builder()
                .name("Shopping")
                .color("#FF9500")
                .icon("cart")
                .displayOrder(1)
                .build();
    }

    @Test
    @DisplayName("Builder로 Reminder를 생성할 수 있다")
    void createWithBuilder() {
        ReminderList list = createList();
        LocalDateTime dueDate = LocalDateTime.of(2026, 3, 25, 10, 0);

        Reminder reminder = Reminder.builder()
                .title("Buy milk")
                .notes("Organic milk")
                .dueDate(dueDate)
                .priority(Priority.HIGH)
                .flagged(true)
                .displayOrder(1)
                .list(list)
                .build();

        assertThat(reminder.getTitle()).isEqualTo("Buy milk");
        assertThat(reminder.getNotes()).isEqualTo("Organic milk");
        assertThat(reminder.getDueDate()).isEqualTo(dueDate);
        assertThat(reminder.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(reminder.isFlagged()).isTrue();
        assertThat(reminder.isCompleted()).isFalse();
        assertThat(reminder.getCompletedAt()).isNull();
        assertThat(reminder.getList()).isEqualTo(list);
        assertThat(reminder.getCreatedAt()).isNotNull();
        assertThat(reminder.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("priority를 지정하지 않으면 NONE이 기본값이다")
    void defaultPriorityIsNone() {
        Reminder reminder = Reminder.builder()
                .title("Test")
                .list(createList())
                .build();

        assertThat(reminder.getPriority()).isEqualTo(Priority.NONE);
    }

    @Test
    @DisplayName("update 메서드로 필드를 변경할 수 있다")
    void update() {
        ReminderList list = createList();
        ReminderList newList = ReminderList.builder()
                .name("Work")
                .color("#007AFF")
                .icon("briefcase")
                .displayOrder(2)
                .build();

        Reminder reminder = Reminder.builder()
                .title("Buy milk")
                .priority(Priority.LOW)
                .list(list)
                .build();

        reminder.update("Call boss", "Important", null, Priority.HIGH, true, 2, newList);

        assertThat(reminder.getTitle()).isEqualTo("Call boss");
        assertThat(reminder.getNotes()).isEqualTo("Important");
        assertThat(reminder.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(reminder.isFlagged()).isTrue();
        assertThat(reminder.getList()).isEqualTo(newList);
    }

    @Test
    @DisplayName("toggleComplete로 완료/미완료를 전환할 수 있다")
    void toggleComplete() {
        Reminder reminder = Reminder.builder()
                .title("Test")
                .list(createList())
                .build();

        assertThat(reminder.isCompleted()).isFalse();
        assertThat(reminder.getCompletedAt()).isNull();

        reminder.toggleComplete();

        assertThat(reminder.isCompleted()).isTrue();
        assertThat(reminder.getCompletedAt()).isNotNull();

        reminder.toggleComplete();

        assertThat(reminder.isCompleted()).isFalse();
        assertThat(reminder.getCompletedAt()).isNull();
    }
}
