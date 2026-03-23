package toby.ai.tobyreminder.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toby.ai.tobyreminder.dto.ReminderListRequest;
import toby.ai.tobyreminder.dto.ReminderListResponse;
import toby.ai.tobyreminder.service.ports.in.ReminderListService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReminderListServiceTest {

    @Autowired
    private ReminderListService reminderListService;

    private ReminderListRequest createRequest(String name, String color, String icon, Integer order) {
        return ReminderListRequest.builder()
                .name(name)
                .color(color)
                .icon(icon)
                .displayOrder(order)
                .build();
    }

    @Test
    @DisplayName("새 리스트를 생성한다")
    void create() {
        ReminderListRequest request = createRequest("Shopping", "#FF9500", "cart", 1);

        ReminderListResponse result = reminderListService.create(request);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Shopping");
        assertThat(result.getColor()).isEqualTo("#FF9500");
        assertThat(result.getIcon()).isEqualTo("cart");
        assertThat(result.getDisplayOrder()).isEqualTo(1);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("전체 리스트를 displayOrder 순으로 조회한다")
    void findAll() {
        reminderListService.create(createRequest("Work", "#007AFF", "briefcase", 2));
        reminderListService.create(createRequest("Shopping", "#FF9500", "cart", 1));

        List<ReminderListResponse> result = reminderListService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Shopping");
        assertThat(result.get(1).getName()).isEqualTo("Work");
    }

    @Test
    @DisplayName("ID로 리스트를 조회한다")
    void findById() {
        ReminderListResponse created = reminderListService.create(
                createRequest("Shopping", "#FF9500", "cart", 1));

        ReminderListResponse result = reminderListService.findById(created.getId());

        assertThat(result.getName()).isEqualTo("Shopping");
        assertThat(result.getColor()).isEqualTo("#FF9500");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회하면 예외가 발생한다")
    void findByIdNotFound() {
        assertThatThrownBy(() -> reminderListService.findById(99L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("리스트를 수정한다")
    void update() {
        ReminderListResponse created = reminderListService.create(
                createRequest("Shopping", "#FF9500", "cart", 1));

        ReminderListRequest updateRequest = createRequest("Groceries", "#34C759", "leaf", 3);
        ReminderListResponse result = reminderListService.update(created.getId(), updateRequest);

        assertThat(result.getName()).isEqualTo("Groceries");
        assertThat(result.getColor()).isEqualTo("#34C759");
        assertThat(result.getIcon()).isEqualTo("leaf");
        assertThat(result.getDisplayOrder()).isEqualTo(3);
    }

    @Test
    @DisplayName("리스트를 삭제한다")
    void delete() {
        ReminderListResponse created = reminderListService.create(
                createRequest("Shopping", "#FF9500", "cart", 1));

        reminderListService.delete(created.getId());

        assertThatThrownBy(() -> reminderListService.findById(created.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("존재하지 않는 리스트를 삭제하면 예외가 발생한다")
    void deleteNotFound() {
        assertThatThrownBy(() -> reminderListService.delete(99L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
