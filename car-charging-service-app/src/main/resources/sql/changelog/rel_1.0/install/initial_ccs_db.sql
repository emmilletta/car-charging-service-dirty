create schema if not exists ccs;

set search_path to ccs;

create table "user"
(
    id                  bigint NOT NULL   DEFAULT NEXTVAL('ccs_user_sqc'),
    user_id             varchar(32) NOT NULL,
    email               varchar(32) NOT NULL,
    registration_date   date NOT NULL,
    status              varchar(32) NOT NULL,
    CONSTRAINT pk_user_id PRIMARY KEY (id),
    CONSTRAINT user_status CHECK (status = 'ACTIVE' OR status = 'INACTIVE')
);

create table vehicle
(
    id          bigint NOT NULL   DEFAULT NEXTVAL('ccs_vehicle_sqc'),
    user_id     integer NOT NULL,
    vehicle_id  integer NOT NULL,
    vendor      varchar(32) NOT NULL,
    CONSTRAINT pk_vehicle_id PRIMARY KEY (id),
    CONSTRAINT fk_vehicle_user_id FOREIGN KEY (user_id) REFERENCES "user" (id),
    CONSTRAINT vehicle_vendor CHECK (vendor = 'BMW' OR vendor = 'MERCEDES')
);

create table session
(
    id          bigint NOT NULL   DEFAULT NEXTVAL('ccs_session_sqc'),
    session_id  bigint NOT NULL,
    status      varchar(32) NOT NULL,
    provider    varchar(32) NOT NULL,
    country     varchar(32),
    user_id     integer NOT NULL,
    vehicle_id  integer NOT NULL,
    start_time  date,
    stop_time   date,
    price       bigint,
    currency    varchar(32),
    CONSTRAINT pk_sessions_id PRIMARY KEY (id),
    CONSTRAINT fk_sessions_user_id FOREIGN KEY (user_id) REFERENCES "user" (id),
    CONSTRAINT fk_sessions_vehicle_id FOREIGN KEY (vehicle_id) REFERENCES vehicle (id),
    CONSTRAINT session_status CHECK (status = 'ACTIVE' OR status = 'INACTIVE'),
    CONSTRAINT session_provider CHECK (provider = 'EUROPE' OR provider = 'ASIA' OR provider = 'JAPAN')
);

CREATE SEQUENCE ccs_user_sqc INCREMENT 1 START 1;
CREATE SEQUENCE ccs_vehicle_sqc INCREMENT 1 START 1;
CREATE SEQUENCE ccs_session_sqc INCREMENT 1 START 1;