/*
 * BConfiguration
 */
CREATE TABLE IF NOT EXISTS bconfiguration (
  id int8 NOT NULL , version int4,
  key VARCHAR(255) NOT NULL,
  user_id int8,
  value VARCHAR(255) NOT NULL,
  CONSTRAINT BCONFIGURATION_PK PRIMARY KEY (id)
);

/*
 * BPermission
 */
CREATE TABLE IF NOT EXISTS bpermission (
  id int8 NOT NULL,
  version int4,
  label VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  CONSTRAINT BPERMISSION_PK PRIMARY KEY (id),
  CONSTRAINT BPERMISSION_LABEL_UK UNIQUE (label),
  CONSTRAINT BPERMISSION_NAME_UK UNIQUE (name)
);

/*
 * BRole
 */
CREATE TABLE brole (
  id int8 NOT NULL,
  version int4,
  description VARCHAR(10485760),
  enabled BOOLEAN,
  label VARCHAR(255) NOT NULL,
  CONSTRAINT BROLE_PK PRIMARY KEY (id),
  CONSTRAINT BROLE_LABEL_UK UNIQUE (label)
);

/*
 * BRole - BPermission
 */
CREATE TABLE brole_bpermission (
  brole_id int8 NOT NULL,
  bpermission_id int8 NOT NULL,
  CONSTRAINT BROLE_PERMISSION_PK PRIMARY KEY (brole_id, bpermission_id),
  CONSTRAINT BPERMISSION_BRP_FK FOREIGN KEY (bpermission_id) REFERENCES bpermission,
  CONSTRAINT BROLE_BRP_FK FOREIGN KEY (brole_id) REFERENCES brole
);

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
  password VARCHAR(150) NOT NULL,
  username VARCHAR(255) NOT NULL,
  CONSTRAINT USER_PK PRIMARY KEY (id),
  CONSTRAINT EUSER_EMAIL_UK UNIQUE (email),
  CONSTRAINT EUSER_USERNAME_UK UNIQUE (username)
);

/*
 * EOwnedEntity
 */
CREATE TABLE eowned_entity (
  id int8 NOT NULL,
  version int4,
  description VARCHAR(10485760) NOT NULL,
  name VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  CONSTRAINT EOWNED_ENTITY_PK PRIMARY KEY (id),
  CONSTRAINT EOWNED_ENTITY_USERNAME_UK UNIQUE (username)
);

/**
 * BAuthorization
 */
CREATE TABLE bauthorization (
  entity_id int8 NOT NULL,
  role_id int8 NOT NULL,
  user_id int8 NOT NULL,
  CONSTRAINT BAUTHORIZATION_PK PRIMARY KEY (entity_id, role_id, user_id),
  CONSTRAINT USER_BAUTH_FK FOREIGN KEY (user_id) REFERENCES euser,
  CONSTRAINT EOWNEDENTITY_BAUTH_FK FOREIGN KEY (entity_id) REFERENCES eowned_entity,
  CONSTRAINT BROLE_BAUTH_FK FOREIGN KEY (role_id) REFERENCES brole
);

/**
 * Hibernate sequence
 */
CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;