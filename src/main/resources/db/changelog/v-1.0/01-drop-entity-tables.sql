alter table message drop constraint if exists FKl1kg5a2471cv6pkew0gdgjrmo;
alter table message drop constraint if exists FKpdrb79dg3bgym7pydlf9k3p1n;
alter table user_room drop constraint if exists FKt69dqc3yclx55jpu6lal8xna8;
alter table user_room drop constraint if exists FKyiyqqc4bed6bdmtsjadvmfnq;
drop table if exists message cascade;
drop table if exists room cascade;
drop table if exists user_room cascade;
drop table if exists users cascade;