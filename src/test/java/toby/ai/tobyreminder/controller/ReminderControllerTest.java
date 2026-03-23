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
import toby.ai.tobyreminder.dto.ReminderListRequest;
import toby.ai.tobyreminder.dto.ReminderListResponse;
import toby.ai.tobyreminder.dto.ReminderRequest;
import toby.ai.tobyreminder.service.ports.in.ReminderListService;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReminderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    private String reminderJson(String title, Priority priority, boolean flagged, LocalDateTime dueDate) throws Exception {
        return objectMapper.writeValueAsString(
                ReminderRequest.builder()
                        .title(title)
                        .priority(priority)
                        .flagged(flagged)
                        .dueDate(dueDate)
                        .listId(listId)
                        .build());
    }

    @Test
    @DisplayName("POST /api/reminders - 리마인더를 생성하고 201을 반환한다")
    void create() throws Exception {
        mockMvc.perform(post("/api/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reminderJson("Buy milk", Priority.HIGH, false, null)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Buy milk"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.listId").value(listId));
    }

    @Test
    @DisplayName("GET /api/reminders - 미완료 리마인더를 조회한다")
    void findAll() throws Exception {
        mockMvc.perform(post("/api/reminders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reminderJson("Task 1", Priority.NONE, false, null)));

        mockMvc.perform(get("/api/reminders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/reminders?listId= - 리스트별 리마인더를 조회한다")
    void findByListId() throws Exception {
        mockMvc.perform(post("/api/reminders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reminderJson("Buy milk", Priority.NONE, false, null)));

        mockMvc.perform(get("/api/reminders").param("listId", listId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Buy milk"));
    }

    @Test
    @DisplayName("GET /api/reminders?flagged=true - 플래그된 리마인더를 조회한다")
    void findFlagged() throws Exception {
        mockMvc.perform(post("/api/reminders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reminderJson("Normal", Priority.NONE, false, null)));
        mockMvc.perform(post("/api/reminders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reminderJson("Flagged", Priority.NONE, true, null)));

        mockMvc.perform(get("/api/reminders").param("flagged", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Flagged"));
    }

    @Test
    @DisplayName("PATCH /api/reminders/{id}/complete - 완료 토글한다")
    void toggleComplete() throws Exception {
        String response = mockMvc.perform(post("/api/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reminderJson("Task", Priority.NONE, false, null)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(patch("/api/reminders/{id}/complete", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.completedAt").isNotEmpty());
    }

    @Test
    @DisplayName("DELETE /api/reminders/{id} - 리마인더를 삭제하고 204를 반환한다")
    void deleteReminder() throws Exception {
        String response = mockMvc.perform(post("/api/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reminderJson("Task", Priority.NONE, false, null)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/reminders/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/reminders/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/reminders/counts - 스마트 리스트 카운트를 조회한다")
    void getCounts() throws Exception {
        LocalDateTime todayDue = LocalDate.now().atTime(15, 0);
        mockMvc.perform(post("/api/reminders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reminderJson("Today task", Priority.NONE, true, todayDue)));

        mockMvc.perform(get("/api/reminders/counts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.today").value(1))
                .andExpect(jsonPath("$.scheduled").value(1))
                .andExpect(jsonPath("$.all").value(1))
                .andExpect(jsonPath("$.flagged").value(1))
                .andExpect(jsonPath("$.completed").value(0));
    }

    @Test
    @DisplayName("GET /api/reminders/search?q= - 리마인더를 검색한다")
    void search() throws Exception {
        mockMvc.perform(post("/api/reminders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reminderJson("Buy milk", Priority.NONE, false, null)));
        mockMvc.perform(post("/api/reminders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reminderJson("Call dentist", Priority.NONE, false, null)));

        mockMvc.perform(get("/api/reminders/search").param("q", "milk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Buy milk"));
    }
}
