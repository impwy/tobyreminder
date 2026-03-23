package toby.ai.tobyreminder.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderListTest {

    @Test
    @DisplayName("Builder로 ReminderList를 생성할 수 있다")
    void createWithBuilder() {
        ReminderList list = ReminderList.builder()
                .name("Shopping")
                .color("#FF9500")
                .icon("cart")
                .displayOrder(1)
                .build();

        assertThat(list.getName()).isEqualTo("Shopping");
        assertThat(list.getColor()).isEqualTo("#FF9500");
        assertThat(list.getIcon()).isEqualTo("cart");
        assertThat(list.getDisplayOrder()).isEqualTo(1);
        assertThat(list.getId()).isNull();
    }

    @Test
    @DisplayName("생성 시 createdAt, updatedAt이 자동 설정된다")
    void createdAtAndUpdatedAtAreSetOnCreation() {
        LocalDateTime before = LocalDateTime.now();

        ReminderList list = ReminderList.builder()
                .name("Shopping")
                .color("#FF9500")
                .icon("cart")
                .displayOrder(1)
                .build();

        assertThat(list.getCreatedAt()).isNotNull();
        assertThat(list.getUpdatedAt()).isNotNull();
        assertThat(list.getCreatedAt()).isEqualTo(list.getUpdatedAt());
        assertThat(list.getCreatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    @DisplayName("update 메서드로 필드를 변경할 수 있다")
    void update() {
        ReminderList list = ReminderList.builder()
                .name("Shopping")
                .color("#FF9500")
                .icon("cart")
                .displayOrder(1)
                .build();

        list.update("Work", "#007AFF", "briefcase", 2);

        assertThat(list.getName()).isEqualTo("Work");
        assertThat(list.getColor()).isEqualTo("#007AFF");
        assertThat(list.getIcon()).isEqualTo("briefcase");
        assertThat(list.getDisplayOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("update 시 updatedAt이 갱신되고 createdAt은 유지된다")
    void updateRefreshesUpdatedAt() throws InterruptedException {
        ReminderList list = ReminderList.builder()
                .name("Shopping")
                .color("#FF9500")
                .icon("cart")
                .displayOrder(1)
                .build();

        LocalDateTime createdAt = list.getCreatedAt();
        LocalDateTime firstUpdatedAt = list.getUpdatedAt();

        Thread.sleep(10);

        list.update("Work", "#007AFF", "briefcase", 2);

        assertThat(list.getCreatedAt()).isEqualTo(createdAt);
        assertThat(list.getUpdatedAt()).isAfter(firstUpdatedAt);
    }
}
