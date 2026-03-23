package toby.ai.tobyreminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toby.ai.tobyreminder.domain.ReminderList;
import toby.ai.tobyreminder.dto.ReminderListRequest;
import toby.ai.tobyreminder.dto.ReminderListResponse;
import toby.ai.tobyreminder.service.ports.in.ReminderListService;
import toby.ai.tobyreminder.repository.ReminderListRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReminderListService implements ReminderListService {

    private final ReminderListRepository reminderListRepository;

    @Override
    public List<ReminderListResponse> findAll() {
        return reminderListRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(ReminderListResponse::from)
                .toList();
    }

    @Override
    public ReminderListResponse findById(Long id) {
        ReminderList reminderList = getReminderListOrThrow(id);
        return ReminderListResponse.from(reminderList);
    }

    @Override
    @Transactional
    public ReminderListResponse create(ReminderListRequest request) {
        ReminderList reminderList = ReminderList.builder()
                .name(request.getName())
                .color(request.getColor())
                .icon(request.getIcon())
                .displayOrder(request.getDisplayOrder())
                .build();

        return ReminderListResponse.from(reminderListRepository.save(reminderList));
    }

    @Override
    @Transactional
    public ReminderListResponse update(Long id, ReminderListRequest request) {
        ReminderList reminderList = getReminderListOrThrow(id);

        reminderList.update(
                request.getName(),
                request.getColor(),
                request.getIcon(),
                request.getDisplayOrder()
        );

        return ReminderListResponse.from(reminderList);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ReminderList reminderList = getReminderListOrThrow(id);
        reminderListRepository.delete(reminderList);
    }

    private ReminderList getReminderListOrThrow(Long id) {
        return reminderListRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ReminderList not found: " + id));
    }
}
