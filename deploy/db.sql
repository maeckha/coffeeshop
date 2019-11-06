#
# Scripts to create database and technicial user
#

# production

CREATE DATABASE coffeeshop_test;

CREATE USER 'coffeeshop_test'@'localhost' IDENTIFIED BY '<PASSWORD>';
GRANT ALL PRIVILEGES ON coffeeshop_test.* TO 'coffeeshop_test'@'localhost';

use coffeeshop_test;


# production

CREATE DATABASE coffeeshop;

CREATE USER 'coffeeshop'@'%' IDENTIFIED BY '<PASSWORD>';
GRANT ALL PRIVILEGES ON coffeeshop.* TO 'coffeeshop'@'%';

use coffeeshop;

#
# DDL
#
create table product (
  id bigint  NOT NULL PRIMARY KEY,
  name VARCHAR(100),
  description VARCHAR (2000),
  price_euro DECIMAL(12,2),
  weight INT
);

create table customer_info
(
  id bigint        auto_increment not null primary key,
  city             varchar(255) null,
  email            varchar(255) null,
  firstname        varchar(255) null,
  iso_country_code varchar(255) null,
  postcode         varchar(255) null,
  street           varchar(255) null,
  surname          varchar(255) null
);

create table order_data
(
  id               bigint auto_increment not null primary key,
  cost_shipping    decimal(19, 4) null,
  amount           decimal(19, 4) null,
  currency         varchar(255)   null,
  order_date       date           null,
  payment_method   varchar(255)   null,
  customer_info_id bigint         null
);

create table order_data_order_items
(
  order_id       bigint not null,
  order_items_id bigint not null
);

create table order_item
(
  id         bigint         auto_increment not null primary key,
  price_euro decimal(19, 4) null,
  product_id bigint         not null,
  quantity   int            not null
);

# scripts to
DROP DATABASE coffeeshop_test;
DROP USER 'coffeeshop_test'@'%' ;