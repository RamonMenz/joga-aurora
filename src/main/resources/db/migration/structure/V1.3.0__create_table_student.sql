CREATE TABLE student (

    id 				varchar(36) NOT NULL,
    name            varchar(256) NOT NULL,
    birth_date      date NOT NULL,
    gender          char(1) NOT NULL,
    classroom_id    varchar(36) NOT NULL,

    CONSTRAINT pk_student PRIMARY KEY (id),
    CONSTRAINT fk_student_classroom FOREIGN KEY (classroom_id) REFERENCES classroom(id)

);