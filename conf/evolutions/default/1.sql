# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table campaign (
  id                        bigint auto_increment not null,
  message                   varchar(255),
  retailer_id               bigint,
  constraint pk_campaign primary key (id))
;

create table category (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_category primary key (id))
;

create table customer (
  id                        bigint auto_increment not null,
  qist_no                   varchar(255),
  last_active               datetime,
  constraint pk_customer primary key (id))
;

create table customer_session (
  id                        bigint auto_increment not null,
  start                     datetime,
  end                       datetime,
  store_id                  bigint,
  customer_id               bigint,
  constraint pk_customer_session primary key (id))
;

create table product (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  created_time              datetime,
  mfr_sku                   varchar(255),
  store_sku                 varchar(255),
  qr_code                   varchar(255),
  qist_no                   varchar(255),
  status                    varchar(255),
  retailer_id               bigint,
  constraint pk_product primary key (id))
;

create table qist_product (
  id                        bigint auto_increment not null,
  start                     datetime,
  end                       datetime,
  qist_price                double,
  product_id                bigint,
  constraint pk_qist_product primary key (id))
;

create table retailer (
  id                        bigint auto_increment not null,
  company_name              varchar(255),
  username                  varchar(255),
  lcoation                  varchar(255),
  qist_no                   varchar(255),
  status                    varchar(255),
  create_time               datetime,
  approved_date             datetime,
  last_active               datetime,
  staff_id                  bigint,
  constraint pk_retailer primary key (id))
;

create table session_product (
  id                        bigint auto_increment not null,
  status                    varchar(255),
  purchased                 tinyint(1) default 0,
  session_id                bigint,
  product_id                bigint,
  constraint pk_session_product primary key (id))
;

create table staff (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  staff_type                integer,
  constraint ck_staff_staff_type check (staff_type in (0,1)),
  constraint pk_staff primary key (id))
;

create table sub_category (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  category_id               bigint,
  constraint pk_sub_category primary key (id))
;


create table customer_product (
  customer_id                    bigint not null,
  product_id                     bigint not null,
  constraint pk_customer_product primary key (customer_id, product_id))
;

create table customer_campaign (
  customer_id                    bigint not null,
  campaign_id                    bigint not null,
  constraint pk_customer_campaign primary key (customer_id, campaign_id))
;

create table product_category (
  product_id                     bigint not null,
  category_id                    bigint not null,
  constraint pk_product_category primary key (product_id, category_id))
;

create table product_sub_category (
  product_id                     bigint not null,
  sub_category_id                bigint not null,
  constraint pk_product_sub_category primary key (product_id, sub_category_id))
;
alter table campaign add constraint fk_campaign_retailer_1 foreign key (retailer_id) references retailer (id) on delete restrict on update restrict;
create index ix_campaign_retailer_1 on campaign (retailer_id);
alter table customer_session add constraint fk_customer_session_store_2 foreign key (store_id) references retailer (id) on delete restrict on update restrict;
create index ix_customer_session_store_2 on customer_session (store_id);
alter table customer_session add constraint fk_customer_session_customer_3 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_customer_session_customer_3 on customer_session (customer_id);
alter table product add constraint fk_product_retailer_4 foreign key (retailer_id) references retailer (id) on delete restrict on update restrict;
create index ix_product_retailer_4 on product (retailer_id);
alter table qist_product add constraint fk_qist_product_product_5 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_qist_product_product_5 on qist_product (product_id);
alter table retailer add constraint fk_retailer_staff_6 foreign key (staff_id) references staff (id) on delete restrict on update restrict;
create index ix_retailer_staff_6 on retailer (staff_id);
alter table session_product add constraint fk_session_product_session_7 foreign key (session_id) references customer_session (id) on delete restrict on update restrict;
create index ix_session_product_session_7 on session_product (session_id);
alter table session_product add constraint fk_session_product_product_8 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_session_product_product_8 on session_product (product_id);
alter table sub_category add constraint fk_sub_category_category_9 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_sub_category_category_9 on sub_category (category_id);



alter table customer_product add constraint fk_customer_product_customer_01 foreign key (customer_id) references customer (id) on delete restrict on update restrict;

alter table customer_product add constraint fk_customer_product_product_02 foreign key (product_id) references product (id) on delete restrict on update restrict;

alter table customer_campaign add constraint fk_customer_campaign_customer_01 foreign key (customer_id) references customer (id) on delete restrict on update restrict;

alter table customer_campaign add constraint fk_customer_campaign_campaign_02 foreign key (campaign_id) references campaign (id) on delete restrict on update restrict;

alter table product_category add constraint fk_product_category_product_01 foreign key (product_id) references product (id) on delete restrict on update restrict;

alter table product_category add constraint fk_product_category_category_02 foreign key (category_id) references category (id) on delete restrict on update restrict;

alter table product_sub_category add constraint fk_product_sub_category_product_01 foreign key (product_id) references product (id) on delete restrict on update restrict;

alter table product_sub_category add constraint fk_product_sub_category_sub_category_02 foreign key (sub_category_id) references sub_category (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table campaign;

drop table customer_campaign;

drop table category;

drop table product_category;

drop table customer;

drop table customer_product;

drop table customer_session;

drop table product;

drop table product_sub_category;

drop table qist_product;

drop table retailer;

drop table session_product;

drop table staff;

drop table sub_category;

SET FOREIGN_KEY_CHECKS=1;

