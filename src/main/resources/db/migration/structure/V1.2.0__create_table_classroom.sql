CREATE TABLE classroom (

    id 				varchar(36) NOT NULL,
    name            varchar(32) NOT NULL,

    CONSTRAINT pk_classroom PRIMARY KEY (id),
    CONSTRAINT uk_classroom UNIQUE (name)

);