
create database rdo;

use rdo;

create table table_of_ints (integer_value int);

insert into table_of_ints values (2);
insert into table_of_ints values (3);

select * from table_of_ints;

create table NUMBERS (
  IntegerValue          int not null,
  IntegerValueNullable  int null,
  LongValue             bigint not null,
  LongValueNullable     bigint null,
  float_value           float not null,
  float_value_nullable  float null,
  double_value          double not null,
  double_value_nullable double null
);

insert into NUMBERS (
  IntegerValue, IntegerValueNullable, LongValue, LongValueNullable,
  float_value, float_value_nullable, double_value, double_value_nullable)
  values (1001, 1002, 2001, 2002, 1.001, 1.002, 2.001, 2.002);

insert into NUMBERS (
  IntegerValue, IntegerValueNullable, LongValue, LongValueNullable,
  float_value, float_value_nullable, double_value, double_value_nullable)
  values (1003, null, 2003, null, 1.003, null, 2.003, null);

select * from numbers;

