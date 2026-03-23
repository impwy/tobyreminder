package toby.ai.tobyreminder.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import toby.ai.tobyreminder.dto.ReminderListRequest;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReminderListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String createListJson(String name, String color, String icon, Integer order) throws Exception {
        return objectMapper.writeValueAsString(
                ReminderListRequest.builder()
                        .name(name)
                        .color(color)
                        .icon(icon)
                        .displayOrder(order)
                        .build()
        );
    }

    @Test
    @DisplayName("POST /api/lists - 리스트를 생성하고 201을 반환한다")
    void create() throws Exception {
        mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createListJson("Shopping", "#FF9500", "cart", 1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Shopping"))
                .andExpect(jsonPath("$.color").value("#FF9500"))
                .andExpect(jsonPath("$.icon").value("cart"))
                .andExpect(jsonPath("$.displayOrder").value(1))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/lists - 전체 리스트를 조회한다")
    void findAll() throws Exception {
        mockMvc.perform(post("/api/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createListJson("Work", "#007AFF", "briefcase", 2)));
        mockMvc.perform(post("/api/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createListJson("Shopping", "#FF9500", "cart", 1)));

        mockMvc.perform(get("/api/lists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Shopping"))
                .andExpect(jsonPath("$[1].name").value("Work"));
    }

    @Test
    @DisplayName("GET /api/lists/{id} - ID로 리스트를 조회한다")
    void findById() throws Exception {
        String response = mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createListJson("Shopping", "#FF9500", "cart", 1)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/lists/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Shopping"));
    }

    @Test
    @DisplayName("GET /api/lists/{id} - 존재하지 않는 ID는 404를 반환한다")
    void findByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/lists/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/lists/{id} - 리스트를 수정한다")
    void update() throws Exception {
        String response = mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createListJson("Shopping", "#FF9500", "cart", 1)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(put("/api/lists/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createListJson("Groceries", "#34C759", "leaf", 3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Groceries"))
                .andExpect(jsonPath("$.color").value("#34C759"));
    }

    @Test
    @DisplayName("DELETE /api/lists/{id} - 리스트를 삭제하고 204를 반환한다")
    void deleteList() throws Exception {
        String response = mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createListJson("Shopping", "#FF9500", "cart", 1)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/lists/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/lists/{id}", id))
                .andExpect(status().isNotFound());
    }
}
