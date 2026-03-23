package toby.ai.tobyreminder.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubtaskRequest {

    private String title;
    private Integer displayOrder;

    @Builder
    public SubtaskRequest(String title, Integer displayOrder) {
        this.title = title;
        this.displayOrder = displayOrder;
    }
}
