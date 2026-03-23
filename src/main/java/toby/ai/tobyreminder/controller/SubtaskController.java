package toby.ai.tobyreminder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toby.ai.tobyreminder.dto.SubtaskRequest;
import toby.ai.tobyreminder.dto.SubtaskResponse;
import toby.ai.tobyreminder.service.ports.in.SubtaskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubtaskController {

    private final SubtaskService subtaskService;

    @GetMapping("/api/reminders/{reminderId}/subtasks")
    public ResponseEntity<List<SubtaskResponse>> findByReminderId(@PathVariable Long reminderId) {
        return ResponseEntity.ok(subtaskService.findByReminderId(reminderId));
    }

    @PostMapping("/api/reminders/{reminderId}/subtasks")
    public ResponseEntity<SubtaskResponse> create(@PathVariable Long reminderId,
                                                   @RequestBody SubtaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subtaskService.create(reminderId, request));
    }

    @PutMapping("/api/subtasks/{id}")
    public ResponseEntity<SubtaskResponse> update(@PathVariable Long id,
                                                   @RequestBody SubtaskRequest request) {
        return ResponseEntity.ok(subtaskService.update(id, request));
    }

    @PatchMapping("/api/subtasks/{id}/complete")
    public ResponseEntity<SubtaskResponse> toggleComplete(@PathVariable Long id) {
        return ResponseEntity.ok(subtaskService.toggleComplete(id));
    }

    @DeleteMapping("/api/subtasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subtaskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
