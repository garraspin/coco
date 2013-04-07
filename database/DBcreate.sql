CREATE SEQUENCE "sq_objective_functions" START 1 INCREMENT 1;

CREATE TABLE objective_functions (
       id_function          INTEGER NOT NULL DEFAULT nextval('sq_objective_functions'),
       function_name        TEXT NOT NULL
);

ALTER TABLE objective_functions
       ADD PRIMARY KEY (id_function);


CREATE SEQUENCE "sq_rank_rules" START 1 INCREMENT 1;

CREATE TABLE rank_rules (
       id_rule              INTEGER NOT NULL DEFAULT nextval('sq_rank_rules'),
       rule_name            TEXT NOT NULL
);

ALTER TABLE rank_rules
       ADD PRIMARY KEY (id_rule);


CREATE SEQUENCE "sq_attributes" START 1 INCREMENT 1;

CREATE TABLE attributes (
       id_attribute         INTEGER NOT NULL DEFAULT nextval('sq_attributes'),
       id_rule              INTEGER NOT NULL,
       optima               REAL NOT NULL,
       attribute_name       TEXT NOT NULL
);

ALTER TABLE attributes
       ADD PRIMARY KEY (id_attribute);


CREATE SEQUENCE "sq_users" START 1 INCREMENT 1;

CREATE TABLE Users (
       id_user              INTEGER NOT NULL DEFAULT nextval('sq_users'),
       user_name            TEXT NOT NULL,
       user_surname         TEXT NOT NULL,
       password             TEXT NOT NULL,
       email                TEXT NOT NULL,
       last_login           DATE NOT NULL DEFAULT now()
);

ALTER TABLE Users
       ADD PRIMARY KEY (id_user);


CREATE SEQUENCE "sq_coco_problems" START 1 INCREMENT 1;

CREATE TABLE coco_problems (
       id_coco              INTEGER NOT NULL  DEFAULT nextval('sq_coco_problems'),
       id_user              INTEGER NOT NULL,
       id_function          INTEGER NOT NULL,
       coco_name            TEXT NOT NULL,
       coco_solution        REAL NOT NULL,
       coco_description     TEXT NOT NULL,
       negative_allowed     BOOLEAN NULL DEFAULT true,
       equilibrium          REAL NOT NULL,
       y_name               TEXT NOT NULL
);

ALTER TABLE coco_problems
       ADD PRIMARY KEY (id_coco);


CREATE SEQUENCE "sq_elements" START 1 INCREMENT 1;

CREATE TABLE elements (
       id_coco              INTEGER NOT NULL,
       id_element           INTEGER NOT NULL DEFAULT nextval('sq_elements'),
       element_name         TEXT NOT NULL,
       y_value              REAL NOT NULL
);

ALTER TABLE elements
       ADD PRIMARY KEY (id_element);


CREATE TABLE elements_attributes (
       id_element           INTEGER NOT NULL,
       id_attribute         INTEGER NOT NULL,
       value                REAL NULL,
       ideal_value          REAL NULL,
       ranking              INTEGER NULL
);

ALTER TABLE elements_attributes
       ADD PRIMARY KEY (id_element, id_attribute);

ALTER TABLE attributes
       ADD FOREIGN KEY (id_rule)
       REFERENCES rank_rules;


ALTER TABLE coco_problems
       ADD FOREIGN KEY (id_function)
       REFERENCES objective_functions;

ALTER TABLE coco_problems
       ADD FOREIGN KEY (id_user)
       REFERENCES Users;


ALTER TABLE elements
       ADD FOREIGN KEY (id_coco)
       REFERENCES coco_problems;


ALTER TABLE elements_attributes
       ADD FOREIGN KEY (id_attribute)
       REFERENCES attributes;


ALTER TABLE elements_attributes
       ADD FOREIGN KEY (id_element)
       REFERENCES elements;
