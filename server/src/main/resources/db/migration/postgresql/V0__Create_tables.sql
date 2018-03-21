/*
 * BConfiguration
 */
CREATE TABLE bconfiguration (
  id int8 NOT NULL , version int4,
  key VARCHAR(255) NOT NULL,
  user_id int8,
  value VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

/*
 * BPermission
 */
CREATE TABLE bpermission (
  id int8 NOT NULL,
  version int4,
  label VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);
ALTER TABLE IF EXISTS bpermission ADD CONSTRAINT UK_BPERMISSION_LABEL UNIQUE (label);
ALTER TABLE IF EXISTS bpermission ADD CONSTRAINT UK_BPERMISSION_NAME UNIQUE (name);

/*
 * BRole
 */
CREATE TABLE brole (
  id int8 NOT NULL,
  version int4,
  description VARCHAR(10485760),
  enabled BOOLEAN,
  label VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);
ALTER TABLE IF EXISTS brole ADD CONSTRAINT UK_BROLE_LABEL UNIQUE (label);

/*
 * BRole - BPermission
 */
CREATE TABLE brole_bpermission (
  brole_id int8 NOT NULL,
  bpermission_id int8 NOT NULL,
  PRIMARY KEY (brole_id, bpermission_id)
);
ALTER TABLE IF EXISTS brole_bpermission ADD CONSTRAINT FK_BPERMISSION_BRP FOREIGN KEY (bpermission_id) REFERENCES bpermission;
ALTER TABLE IF EXISTS brole_bpermission ADD CONSTRAINT FK_BROLE_BRP FOREIGN KEY (brole_id) REFERENCES brole;

/*
 * EUser
 */
CREATE TABLE euser (
  id int8 NOT NULL,
  version int4,
  account_non_expired BOOLEAN,
  account_non_locked BOOLEAN,
  authorities bytea,
  credentials_non_expired BOOLEAN,
  email VARCHAR(254) NOT NULL,
  email_verified BOOLEAN,
  enabled BOOLEAN,
  last_name VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(10485760) NOT NULL,
  username VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);
ALTER TABLE IF EXISTS euser ADD CONSTRAINT UK_EUSER_EMAIL UNIQUE (email);
ALTER TABLE IF EXISTS euser ADD CONSTRAINT UK_EUSER_USERNAME UNIQUE (username);

/*
 * EOwnedEntity
 */
CREATE TABLE eowned_entity (
  id int8 NOT NULL,
  version int4,
  description VARCHAR(10485760) NOT NULL,
  name VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);
ALTER TABLE IF EXISTS eowned_entity ADD CONSTRAINT UK_EOWNED_ENTITY_USERNAME UNIQUE (username);

/**
 * BAuthorization
 */
CREATE TABLE bauthorization (
  entity_id int8 NOT NULL,
  role_id int8 NOT NULL,
  user_id int8 NOT NULL,
  PRIMARY KEY (entity_id, role_id, user_id)
);
ALTER TABLE IF EXISTS bauthorization ADD CONSTRAINT FK_EUSER_BAUTH FOREIGN KEY (user_id) REFERENCES euser;
ALTER TABLE IF EXISTS bauthorization ADD CONSTRAINT FK_EOWNEDENTITY_BAUTH FOREIGN KEY (entity_id) REFERENCES eowned_entity;
ALTER TABLE IF EXISTS bauthorization ADD CONSTRAINT FK_BROLE_BAUTH FOREIGN KEY (role_id) REFERENCES brole;

/**
 * Hibernate sequence
 */
CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;