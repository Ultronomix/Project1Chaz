CREATE TABLE ers_reimbursements(
reimb_id varchar PRIMARY KEY,
amount numeric (6, 2),
submitted timestamp NOT NULL,
resolved timestamp NOT NULL ,
description varchar NOT NULL,
payment_id varchar,
author_id varchar NOT NULL, 
resolved_id varchar,
status_id varchar NOT NULL,
type_id varchar NOT NULL 
);

CREATE TABLE ers_users (
user_id varchar PRIMARY KEY,
username varchar UNIQUE NOT NULL,
email varchar UNIQUE NOT NULL,
password varchar NOT NULL,
given_name varchar NOT NULL,
surname varchar NOT NULL,
is_active boolean,
role_id varchar,
CONSTRAINT FK_role_id FOREIGN KEY (role_id)
REFERENCES ers_user_roles (role_id)
);

CREATE TABLE ers_reimbursement_statuses (
status_id varchar PRIMARY KEY,
status varchar UNIQUE
);

CREATE TABLE ers_reimbursement_types (
type_id varchar PRIMARY KEY,
type_ varchar UNIQUE 
);

CREATE TABLE ers_user_roles (
role_id varchar PRIMARY KEY,
role_ varchar UNIQUE
);


INSERT INTO ers_users(user_id, username, email, "password", given_name, surname, is_active)
VALUES ('ten1918', 'tenten', 'tenten@revature.com', 'ninjaweaponmaster', 'Tenten', 'T', 'TRUE');

INSERT INTO ers_user_roles(role_id, role_)
values('0003', 'EMPLOYEE');

INSERT INTO ers_reimbursement_statuses(status_id, status)
values('99911', 'APPROVED'),
      ('99912', 'DENIED'),
      ('99913', 'PENDING');

SELECT *
FROM ers_user_roles eur;

SELECT *
FROM ers_users eu;


SELECT *
FROM ers_reimbursements er;

SELECT *
FROM ers_reimbursement_statuses ers;

SELECT *
FROM ers_reimbursement_types ert;




UPDATE ers_users 
SET email = 'narutouzumaki@revature.com'
WHERE user_id = 'nar1929';




DROP TABLE ers_reimbursement_types;





ALTER TABLE ers_users 
ADD FOREIGN KEY (role_id)
REFERENCES ers_user_roles(role_id);


ALTER TABLE ers_reimbursements 
ADD FOREIGN KEY (author_id)
REFERENCES ers_users(user_id);

ALTER TABLE ers_reimbursements 
ADD FOREIGN KEY (resolved_id)
REFERENCES ers_users;

ALTER TABLE ers_reimbursements 
ADD FOREIGN KEY (status_id)
REFERENCES ers_reimbursement_statuses(status_id);

ALTER TABLE ers_reimbursements 
ADD FOREIGN KEY (type_id)
REFERENCES ers_reimbursement_types(type_id);



