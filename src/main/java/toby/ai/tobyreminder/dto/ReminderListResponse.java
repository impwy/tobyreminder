package toby.ai.tobyreminder.dto;

import lombok.Builder;
import lombok.Getter;
import toby.ai.tobyreminder.domain.ReminderList;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReminderListResponse {

    private Long id;
    private String name;
    private String color;
    private String icon;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReminderListResponse from(ReminderList reminderList) {
        return ReminderListResponse.builder()
                .id(reminderList.getId())
                .name(reminderList.getName())
                .color(reminderList.getColor())
                .icon(reminderList.getIcon())
                .displayOrder(reminderList.getDisplayOrder())
                .createdAt(reminderList.getCreatedAt())
                .updatedAt(reminderList.getUpdatedAt())
                .build();
    }
}
