--conectado como postgre, pass:123456
select current_role

/*https://www.postgresql.org/docs/9.3/static/sql-createrole.html
A role is an entity that can own database objects and have database privileges; a role can be considered a "user", a "group", or both depending on how it is used. Refer to Chapter 20 and Chapter 19 for information about managing users and authentication. You must have CREATEROLE privilege or be a database superuser to use this command.
*/

create role sgp with
CREATEDB
CREATEROLE
INHERIT
LOGIN
ENCRYPTED PASSWORD 'sgp'

/**https://www.postgresql.org/docs/9.3/static/manage-ag-tablespaces.html*/

select * from pg_tablespace


create TABLESPACE sgp_tablespace
OWNER sgp
LOCATION 'C:\Archivos de programa\PostgreSQL\9.3\data'

/*https://www.postgresql.org/docs/9.3/static/sql-createdatabase.html*/

create DATABASE database_sgp WITH
OWNER sgp
TEMPLATE template0
TABLESPACE sgp_tablespace

/*https://www.postgresql.org/docs/9.3/static/sql-createschema.html
CREATE SCHEMA enters a new schema into the current database.
*/
--DESCONECTAR SESION postgre, iniciar sesion sgp
--inciada sesion con postgre
select current_role
SELECT current_schema()--retorna public
SELECT current_database()

--CREATE SCHEMA schema_name [ AUTHORIZATION user_name ]
--if the AUTHORIZATION clause is used, all the created objects will be owned by that user.

CREATE SCHEMA schema_sgp AUTHORIZATION sgp

SHOW search_path--retorna: "$user",public

--objetivo: hacer que el esquema por defecto sea schema_sgp
--desconectar sesiones abiertas
--detener el servicio
--arbrir archivo:C:\Archivos de programa\PostgreSQL\9.3\data\postgresql.conf
--buscar cadena: CLIENT CONNECTION DEFAULTS
--identificar linea: #search_path = '"$user",public'		# schema names
--https://www.postgresql.org/docs/current/static/runtime-config-client.html
--modificar la linea: search_path = '"$user",schema_sgp,public'		# schema names
--guardar archivo
--reiniciar servicio
--iniciar sesion sgp
SELECT current_schema()--retorna:schema_sgp
SHOW search_path--retorna:"$user",schema_sgp,public