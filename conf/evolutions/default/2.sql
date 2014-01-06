# Auto Evolutions
# --- !Ups
insert into lotr_type values
  (1, 'Hero'),
  (2, 'Ally'),
  (3, 'Attachment'),
  (4, 'Event'),
  (5, 'Enemy'),
  (6, 'Object'),
  (7, 'Treachery')
;

insert into lotr_sphere values
  (1, 'Leadership'),
  (2, 'Tactics'),
  (3, 'Spirit'),
  (4, 'Lore'),
  (5, 'Neutral')
;

insert into lotr_set values
  (1, 'Core', 1)
;

# --- !Downs
delete from lotr_type;
delete from lotr_sphere;