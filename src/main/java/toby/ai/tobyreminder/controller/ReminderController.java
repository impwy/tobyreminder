package toby.ai.tobyreminder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toby.ai.tobyreminder.dto.ReminderRequest;
import toby.ai.tobyreminder.dto.ReminderResponse;
import toby.ai.tobyreminder.dto.SmartListCountResponse;
import toby.ai.tobyreminder.service.ports.in.ReminderService;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @GetMapping
    public ResponseEntity<List<ReminderResponse>> findAll(
            @RequestParam(required = false) Long listId,
            @RequestParam(required = false) Boolean today,
            @RequestParam(required = false) Boolean scheduled,
            @RequestParam(required = false) Boolean flagged,
            @RequestParam(required = false) Boolean completed) {

        List<ReminderResponse> result;

        if (listId != null) {
            result = reminderService.findByListId(listId);
        } else if (Boolean.TRUE.equals(today)) {
            result = reminderService.findToday();
        } else if (Boolean.TRUE.equals(scheduled)) {
            result = reminderService.findScheduled();
        } else if (Boolean.TRUE.equals(flagged)) {
            result = reminderService.findFlagged();
        } else if (Boolean.TRUE.equals(completed)) {
            result = reminderService.findCompleted();
        } else {
            result = reminderService.findAll();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reminderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReminderResponse> create(@RequestBody ReminderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reminderService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponse> update(@PathVariable Long id,
                                                   @RequestBody ReminderRequest request) {
        return ResponseEntity.ok(reminderService.update(id, request));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ReminderResponse> toggleComplete(@PathVariable Long id) {
        return ResponseEntity.ok(reminderService.toggleComplete(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reminderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/counts")
    public ResponseEntity<SmartListCountResponse> getCounts() {
        return ResponseEntity.ok(reminderService.getCounts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReminderResponse>> search(@RequestParam String q) {
        return ResponseEntity.ok(reminderService.search(q));
    }
}
