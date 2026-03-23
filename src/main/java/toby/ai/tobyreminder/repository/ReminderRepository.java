package toby.ai.tobyreminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toby.ai.tobyreminder.domain.Reminder;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByListIdOrderByDisplayOrderAsc(Long listId);

    List<Reminder> findByCompletedFalseOrderByDisplayOrderAsc();

    List<Reminder> findByCompletedTrueOrderByCompletedAtDesc();

    List<Reminder> findByFlaggedTrueAndCompletedFalseOrderByDisplayOrderAsc();

    @Query("SELECT r FROM Reminder r WHERE r.dueDate IS NOT NULL AND r.completed = false ORDER BY r.dueDate ASC")
    List<Reminder> findScheduled();

    @Query("SELECT r FROM Reminder r WHERE r.dueDate >= :startOfDay AND r.dueDate < :endOfDay AND r.completed = false ORDER BY r.dueDate ASC")
    List<Reminder> findToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT r FROM Reminder r WHERE (LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(r.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))) ORDER BY r.displayOrder ASC")
    List<Reminder> search(@Param("keyword") String keyword);

    long countByCompletedFalse();

    long countByCompletedTrue();

    long countByFlaggedTrueAndCompletedFalse();

    @Query("SELECT COUNT(r) FROM Reminder r WHERE r.dueDate IS NOT NULL AND r.completed = false")
    long countScheduled();

    @Query("SELECT COUNT(r) FROM Reminder r WHERE r.dueDate >= :startOfDay AND r.dueDate < :endOfDay AND r.completed = false")
    long countToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
