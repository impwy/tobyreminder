package toby.ai.tobyreminder.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subtask")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subtask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean completed;

    private Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reminder_id", nullable = false)
    private Reminder reminder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public Subtask(String title, Integer displayOrder, Reminder reminder) {
        this.title = title;
        this.completed = false;
        this.displayOrder = displayOrder;
        this.reminder = reminder;
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void update(String title, Integer displayOrder) {
        this.title = title;
        this.displayOrder = displayOrder;
        this.updatedAt = LocalDateTime.now();
    }

    public void toggleComplete() {
        this.completed = !this.completed;
        this.updatedAt = LocalDateTime.now();
    }
}
