# Auto Evolutions
# --- !Ups
create sequence seq_card_id;
create sequence seq_cardtype_id;

create table card (
  id integer default nextval('seq_card_id') primary key,
  name varchar(255) not null,
  card_text varchar(2048),
  card_set varchar(10) not null,
  card_number integer not null,
  card_type_id integer
);

/* DEFINE CARD-SET */
create table cardset (
  card_set varchar(10) primary key,
  card_set_name varchar(255) not null,
  seq number not null
);

/* DEFINE CARD TYPE */
create table cardtype (
  id integer default nextval('seq_cardtype_id') primary key,
  name varchar(255) not null,
  seq number not null
)

# --- !Downs
drop table cardtype;
drop table cardset;
drop table card;

drop sequence seq_cardtype_id;
drop sequence seq_card_id;

