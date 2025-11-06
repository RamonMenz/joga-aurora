CREATE TABLE attendance (

    id                  varchar(36) NOT NULL,
    student_id          varchar(36) NOT NULL,
    attendance_date     date NOT NULL,
    status              char(1) NOT NULL,

    CONSTRAINT pk_attendance PRIMARY KEY (id),
    CONSTRAINT fk_attendance_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE

);