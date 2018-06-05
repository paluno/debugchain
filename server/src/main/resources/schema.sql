DROP TABLE IF EXISTS project_entity;
DROP TABLE IF EXISTS user_entity;
DROP TABLE IF EXISTS membership_entity;

CREATE TABLE project_entity (
  gitlab_id BIGINT NOT NULL,
  address   VARCHAR(255),
  PRIMARY KEY (gitlab_id)
);

CREATE TABLE user_entity (
  gitlab_id BIGINT NOT NULL,
  address   VARCHAR(255),
  PRIMARY KEY (gitlab_id)
);

CREATE TABLE membership_entity (
  user_gitlab_id    BIGINT NOT NULL,
  project_gitlab_id BIGINT NOT NULL,
  reviewer          BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (project_gitlab_id, user_gitlab_id),
  CONSTRAINT FKsgui4ssyi4795eav4k397hrpd FOREIGN KEY (user_gitlab_id) REFERENCES user_entity,
  CONSTRAINT FKkpwm9uicutofmeml6q8mdk1pd FOREIGN KEY (project_gitlab_id) REFERENCES project_entity
);