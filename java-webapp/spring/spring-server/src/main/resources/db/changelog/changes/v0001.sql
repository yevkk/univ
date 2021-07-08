create table book (
    id serial not null,
    name text not null,
    author text not null,
    lang text not null,

    primary key (id)
);

create table book_stats (
    id serial not null,
    amount integer not null,
    total_requests integer not null,
    rate double precision not null,
    book_id serial not null,

    primary key (id),
    foreign key (book_id) references book (id) on delete cascade
);

create table delivery_type (
    id serial not null,
    description text not null,

    primary key (id)
);

create table book_request (
    id serial not null,
    datetime timestamp with time zone not null,
    book_id serial not null,
    user_id text not null,
    delivery_type_id serial not null,
    contact text not null,
    state text not null,

    primary key (id),
    foreign key (book_id) references book (id) on delete cascade,
    foreign key (delivery_type_id) references delivery_type (id) on delete cascade
);

create table book_request_return (
    id serial not null,
    datetime timestamp with time zone not null,
    request_id serial not null,
    state text not null,

    primary key (id),
    foreign key (request_id) references book_request (id) on delete cascade
);

create table book_rate_changelog (
    id serial not null,
    datetime timestamp with time zone not null,
    book_id serial not null,
    user_id text not null,
    contribution double precision not null,

    primary key (id),
    foreign key (book_id) references book (id) on delete cascade
);

create table book_balance_changelog (
    id serial not null,
    datetime timestamp with time zone not null,
    book_id serial not null,
    amount integer not null,
    comment text not null,

    primary key (id),
    foreign key (book_id) references book (id) on delete cascade
);
