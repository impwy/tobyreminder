package toby.ai.tobyreminder.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminder_list")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReminderList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String color;

    private String icon;

    private Integer displayOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public ReminderList(String name, String color, String icon, Integer displayOrder) {
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.displayOrder = displayOrder;
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void update(String name, String color, String icon, Integer displayOrder) {
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.displayOrder = displayOrder;
        this.updatedAt = LocalDateTime.now();
    }
}
