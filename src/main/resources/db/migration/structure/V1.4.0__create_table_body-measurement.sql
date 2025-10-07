CREATE TABLE body_measurement (

    id                              varchar(36) NOT NULL,
    student_id                      varchar(36) NOT NULL,
    collection_date                 date NOT NULL,
    waist                           decimal(5,2) NOT NULL,
    weight                          decimal(5,2) NOT NULL,
    height                          integer NOT NULL,
    bmi                             decimal(5,2) NOT NULL,
    bmi_reference                   char(1) NOT NULL,
    waist_height_ratio              decimal(5,2) NOT NULL,
    waist_height_ratio_reference    char(1) NOT NULL,

    CONSTRAINT pk_body_measurement PRIMARY KEY (id),
    CONSTRAINT fk_body_measurement_student FOREIGN KEY (student_id) REFERENCES student(id)

);