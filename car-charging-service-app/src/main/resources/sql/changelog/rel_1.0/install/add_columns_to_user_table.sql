set search_path to ccs;

alter table "user" add column provider_user_id varchar(32);
alter table "user" add column user_access_key varchar(32);