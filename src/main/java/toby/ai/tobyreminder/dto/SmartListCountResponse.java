package toby.ai.tobyreminder.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmartListCountResponse {

    private long today;
    private long scheduled;
    private long all;
    private long flagged;
    private long completed;
}
