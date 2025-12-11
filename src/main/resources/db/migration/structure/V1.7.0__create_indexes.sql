CREATE INDEX idx_student_classroom_id ON student (classroom_id);

CREATE INDEX idx_body_measurement_student_date
    ON body_measurement (student_id, collection_date);

CREATE INDEX idx_physical_test_student_date
    ON physical_test (student_id, collection_date);

CREATE INDEX idx_attendance_student_date
    ON attendance (student_id, attendance_date);
