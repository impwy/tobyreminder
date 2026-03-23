package toby.ai.tobyreminder.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import toby.ai.tobyreminder.domain.Priority;
import toby.ai.tobyreminder.dto.*;
import toby.ai.tobyreminder.service.ports.in.ReminderListService;
import toby.ai.tobyreminder.service.ports.in.ReminderService;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SubtaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReminderListService reminderListService;

    @Autowired
    private ReminderService reminderService;

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

    private String subtaskJson(String title, Integer displayOrder) throws Exception {
        return objectMapper.writeValueAsString(
                SubtaskRequest.builder()
                        .title(title)
                        .displayOrder(displayOrder)
                        .build());
    }

    @Test
    @DisplayName("POST /api/reminders/{id}/subtasks - 서브태스크를 생성하고 201을 반환한다")
    void create() throws Exception {
        mockMvc.perform(post("/api/reminders/{id}/subtasks", reminderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subtaskJson("Milk", 1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Milk"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.reminderId").value(reminderId));
    }

    @Test
    @DisplayName("GET /api/reminders/{id}/subtasks - 서브태스크 목록을 조회한다")
    void findByReminderId() throws Exception {
        mockMvc.perform(post("/api/reminders/{id}/subtasks", reminderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(subtaskJson("Milk", 1)));
        mockMvc.perform(post("/api/reminders/{id}/subtasks", reminderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(subtaskJson("Bread", 2)));

        mockMvc.perform(get("/api/reminders/{id}/subtasks", reminderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("PATCH /api/subtasks/{id}/complete - 서브태스크 완료를 토글한다")
    void toggleComplete() throws Exception {
        String response = mockMvc.perform(post("/api/reminders/{id}/subtasks", reminderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subtaskJson("Milk", 1)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(patch("/api/subtasks/{id}/complete", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    @DisplayName("DELETE /api/subtasks/{id} - 서브태스크를 삭제하고 204를 반환한다")
    void deleteSubtask() throws Exception {
        String response = mockMvc.perform(post("/api/reminders/{id}/subtasks", reminderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(subtaskJson("Milk", 1)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/subtasks/{id}", id))
                .andExpect(status().isNoContent());
    }
}
