# Auto Evolutions
# --- !Ups
create sequence seq_lotr_card_id;
create table lotr_card (
  id integer default nextval('seq_lotr_card_id') primary key,
  name varchar(255) not null,
  type_id_01 integer not null,
  type_id_02 integer,
  sphere_id integer not null,
  threat_cost integer,
  will_threat integer,
  attack integer,
  defence integer,
  hitpoint integer,
  card_text varchar(1024) not null,
  flavor_text varchar(1024),
  set_id integer not null,
  number integer not null,
  quantity integer default 3,
  illustrator varchar(128)
);

create table lotr_type (
  id integer primary key,
  name varchar(128)
);

create table lotr_sphere (
  id integer primary key,
  name varchar(128)
);

create table lotr_set (
  id integer primary key,
  name varchar(128),
  seq integer
);

# --- !Downs
drop table lotr_sphere;
drop table lotr_type;
drop table lotr_card;
drop table lotr_set;

drop sequence seq_lotr_card_id;
