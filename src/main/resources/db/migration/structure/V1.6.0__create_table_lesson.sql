CREATE TABLE lesson (

    id              varchar(36) NOT NULL,
    classroom_id    varchar(36) NOT NULL,
    lesson_date     date NOT NULL,

    CONSTRAINT pk_lesson PRIMARY KEY (id),
    CONSTRAINT fk_lesson_classroom FOREIGN KEY (classroom_id) REFERENCES classroom(id)

);