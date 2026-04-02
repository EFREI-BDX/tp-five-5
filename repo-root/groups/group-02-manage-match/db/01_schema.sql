drop table if exists match  cascade;
drop table if exists team   cascade;
drop table if exists field  cascade;

drop type if exists match_state_enum cascade;
drop type if exists team_state_enum  cascade;

create type match_state_enum as enum (
    'not_started',
    'in_progress',
    'finished',
    'cancelled'
);

create type team_state_enum as enum (
    'incomplete',
    'complete',
    'dissoute'
);

create table field (
    id    uuid         primary key default gen_random_uuid(),
    label varchar(255) not null,

    constraint field_label_not_empty check (label <> '')
);

create table team (
    id         uuid            primary key default gen_random_uuid(),
    tag        varchar(3)      not null unique,
    label      varchar(255)    not null,
    team_state team_state_enum not null default 'incomplete',

    constraint team_tag_not_empty   check (tag   <> ''),
    constraint team_label_not_empty check (label <> '')
);

create table match (
    id           uuid             primary key default gen_random_uuid(),
    match_date   date             not null,
    start_time   time             not null,
    end_time     time             not null,
    match_state  match_state_enum not null default 'not_started',
    field_id     uuid             not null references field(id),
    team_a_id    uuid             not null references team(id),
    team_b_id    uuid             not null references team(id),

    constraint match_period_valid   check (end_time > start_time),
    constraint match_teams_distinct check (team_a_id <> team_b_id)
);

create index idx_match_field_date on match (field_id, match_date);

create unique index idx_match_field_slot
    on match (field_id, match_date, start_time, end_time)
    where match_state <> 'cancelled';