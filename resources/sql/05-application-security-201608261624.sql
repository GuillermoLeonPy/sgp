/*application security*/

create table app_sec_program(
id	   		 		   bigint,
program_key		 		   varchar(500));

--ALTER TABLE app_sec_program DROP program_key;
ALTER TABLE app_sec_program ADD program_key		 		   varchar(500) not null;
ALTER TABLE app_sec_program ADD program_key		 		   varchar(500) not null;
ALTER TABLE app_sec_program ADD CONSTRAINT app_sec_program_id_pk PRIMARY KEY (id);
ALTER TABLE app_sec_program ADD CONSTRAINT app_sec_program_program_key_uk UNIQUE (program_key);

create sequence app_sec_program_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by app_sec_program.id;


create or replace function f_app_sec_program_id_sq
(pid	   		 		   bigint)
returns bigint as
$BODY$
declare
	vsn	bigint;
begin
	select nextval('app_sec_program_id_sq') into vsn;
	return vsn;
end;
$BODY$
LANGUAGE 'plpgsql';

--select f_app_sec_program_id_sq(1)

create table app_sec_rol(
id	   		 		   bigint,
role_name		 		   varchar(500) NOT NULL,
role_description 		   varchar(500) NOT NULL,
is_editable				varchar(1) NOT NULL);

ALTER TABLE app_sec_rol ADD CONSTRAINT app_sec_rol_id_pk PRIMARY KEY (id);
ALTER TABLE app_sec_rol ADD CONSTRAINT app_sec_rol_role_name_uk UNIQUE (role_name);

ALTER TABLE app_sec_rol ADD CHECK (is_editable in ('S', 'N'));

create sequence app_sec_rol_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by app_sec_rol.id;

create or replace function f_app_sec_rol_id_sq
(pid	   		 		   bigint)
returns bigint as
$BODY$
declare
	vsn	bigint;
begin
	select nextval('app_sec_rol_id_sq') into vsn;
	return vsn;
end;
$BODY$
LANGUAGE 'plpgsql';


--drop table app_sec_rol_program
create table app_sec_rol_program(
id	   		 		   bigint,
id_app_sec_rol	   		 		   bigint NOT NULL,
id_app_sec_program	   		 		   bigint NOT NULL,
is_editable				varchar(1) NOT NULL);

ALTER TABLE app_sec_rol_program ADD CHECK (is_editable in ('S', 'N'));
ALTER TABLE app_sec_rol_program ADD CONSTRAINT app_sec_rol_program_uk UNIQUE (id_app_sec_rol,id_app_sec_program);

ALTER TABLE app_sec_rol_program 
ADD constraint app_sec_rol_program_fk_01
FOREIGN KEY (id_app_sec_rol) REFERENCES app_sec_rol (id);

ALTER TABLE app_sec_rol_program 
ADD constraint app_sec_rol_program_fk_02
FOREIGN KEY (id_app_sec_program) REFERENCES app_sec_program (id);

create sequence app_sec_rol_program_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by app_sec_rol_program.id;

create or replace function f_app_sec_rol_program_id_sq
(pid	   		 		   bigint)
returns bigint as
$BODY$
declare
	vsn	bigint;
begin
	select nextval('app_sec_rol_program_id_sq') into vsn;
	return vsn;
end;
$BODY$
LANGUAGE 'plpgsql';


--drop table app_sec_person_role
create table app_sec_person_role(
id	   		 		   bigint,
id_app_sec_rol	   		 		   bigint NOT NULL,
id_person	   		 		   bigint NOT NULL,
is_editable				varchar(1) NOT NULL);

ALTER TABLE app_sec_person_role ADD CHECK (is_editable in ('S', 'N'));
ALTER TABLE app_sec_person_role ADD CONSTRAINT app_sec_person_role_id_person_uk UNIQUE (id_person);

ALTER TABLE app_sec_person_role 
ADD constraint app_sec_person_role_fk_01
FOREIGN KEY (id_person) REFERENCES pms_person (id);

ALTER TABLE app_sec_person_role 
ADD constraint app_sec_person_role_fk_02
FOREIGN KEY (id_app_sec_rol) REFERENCES app_sec_rol (id);

create sequence app_sec_person_role_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by app_sec_person_role.id;

create or replace function f_app_sec_person_role_id_sq
(pid	   		 		   bigint)
returns bigint as
$BODY$
declare
	vsn	bigint;
begin
	select nextval('app_sec_person_role_id_sq') into vsn;
	return vsn;
end;
$BODY$
LANGUAGE 'plpgsql';

ALTER table pms_person ADD app_user_name	   varchar(20);
ALTER table pms_person ADD app_user_passwd	   varchar(10);
ALTER table pms_person ADD app_user_is_editable	   varchar(1);
ALTER table pms_person ADD app_user_allow_user	   varchar(1);

ALTER TABLE pms_person ADD CHECK (app_user_is_editable in ('S', 'N'));
ALTER TABLE pms_person ADD CHECK (app_user_allow_user in ('S', 'N'));
