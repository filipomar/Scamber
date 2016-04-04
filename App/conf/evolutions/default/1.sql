# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table eitem (
  id                        bigint auto_increment not null,
  last_updated_on           datetime not null,
  created_on                datetime not null,
  name                      varchar(255) not null,
  description               TINYTEXT not null,
  owner                     varchar(255),
  category                  varchar(255),
  photo_one                 varchar(255) not null,
  photo_two                 varchar(255),
  photo_three               varchar(255),
  photo_four                varchar(255),
  photo_five                varchar(255),
  constraint pk_eitem primary key (id))
;

create table eitem_category (
  internal_name             varchar(255) not null,
  last_updated_on           datetime not null,
  created_on                datetime not null,
  constraint pk_eitem_category primary key (internal_name))
;

create table eitem_view (
  id                        bigint auto_increment not null,
  last_updated_on           datetime not null,
  created_on                datetime not null,
  viewer                    varchar(255),
  viewed                    bigint,
  liked                     tinyint(1) default 0,
  constraint pk_eitem_view primary key (id))
;

create table emessage (
  id                        bigint auto_increment not null,
  last_updated_on           datetime not null,
  created_on                datetime not null,
  content                   TINYTEXT not null,
  from_user                 varchar(255),
  to_user                   varchar(255),
  constraint pk_emessage primary key (id))
;

create table euser (
  account                   varchar(255) not null,
  last_updated_on           datetime not null,
  created_on                datetime not null,
  email                     varchar(255) not null,
  password                  varchar(255) not null,
  full_name                 varchar(255) not null,
  picture                   varchar(255) not null,
  constraint uq_euser_email unique (email),
  constraint uq_euser_picture unique (picture),
  constraint pk_euser primary key (account))
;

alter table eitem add constraint fk_eitem_owner_1 foreign key (owner) references euser (account) on delete restrict on update restrict;
create index ix_eitem_owner_1 on eitem (owner);
alter table eitem add constraint fk_eitem_category_2 foreign key (category) references eitem_category (internal_name) on delete restrict on update restrict;
create index ix_eitem_category_2 on eitem (category);
alter table eitem_view add constraint fk_eitem_view_viewer_3 foreign key (viewer) references euser (account) on delete restrict on update restrict;
create index ix_eitem_view_viewer_3 on eitem_view (viewer);
alter table eitem_view add constraint fk_eitem_view_viewed_4 foreign key (viewed) references eitem (id) on delete restrict on update restrict;
create index ix_eitem_view_viewed_4 on eitem_view (viewed);
alter table emessage add constraint fk_emessage_from_5 foreign key (from_user) references euser (account) on delete restrict on update restrict;
create index ix_emessage_from_5 on emessage (from_user);
alter table emessage add constraint fk_emessage_to_6 foreign key (to_user) references euser (account) on delete restrict on update restrict;
create index ix_emessage_to_6 on emessage (to_user);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table eitem;

drop table eitem_category;

drop table eitem_view;

drop table emessage;

drop table euser;

SET FOREIGN_KEY_CHECKS=1;

