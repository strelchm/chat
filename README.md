# chat
Project for SimbirSoft internship

**Local deployment** 
By default, PostgreSQL is configured, but driver can be changed. Example for creating PostgreSQL DB from psql:
```
postgres=# create database chat;
postgres=# create user chatuser with encrypted password '12345';
postgres=# grant all privileges on database chat to chatuser;
```
