package toby.ai.tobyreminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toby.ai.tobyreminder.domain.Reminder;
import toby.ai.tobyreminder.domain.ReminderList;
import toby.ai.tobyreminder.domain.Subtask;
import toby.ai.tobyreminder.dto.ReminderRequest;
import toby.ai.tobyreminder.dto.ReminderResponse;
import toby.ai.tobyreminder.dto.SmartListCountResponse;
import toby.ai.tobyreminder.repository.ReminderListRepository;
import toby.ai.tobyreminder.repository.ReminderRepository;
import toby.ai.tobyreminder.repository.SubtaskRepository;
import toby.ai.tobyreminder.service.ports.in.ReminderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReminderService implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderListRepository reminderListRepository;
    private final SubtaskRepository subtaskRepository;

    @Override
    public List<ReminderResponse> findByListId(Long listId) {
        return reminderRepository.findByListIdOrderByDisplayOrderAsc(listId).stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public List<ReminderResponse> findAll() {
        return reminderRepository.findByCompletedFalseOrderByDisplayOrderAsc().stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public List<ReminderResponse> findToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return reminderRepository.findToday(startOfDay, endOfDay).stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public List<ReminderResponse> findScheduled() {
        return reminderRepository.findScheduled().stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public List<ReminderResponse> findFlagged() {
        return reminderRepository.findByFlaggedTrueAndCompletedFalseOrderByDisplayOrderAsc().stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public List<ReminderResponse> findCompleted() {
        return reminderRepository.findByCompletedTrueOrderByCompletedAtDesc().stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public List<ReminderResponse> search(String keyword) {
        return reminderRepository.search(keyword).stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @Override
    public ReminderResponse findById(Long id) {
        Reminder reminder = getReminderOrThrow(id);
        List<Subtask> subtasks = subtaskRepository.findByReminderIdOrderByDisplayOrderAsc(id);
        return ReminderResponse.from(reminder, subtasks);
    }

    @Override
    @Transactional
    public ReminderResponse create(ReminderRequest request) {
        ReminderList list = getReminderListOrThrow(request.getListId());

        Reminder reminder = Reminder.builder()
                .title(request.getTitle())
                .notes(request.getNotes())
                .dueDate(request.getDueDate())
                .priority(request.getPriority())
                .flagged(request.isFlagged())
                .displayOrder(request.getDisplayOrder())
                .list(list)
                .build();

        return ReminderResponse.from(reminderRepository.save(reminder));
    }

    @Override
    @Transactional
    public ReminderResponse update(Long id, ReminderRequest request) {
        Reminder reminder = getReminderOrThrow(id);
        ReminderList list = getReminderListOrThrow(request.getListId());

        reminder.update(
                request.getTitle(),
                request.getNotes(),
                request.getDueDate(),
                request.getPriority(),
                request.isFlagged(),
                request.getDisplayOrder(),
                list
        );

        return ReminderResponse.from(reminder);
    }

    @Override
    @Transactional
    public ReminderResponse toggleComplete(Long id) {
        Reminder reminder = getReminderOrThrow(id);
        reminder.toggleComplete();
        return ReminderResponse.from(reminder);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Reminder reminder = getReminderOrThrow(id);
        reminderRepository.delete(reminder);
    }

    @Override
    public SmartListCountResponse getCounts() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return SmartListCountResponse.builder()
                .today(reminderRepository.countToday(startOfDay, endOfDay))
                .scheduled(reminderRepository.countScheduled())
                .all(reminderRepository.countByCompletedFalse())
                .flagged(reminderRepository.countByFlaggedTrueAndCompletedFalse())
                .completed(reminderRepository.countByCompletedTrue())
                .build();
    }

    private Reminder getReminderOrThrow(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reminder not found: " + id));
    }

    private ReminderList getReminderListOrThrow(Long listId) {
        return reminderListRepository.findById(listId)
                .orElseThrow(() -> new NoSuchElementException("ReminderList not found: " + listId));
    }
}
