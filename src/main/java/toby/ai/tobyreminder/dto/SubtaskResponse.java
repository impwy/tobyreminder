package toby.ai.tobyreminder.dto;

import lombok.Builder;
import lombok.Getter;
import toby.ai.tobyreminder.domain.Subtask;

import java.time.LocalDateTime;

@Getter
@Builder
public class SubtaskResponse {

    private Long id;
    private String title;
    private boolean completed;
    private Integer displayOrder;
    private Long reminderId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SubtaskResponse from(Subtask subtask) {
        return SubtaskResponse.builder()
                .id(subtask.getId())
                .title(subtask.getTitle())
                .completed(subtask.isCompleted())
                .displayOrder(subtask.getDisplayOrder())
                .reminderId(subtask.getReminder().getId())
                .createdAt(subtask.getCreatedAt())
                .updatedAt(subtask.getUpdatedAt())
                .build();
    }
}
