-- ReminderList
INSERT INTO reminder_list (name, color, icon, display_order, created_at, updated_at) VALUES
('Shopping', '#FF9500', 'cart', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Work', '#007AFF', 'briefcase', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Personal', '#34C759', 'person', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Reminder
INSERT INTO reminder (title, notes, due_date, priority, flagged, completed, completed_at, display_order, list_id, created_at, updated_at) VALUES
('Buy milk', 'Organic whole milk', CURRENT_TIMESTAMP, 'HIGH', false, false, null, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Buy bread', null, DATEADD(DAY, 1, CURRENT_TIMESTAMP), 'NONE', false, false, null, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Buy eggs', '12 pack', null, 'LOW', true, false, null, 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Finish report', 'Q1 quarterly report', DATEADD(DAY, 2, CURRENT_TIMESTAMP), 'HIGH', true, false, null, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Team meeting prep', null, CURRENT_TIMESTAMP, 'MEDIUM', false, false, null, 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Call dentist', 'Schedule cleaning', DATEADD(DAY, 3, CURRENT_TIMESTAMP), 'NONE', false, false, null, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Read book', 'Chapter 5-7', null, 'LOW', false, true, CURRENT_TIMESTAMP, 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Subtask
INSERT INTO subtask (title, completed, display_order, reminder_id, created_at, updated_at) VALUES
('Whole milk', false, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Butter', false, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Gather data', true, 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Write summary', false, 2, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Review with team', false, 3, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
