package toby.ai.tobyreminder.service.ports.in;

import toby.ai.tobyreminder.dto.ReminderRequest;
import toby.ai.tobyreminder.dto.ReminderResponse;
import toby.ai.tobyreminder.dto.SmartListCountResponse;

import java.util.List;

public interface ReminderService {

    List<ReminderResponse> findByListId(Long listId);

    List<ReminderResponse> findAll();

    List<ReminderResponse> findToday();

    List<ReminderResponse> findScheduled();

    List<ReminderResponse> findFlagged();

    List<ReminderResponse> findCompleted();

    List<ReminderResponse> search(String keyword);

    ReminderResponse findById(Long id);

    ReminderResponse create(ReminderRequest request);

    ReminderResponse update(Long id, ReminderRequest request);

    ReminderResponse toggleComplete(Long id);

    void delete(Long id);

    SmartListCountResponse getCounts();
}
