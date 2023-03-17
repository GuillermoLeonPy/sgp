/*conectado como user postgre, pass:123456*/
/*
https://www.postgresql.org/docs/9.3/static/database-roles.html
n order to bootstrap the database system, a freshly initialized system always contains one predefined role. This role is always a "superuser", and by default (unless altered when running initdb) it will have the same name as the operating system user that initialized the database cluster. Customarily, this role will be named postgres. In order to create more roles you first have to connect as this initial role.
*/
--tabla pg_user
select * from pg_user

--lista de usuarios y privilegios
select u.usename as "user name", u.usesysid as "user id",
case when u.usesuper and u.usecreatedb then CAST('superuser, create database'
as pg_catalog.text)
when u.usecreatedb then CAST('create database' AS pg_catalog.text)
else CAST('' AS pg_catalog.text) END as "attributes"
from pg_catalog.pg_user u order by 1

--selecciones segun la conexion actual
SELECT current_schema()
SELECT current_database()

--lista de bases de datos
select * from pg_database

--https://www.postgresql.org/docs/9.3/static/app-createuser.html
/*createuser -- define a new PostgreSQL user account
createuser creates a new PostgreSQL user (or more precisely, a role)
createuser is a wrapper around the SQL command CREATE ROLE.
*/
--lista de roles
select * from pg_roles