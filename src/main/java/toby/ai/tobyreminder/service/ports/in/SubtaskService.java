package toby.ai.tobyreminder.service.ports.in;

import toby.ai.tobyreminder.dto.SubtaskRequest;
import toby.ai.tobyreminder.dto.SubtaskResponse;

import java.util.List;

public interface SubtaskService {

    List<SubtaskResponse> findByReminderId(Long reminderId);

    SubtaskResponse create(Long reminderId, SubtaskRequest request);

    SubtaskResponse update(Long id, SubtaskRequest request);

    SubtaskResponse toggleComplete(Long id);

    void delete(Long id);
}
