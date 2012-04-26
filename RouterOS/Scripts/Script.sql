--<ScriptOptions statementTerminator=";"/>

CREATE TABLE stats (
	id BIGINT NOT NULL,
	millis BIGINT NOT NULL,
	rxbytes BIGINT NOT NULL,
	txbytes BIGINT NOT NULL,
	created TIMESTAMP DEFAULT 'CURRENT_TIMESTAMP' NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX created ON stats (created ASC);

CREATE UNIQUE INDEX millis ON stats (millis ASC);

