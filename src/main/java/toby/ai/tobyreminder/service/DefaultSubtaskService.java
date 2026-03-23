package toby.ai.tobyreminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toby.ai.tobyreminder.domain.Reminder;
import toby.ai.tobyreminder.domain.Subtask;
import toby.ai.tobyreminder.dto.SubtaskRequest;
import toby.ai.tobyreminder.dto.SubtaskResponse;
import toby.ai.tobyreminder.repository.ReminderRepository;
import toby.ai.tobyreminder.repository.SubtaskRepository;
import toby.ai.tobyreminder.service.ports.in.SubtaskService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultSubtaskService implements SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final ReminderRepository reminderRepository;

    @Override
    public List<SubtaskResponse> findByReminderId(Long reminderId) {
        return subtaskRepository.findByReminderIdOrderByDisplayOrderAsc(reminderId).stream()
                .map(SubtaskResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public SubtaskResponse create(Long reminderId, SubtaskRequest request) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new NoSuchElementException("Reminder not found: " + reminderId));

        Subtask subtask = Subtask.builder()
                .title(request.getTitle())
                .displayOrder(request.getDisplayOrder())
                .reminder(reminder)
                .build();

        return SubtaskResponse.from(subtaskRepository.save(subtask));
    }

    @Override
    @Transactional
    public SubtaskResponse update(Long id, SubtaskRequest request) {
        Subtask subtask = getSubtaskOrThrow(id);
        subtask.update(request.getTitle(), request.getDisplayOrder());
        return SubtaskResponse.from(subtask);
    }

    @Override
    @Transactional
    public SubtaskResponse toggleComplete(Long id) {
        Subtask subtask = getSubtaskOrThrow(id);
        subtask.toggleComplete();
        return SubtaskResponse.from(subtask);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Subtask subtask = getSubtaskOrThrow(id);
        subtaskRepository.delete(subtask);
    }

    private Subtask getSubtaskOrThrow(Long id) {
        return subtaskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Subtask not found: " + id));
    }
}
