create table message (id uuid not null, room_id uuid, user_id uuid, text varchar(2048), created timestamp, updated timestamp, primary key (id));
create table room (id uuid not null, name varchar(50) not null, type varchar(255) not null, created timestamp, updated timestamp, primary key (id));
create table user_room (user_id uuid not null, room_id uuid not null, user_room_role varchar(255), blocked_time timestamp, created timestamp, updated timestamp, primary key (room_id, user_id));
create table users (id uuid not null, login varchar(50), password varchar(100),  user_app_role varchar(255), status varchar(255), created timestamp, updated timestamp, primary key (id));
alter table users add constraint UK_ow0gan20590jrb00upg3va2fn unique (login);
alter table room add constraint UK_ow0gan20591jrb00upg3va2fn unique (name);
alter table message add constraint FKl1kg5a2471cv6pkew0gdgjrmo foreign key (room_id) references room;
alter table message add constraint FKpdrb79dg3bgym7pydlf9k3p1n foreign key (user_id) references users;
alter table user_room add constraint FKt69dqc3yclx55jpu6lal8xna8 foreign key (room_id) references room;
alter table user_room add constraint FKyiyqqc4bed6bdmtsjadvmfnq foreign key (user_id) references users;
insert into users values ('00000000-0000-0000-0000-000000000001', 'admin', '$2a$10$tQQzeK4WKAGsz/hFQ/8X6eurXIphyUFwsP0eNAZtXbhOQrsPglYLW', 'ADMIN', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000002', 'client', '$2a$10$dHDEHGFAou1LoRlw83aAi.QCoNpDOPu.53q2cYf4sfcB5PxihDv/m', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into room values ('00000000-0000-0000-0000-000000000001', 'room#1', 'PUBLIC', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into user_room values ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', 'OWNER', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
