package toby.ai.tobyreminder.dto;

import lombok.Builder;
import lombok.Getter;
import toby.ai.tobyreminder.domain.Priority;
import toby.ai.tobyreminder.domain.Reminder;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReminderResponse {

    private Long id;
    private String title;
    private String notes;
    private LocalDateTime dueDate;
    private Priority priority;
    private boolean flagged;
    private boolean completed;
    private LocalDateTime completedAt;
    private Integer displayOrder;
    private Long listId;
    private String listName;
    private String listColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReminderResponse from(Reminder reminder) {
        return ReminderResponse.builder()
                .id(reminder.getId())
                .title(reminder.getTitle())
                .notes(reminder.getNotes())
                .dueDate(reminder.getDueDate())
                .priority(reminder.getPriority())
                .flagged(reminder.isFlagged())
                .completed(reminder.isCompleted())
                .completedAt(reminder.getCompletedAt())
                .displayOrder(reminder.getDisplayOrder())
                .listId(reminder.getList().getId())
                .listName(reminder.getList().getName())
                .listColor(reminder.getList().getColor())
                .createdAt(reminder.getCreatedAt())
                .updatedAt(reminder.getUpdatedAt())
                .build();
    }
}
