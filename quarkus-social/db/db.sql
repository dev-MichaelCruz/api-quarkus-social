CREATE DATABASE quarkus_social;

CREATE TABLE USERS (
	id bigserial not null primary key,
	name varchar(100) not null,
	age integer not null
);

CREATE TABLE POSTS (
    id bigserial not null primary key,
    post_text text not null,
    dateTime timestamp,
    user_id bigint not null references USERS(id)
);

CREATE TABLE FOLLOWERS (
	id bigserial not null primary key,
	user_id bigint not null references USERS(id),
	follower_id bigint not null references USERS(id)
);