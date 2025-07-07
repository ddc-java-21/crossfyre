create sequence game_seq start with 1 increment by 50;
create sequence solution_puzzle_seq start with 1 increment by 50;
create sequence solution_word_seq start with 1 increment by 50;
create sequence user_profile_seq start with 1 increment by 50;
create sequence user_puzzle_seq start with 1 increment by 50;
create sequence user_word_seq start with 1 increment by 50;
create table game
(
    completed      timestamp(6) with time zone not null,
    game_id        bigint                      not null,
    player_id      bigint                      not null unique,
    posted         timestamp(6) with time zone not null,
    user_puzzle_id bigint                      not null unique,
    external_key   uuid                        not null unique,
    primary key (game_id)
);
create table solution_puzzle
(
    size               integer                     not null,
    created            timestamp(6) with time zone not null,
    solution_puzzle_id bigint                      not null,
    external_key       uuid                        not null unique,
    title              varchar(30)                 not null unique,
    board              varchar(255)                not null,
    primary key (solution_puzzle_id)
);
create table solution_word
(
    word_column        integer                     not null,
    word_row           integer                     not null,
    posted             timestamp(6) with time zone not null,
    solution_puzzle_id bigint                      not null,
    solution_word_id   bigint                      not null,
    external_key       uuid                        not null unique,
    clue               varchar(255)                not null unique,
    word_direction     varchar(255)                not null,
    word_name          varchar(255)                not null,
    primary key (solution_word_id)
);
create table user_profile
(
    created         timestamp(6) with time zone not null,
    user_profile_id bigint                      not null,
    external_key    uuid                        not null unique,
    display_name    varchar(30)                 not null unique,
    oauth_key       varchar(30)                 not null unique,
    avatar          varchar(255),
    primary key (user_profile_id)
);
create table user_puzzle
(
    size               integer                     not null,
    created            timestamp(6) with time zone not null,
    solution_puzzle_id bigint                      not null,
    user_puzzle_id     bigint                      not null,
    external_key       uuid                        not null unique,
    title              varchar(30)                 not null unique,
    board              varchar(255)                not null,
    primary key (user_puzzle_id)
);
create table user_word
(
    word_column    integer                     not null,
    word_row       integer                     not null,
    posted         timestamp(6) with time zone not null,
    user_puzzle_id bigint                      not null,
    user_word_id   bigint                      not null,
    external_key   uuid                        not null unique,
    clue           varchar(255)                not null unique,
    word_direction varchar(255)                not null,
    word_name      varchar(255)                not null,
    primary key (user_word_id)
);
alter table if exists game
    add constraint FKe2gg67v12imv2fr2nx0af0usn foreign key (player_id) references user_profile;
alter table if exists game
    add constraint FKggbqyowbcjj60mhpp2td22242 foreign key (user_puzzle_id) references user_puzzle;
alter table if exists solution_word
    add constraint FKp8hcr0hrq4fwjy21h4shggx39 foreign key (solution_puzzle_id) references solution_puzzle;
alter table if exists user_puzzle
    add constraint FKnt5dqf3m5a84px3lidh3mm9gl foreign key (solution_puzzle_id) references solution_puzzle;
alter table if exists user_word
    add constraint FK8mf6onh6mbt9dx24fwopoj2d9 foreign key (user_puzzle_id) references user_puzzle;
