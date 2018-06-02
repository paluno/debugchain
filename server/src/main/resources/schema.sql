DROP TABLE IF EXISTS project_entity;
DROP TABLE IF EXISTS reviewer_entity;
DROP TABLE IF EXISTS project_entity_reviewers;

CREATE TABLE project_entity (
  gitlab_id   int8 NOT NULL,
  address     varchar(255),
  PRIMARY KEY (gitlab_id)
);

CREATE TABLE reviewer_entity (
  gitlab_id int8 NOT NULL,
  address varchar(255),
  PRIMARY KEY (gitlab_id)
);

CREATE TABLE project_entity_reviewers (
  project_entity_gitlab_id  int8 NOT NULL,
  reviewers_gitlab_id       int8 NOT NULL UNIQUE,
  CONSTRAINT FKntw6r94uxh2vuo0orns3vc663 FOREIGN KEY (reviewers_gitlab_id) REFERENCES reviewer_entity,
  CONSTRAINT FKna6on3c3bq3xs6spovgyq3wpo FOREIGN KEY (project_entity_gitlab_id) REFERENCES project_entity
);