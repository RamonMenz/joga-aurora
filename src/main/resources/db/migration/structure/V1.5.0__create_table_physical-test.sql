CREATE TABLE physical_test (

    id                          varchar(36) NOT NULL,
    student_id                  varchar(36) NOT NULL,
    collection_date             date NOT NULL,
    six_minutes_test            decimal(6,2) NOT NULL,
    six_minutes_reference       char(1) NOT NULL,
    flex_test                   decimal(6,2) NOT NULL,
    flex_reference              char(1) NOT NULL,
    rml_test                    decimal(6,2) NOT NULL,
    rml_reference               char(1) NOT NULL,
    twenty_meters_test          decimal(6,2) NOT NULL,
    twenty_meters_reference     char(1) NOT NULL,
    throw_two_kg_test           decimal(6,2) NOT NULL,
    throw_two_kg_reference      char(1) NOT NULL,

    CONSTRAINT pk_physical_test PRIMARY KEY (id),
    CONSTRAINT fk_physical_test_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE

);