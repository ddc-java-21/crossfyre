create sequence guess_seq start with 1 increment by 50;
create sequence puzzle_seq start with 1 increment by 50;
create sequence puzzle_word_seq start with 1 increment by 50;
create sequence user_puzzle_seq start with 1 increment by 50;
create sequence user_seq start with 1 increment by 50;
create table guess
(
    guess_char     char(1) not null,
    guess_column   integer not null,
    guess_row      integer not null,
    guess_id       bigint  not null,
    user_puzzle_id bigint  not null,
    primary key (guess_id)
);
create table puzzle
(
    size         integer                     not null,
    created      timestamp(6) with time zone not null,
    puzzle_id    bigint                      not null,
    title        varchar(15)                 not null unique,
    external_key uuid                        not null unique,
    board        varchar(255)                not null,
    primary key (puzzle_id)
);
create table puzzle_word
(
    word_column    integer      not null,
    word_row       integer      not null,
    puzzle_id      bigint       not null,
    puzzle_word_id bigint       not null,
    clue           varchar(255) not null unique,
    word_direction varchar(255) not null,
    word_name      varchar(255) not null unique,
    primary key (puzzle_word_id)
);
create table user
(
    created      timestamp(6) with time zone not null,
    user_id      bigint                      not null,
    external_key uuid                        not null unique,
    display_name varchar(20)                 not null,
    oauth_key    varchar(30)                 not null unique,
    avatar       varchar(255),
    primary key (user_id)
);
create table user_puzzle
(
    created        timestamp(6) with time zone not null,
    puzzle_id      bigint                      not null,
    solved         timestamp(6) with time zone not null,
    user_id        bigint                      not null,
    user_puzzle_id bigint                      not null,
    external_key   uuid                        not null unique,
    primary key (user_puzzle_id)
);
alter table if exists guess
    add constraint FKmayfx7rfqnos60y7jtvgx0u3s foreign key (user_puzzle_id) references user_puzzle;
alter table if exists puzzle_word
    add constraint FK6qqef7f12yg9ksowywhd4qou0 foreign key (puzzle_id) references puzzle;
alter table if exists user_puzzle
    add constraint FK8o0f33pxc4w5arng3v9ld02mn foreign key (puzzle_id) references puzzle;
alter table if exists user_puzzle
    add constraint FKi9qppcfilrktf0oy1qmpuxbtp foreign key (user_id) references user;
