create sequence guess_seq start with 1 increment by 50;
create sequence puzzle_seq start with 1 increment by 50;
create sequence puzzle_word_seq start with 1 increment by 50;
create sequence user_profile_seq start with 1 increment by 50;
create sequence user_puzzle_seq start with 1 increment by 50;
create table guess
(
    guess_char     char(1)                     not null,
    guess_column   integer                     not null,
    guess_row      integer                     not null,
    created        timestamp(6) with time zone not null,
    guess_id       bigint                      not null,
    user_puzzle_id bigint                      not null,
    external_key   uuid                        not null unique,
    primary key (guess_id)
);
create table puzzle
(
    size         integer                                                                                 not null,
    created      timestamp(6) with time zone                                                             not null,
    date         timestamp(6) with time zone,
    puzzle_id    bigint                                                                                  not null,
    external_key uuid                                                                                    not null unique,
    board        enum ('FAKEDAY','FRIDAY','MONDAY','SATURDAY','SUNDAY','THURSDAY','TUESDAY','WEDNESDAY') not null,
    primary key (puzzle_id)
);
create table puzzle_word
(
    word_column    integer                not null,
    word_length    integer                not null,
    word_row       integer                not null,
    puzzle_id      bigint                 not null,
    puzzle_word_id bigint                 not null,
    external_key   uuid                   not null unique,
    clue           varchar(255)           not null,
    word_name      varchar(255)           not null,
    word_direction enum ('ACROSS','DOWN') not null,
    primary key (puzzle_word_id),
    unique (puzzle_id, word_name)
);
create table user_profile
(
    created         timestamp(6) with time zone not null,
    user_profile_id bigint                      not null,
    external_key    uuid                        not null unique,
    display_name    varchar(20)                 not null,
    oauth_key       varchar(30)                 not null unique,
    avatar          varchar(255),
    primary key (user_profile_id)
);
create table user_puzzle
(
    is_solved      boolean                     not null,
    created        timestamp(6) with time zone not null,
    puzzle_id      bigint                      not null,
    solved         timestamp(6) with time zone,
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
    add constraint FK13w92u5k5tojqmgbbwyuw3ewm foreign key (user_id) references user_profile;
