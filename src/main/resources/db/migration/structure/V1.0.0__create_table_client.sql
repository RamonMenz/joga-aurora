CREATE TABLE client (

	id          varchar(36) NOT NULL,
	name        varchar(256) NOT NULL,
	password    varchar(128) NOT NULL,
	active      boolean NOT NULL,

	CONSTRAINT pk_client PRIMARY KEY (id),
	CONSTRAINT uk_client_name UNIQUE(name)

);