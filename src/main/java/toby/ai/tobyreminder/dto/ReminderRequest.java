package toby.ai.tobyreminder.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toby.ai.tobyreminder.domain.Priority;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReminderRequest {

    private String title;
    private String notes;
    private LocalDateTime dueDate;
    private Priority priority;
    private boolean flagged;
    private Integer displayOrder;
    private Long listId;

    @Builder
    public ReminderRequest(String title, String notes, LocalDateTime dueDate, Priority priority,
                           boolean flagged, Integer displayOrder, Long listId) {
        this.title = title;
        this.notes = notes;
        this.dueDate = dueDate;
        this.priority = priority;
        this.flagged = flagged;
        this.displayOrder = displayOrder;
        this.listId = listId;
    }
}
