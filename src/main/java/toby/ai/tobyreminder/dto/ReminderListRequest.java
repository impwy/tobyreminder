package toby.ai.tobyreminder.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReminderListRequest {

    private String name;
    private String color;
    private String icon;
    private Integer displayOrder;

    @Builder
    public ReminderListRequest(String name, String color, String icon, Integer displayOrder) {
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.displayOrder = displayOrder;
    }
}
