set search_path to ccs;

alter table vehicle alter column vehicle_id type varchar(32);
alter table session alter column session_id type varchar(32);