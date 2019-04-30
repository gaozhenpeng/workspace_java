CREATE DATABASE `ida` CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
use ida;

create table borad
(
  board_id             varchar(256) not null comment 'chinese test, column comment: 看板id',
  primary key (board_id)
) COMMENT='chinese test, table comment: 仪表板';

