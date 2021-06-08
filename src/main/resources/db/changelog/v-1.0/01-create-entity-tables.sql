create table message (id uuid not null, room_id uuid not null, user_id uuid not null, text varchar(2048) not null, created timestamp, updated timestamp, primary key (id));
create table room (id uuid not null, name varchar(50) not null, type varchar(255) not null, created timestamp, updated timestamp, primary key (id));
create table user_room (user_id uuid not null, room_id uuid not null, user_room_role varchar(255) not null, blocked_time timestamp, created timestamp, updated timestamp, primary key (room_id, user_id));
create table users (id uuid not null, login varchar(50) not null, password varchar(100) not null,  user_app_role varchar(255) not null, status varchar(255) not null, created timestamp, updated timestamp, primary key (id));

alter table users add constraint UK_ow0gan20590jrb00upg3va2fn unique (login);
alter table room add constraint UK_ow0gan20591jrb00upg3va2fn unique (name);
alter table message add constraint FKl1kg5a2471cv6pkew0gdgjrmo foreign key (room_id) references room;
alter table message add constraint FKpdrb79dg3bgym7pydlf9k3p1n foreign key (user_id) references users;
alter table user_room add constraint FKt69dqc3yclx55jpu6lal8xna8 foreign key (room_id) references room;
alter table user_room add constraint FKyiyqqc4bed6bdmtsjadvmfnq foreign key (user_id) references users;

insert into users values ('00000000-0000-0000-0000-000000000000', 'admin', '$2a$10$tQQzeK4WKAGsz/hFQ/8X6eurXIphyUFwsP0eNAZtXbhOQrsPglYLW', 'ADMIN', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000001', 'client#1', '$2a$10$qIiKHnzomVYeRU4U49CRium25ziMG5ppMzEZKf.PPLfC.zQa6Fkme', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000002', 'client#2', '$2a$10$TMdd2KhDPgjhb6nry87.b.jelYfmaHh/vE30I0LA0O9PDNHOzIju.', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000003', 'client#3', '$2a$10$fFetD2YGamGNCexgK6vLXOCh.7/qBAch28NwJP7tNNaNVCZZW47Mu', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000004', 'client#4', '$2a$10$Uds7fLryNUNjTxQx3QISOeHk4VIEpvulrKuyKchhQA35QeAqSM/gq', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000005', 'client#5', '$2a$10$cEatgB3qYBISVXLftQq4P.rlhkQWRoOgBOSh38..FvePGU1tMAnNG', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000006', 'client#6', '$2a$10$VRuA425To8ANDihyXfjpPOeG3pUR/ScNEmZDb/5WfEcVVntPi9wHm', 'CLIENT', 'GLOBAL_BLOCKED', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000007', 'client#7', '$2a$10$3a30ktGCDN358cxvk0cQlutm98AyLw50sSJmoA6vuM52cM5PcHUyW', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000008', 'client#8', '$2a$10$rzKq5tgLzi8u17RuHKA8V.vUzhEVwr26W1yLy.KObh3He3mFJ.2F6', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000009', 'client#9', '$2a$10$o/Xrdre.podTa/jsz/Z3YOvts6VwEOGfll56kgLuYvAWS2Fktllsq', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000010', 'client#10', '$2a$10$AizCu.Lu018Q0TB8ZklFTuE0CRePc2SH10RCSARa.oJf18fhUkDKq', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000011', 'client#11', '$2a$10$jphet69IEInp1MYbNIBRaeTCyN9ztOa4cq.2loanu2SpDz8IxQUfG', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000012', 'client#12', '$2a$10$e/rQWHX3H54PXxR9NGS0gOFTQWVBMaAVecNaz5s/Heu3/Md7Y7sH2', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000013', 'client#13', '$2a$10$OULJ6mV42TQDuz44hhtsZeTeWyWIVp0GVeZTwbkDUjgzRev95AE8m', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000014', 'client#14', '$2a$10$aQJeMUNceM4sZi3COv3QoOXb5e6tO5SxsD2qK7fLc5bV6LoOaxxGS', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into users values ('00000000-0000-0000-0000-000000000015', 'client#15', '$2a$10$neIiuBpwtcfXGbyYpR9jH.9rtcGrZze/uq1eiNmCoV3v7ZJkMm4qa', 'CLIENT', 'ACTIVE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');

insert into room values ('00000000-0000-0000-0000-000000000001', 'room#1', 'PUBLIC', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into room values ('00000000-0000-0000-0000-000000000002', 'room#2', 'PUBLIC', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into room values ('00000000-0000-0000-0000-000000000003', 'room#3', 'PRIVATE', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into room values ('00000000-0000-0000-0000-000000000004', 'room#4', 'PUBLIC', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into room values ('00000000-0000-0000-0000-000000000005', 'room#5', 'PUBLIC', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');

insert into user_room values ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'OWNER', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into user_room values ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', 'USER', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into user_room values ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000001', 'USER', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into user_room values ('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000001', 'BLOCKED_USER', '2022-01-01 00:00:00.000', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into user_room values ('00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000001', 'MODERATOR', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into user_room values ('00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000001', 'USER', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');

insert into user_room values ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000002', 'OWNER', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into user_room values ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000002', 'USER', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into user_room values ('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000002', 'BLOCKED_USER', '2022-01-01 00:00:00.000', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into user_room values ('00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000002', 'MODERATOR', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');

insert into user_room values ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000003', 'OWNER', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into user_room values ('00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000003', 'MODERATOR', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');

insert into user_room values ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000004', 'OWNER', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into user_room values ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000004', 'MODERATOR', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');

insert into user_room values ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000005', 'OWNER', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into user_room values ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000005', 'MODERATOR', null, '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');

insert into message values ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Hello', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into message values ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000002', 'Hi', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into message values ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000002', 'Test Message', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into message values ('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000003', 'Test Message', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into message values ('00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000003', 'Test Message', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into message values ('00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000004', 'Test Message', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into message values ('00000000-0000-0000-0000-000000000007', '00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000001', 'Test Message', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into message values ('00000000-0000-0000-0000-000000000008', '00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000001', 'Test Message', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');
insert into message values ('00000000-0000-0000-0000-000000000009', '00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000002', 'Test Message', '2021-06-06 00:00:00.000', '2021-06-06 00:00:00.000');