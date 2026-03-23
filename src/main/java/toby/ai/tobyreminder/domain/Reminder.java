package toby.ai.tobyreminder.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminder")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String notes;

    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Column(nullable = false)
    private boolean flagged;

    @Column(nullable = false)
    private boolean completed;

    private LocalDateTime completedAt;

    private Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    private ReminderList list;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public Reminder(String title, String notes, LocalDateTime dueDate, Priority priority,
                    boolean flagged, Integer displayOrder, ReminderList list) {
        this.title = title;
        this.notes = notes;
        this.dueDate = dueDate;
        this.priority = priority != null ? priority : Priority.NONE;
        this.flagged = flagged;
        this.completed = false;
        this.displayOrder = displayOrder;
        this.list = list;
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void update(String title, String notes, LocalDateTime dueDate, Priority priority,
                       boolean flagged, Integer displayOrder, ReminderList list) {
        this.title = title;
        this.notes = notes;
        this.dueDate = dueDate;
        this.priority = priority != null ? priority : Priority.NONE;
        this.flagged = flagged;
        this.displayOrder = displayOrder;
        this.list = list;
        this.updatedAt = LocalDateTime.now();
    }

    public void toggleComplete() {
        this.completed = !this.completed;
        this.completedAt = this.completed ? LocalDateTime.now() : null;
        this.updatedAt = LocalDateTime.now();
    }
}
