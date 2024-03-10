--liquibase formatted sql

-- создание таблицы с чатами

create table if not exists chat
(
	id serial,
	tg_id bigint,
	primary key (id),
	unique (tg_id)
);

-- создание таблицы со ссылками

create table if not exists link
(
	id serial,
	url varchar(2048),
	last_update timestamp with time zone not null,
	last_check timestamp with time zone not null,
	primary key (id),
	unique (url)
);

-- создание таблицы с подписками чатов на обновления ссылок

create table if not exists subscription
(
	chat_id bigint,
	link_id bigint,
	primary key (chat_id, link_id),
	foreign key (chat_id) references chat(id),
	foreign key (link_id) references link(id)
);
